import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;
import net.itransformers.expect4java.Expect4j;
import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.CLIStreamLogger;
import net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection;
import net.itransformers.expect4java.cliconnection.impl.SshCLIConnection;
import net.itransformers.expect4java.impl.Expect4jException;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by vasko on 10.11.15.
 */
public class SSHExample {
    public static void main(String[] args) throws IOException, Expect4jException {
        JSch.setLogger(new MyLogger());

        Properties props = new Properties();
        File file = new File("local.properties");
        props.load(new FileInputStream(file));

        CLIStreamLogger inStreamLogger = message -> {
            System.out.println("<<< "+message);
        };
        CLIStreamLogger outStreamLogger = message -> {
            System.out.println(">>> "+message);
        };

        CLIConnection sshConn = new LoggableCLIConnection(new SshCLIConnection(), inStreamLogger, outStreamLogger);
        Map<String, Object> connParams = new HashMap<>();
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications",
                "keyboard-interactive,password");
        connParams.put("username", props.getProperty("username"));
        connParams.put("password", props.getProperty("password"));
        connParams.put("address", props.getProperty("address"));
        connParams.put("port", Integer.parseInt(props.getProperty("ssh_port")));
        connParams.put("timeout", Integer.parseInt(props.getProperty("timeout")));
        connParams.put("config", config);
        UserInfo ui=new MySimpleUserInfo(props.getProperty("password"));
        connParams.put("userInfo", ui);

        sshConn.connect(connParams);

        Expect4j e4j = new Expect4jImpl(sshConn);
        e4j.send("ls\n");
        e4j.send("exit\n");
        e4j.expect(new EofMatch());
        e4j.close();
        sshConn.disconnect();

    }
    public static class MyLogger implements com.jcraft.jsch.Logger {
        static java.util.Hashtable name=new java.util.Hashtable();
        static{
            name.put(new Integer(DEBUG), "DEBUG: ");
            name.put(new Integer(INFO), "INFO: ");
            name.put(new Integer(WARN), "WARN: ");
            name.put(new Integer(ERROR), "ERROR: ");
            name.put(new Integer(FATAL), "FATAL: ");
        }
        public boolean isEnabled(int level){
            return true;
        }
        public void log(int level, String message){
            System.err.print(name.get(new Integer(level)));
            System.err.println(message);
        }
    }
}
