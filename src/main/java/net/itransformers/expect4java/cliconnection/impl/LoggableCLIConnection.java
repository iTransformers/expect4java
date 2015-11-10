package net.itransformers.expect4java.cliconnection.impl;

import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.CLIStreamLogger;
import net.itransformers.expect4java.cliconnection.utils.OutputStreamCLILogger;
import net.itransformers.expect4java.cliconnection.utils.TeeInputStream;
import net.itransformers.expect4java.cliconnection.utils.TeeOutputStream;
import net.itransformers.expect4java.impl.Expect4jImpl;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by vasko on 10.11.15.
 */
public class LoggableCLIConnection implements CLIConnection {
    private TeeInputStream is;
    private TeeOutputStream os;
    private CLIConnection cliConnection;
    private final CLIStreamLogger inStreamLogger;
    private CLIStreamLogger outStreamLogger;

    public LoggableCLIConnection(CLIConnection cliConnection) {
        this(cliConnection, message -> {
                    LoggerFactory.getLogger(Expect4jImpl.class).info(">>> " + message);
                },
                message -> {
                    LoggerFactory.getLogger(Expect4jImpl.class).info("<<< " + message);
                }
        );

    }

    public LoggableCLIConnection(CLIConnection cliConnection, CLIStreamLogger inStreamLogger, CLIStreamLogger outStreamLogger) {
        this.cliConnection = cliConnection;
        this.inStreamLogger = inStreamLogger;
        this.outStreamLogger = outStreamLogger;
        if (inStreamLogger == null) {
            throw new IllegalArgumentException("Input logger cannot be null");
        }
        if (outStreamLogger == null) {
            throw new IllegalArgumentException("Output logger cannot be null");
        }
    }

    @Override
    public void connect(Map<String, Object> params) throws IOException {
        cliConnection.connect(params);
        InputStream origIs = cliConnection.inputStream();
        OutputStream origOs = cliConnection.outputStream();
        is = new TeeInputStream(origIs, new OutputStreamCLILogger(inStreamLogger));
        os = new TeeOutputStream(origOs, new OutputStreamCLILogger(outStreamLogger));
    }

    @Override
    public void disconnect() throws IOException {
        is.close();
        os.close();
        cliConnection.disconnect();
    }

    @Override
    public InputStream inputStream() {
        return is;
    }

    @Override
    public OutputStream outputStream() {
        return os;
    }
}
