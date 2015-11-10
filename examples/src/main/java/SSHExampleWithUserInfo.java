import net.itransformers.expect4java.Expect4j;
import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.CLIStreamLogger;
import net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection;
import net.itransformers.expect4java.cliconnection.impl.SshCLIConnection;
import net.itransformers.expect4java.impl.Expect4jException;
import net.itransformers.expect4java.impl.Expect4jImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by vasko on 10.11.15.
 */
public class SSHExampleWithUserInfo {
    public static void main(String[] args) throws IOException, Expect4jException {

        Properties props = new Properties();
        File file = new File("local.properties");
        props.load(new FileInputStream(file));
        CLIStreamLogger inStreamLogger = message -> {
            System.out.println("<<< ");
        };
        CLIStreamLogger outStreamLogger = message -> {
            System.out.println(">>> ");
        };

        CLIConnection sshConn = new LoggableCLIConnection(new SshCLIConnection(), inStreamLogger, outStreamLogger);
        Map<String, Object> connParams = new HashMap<>();
        Properties props3 = new Properties();
        props3.put("StrictHostKeyChecking", "no");
        connParams.put("username", props.getProperty("username"));
        connParams.put("password", props.getProperty("password"));
        connParams.put("address", props.getProperty("address"));
        connParams.put("port", Integer.parseInt(props.getProperty("ssh_port")));
        connParams.put("timeout", Integer.parseInt(props.getProperty("timeout")));
        connParams.put("config", props3);
        connParams.put("userInfo", new MySimpleUserInfo(props.getProperty("password")));

        sshConn.connect(connParams);

        Expect4j e4j = new Expect4jImpl(sshConn);
        e4j.send("ls");

    }
}
