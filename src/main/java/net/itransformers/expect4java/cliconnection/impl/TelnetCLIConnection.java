package net.itransformers.expect4java.cliconnection.impl;

import net.itransformers.expect4java.cliconnection.CLIConnection;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class TelnetCLIConnection implements CLIConnection{

    private TelnetClient telnet = new TelnetClient();

    @Override
    public void connect(Map<String, Object> params) throws IOException {
//        logger.info("Open telnet connection to: " + host + ":" + port);
        try {
            if (!params.containsKey("port")) {
                throw new IllegalArgumentException("no port parameter is specified");
            }
            Integer port = (Integer) params.get("port");
            telnet.connect((String) params.get("address"), port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        telnet.setDefaultTimeout(2000);
    }

    @Override
    public void disconnect() throws IOException {
        telnet.disconnect();
    }

    @Override
    public InputStream inputStream() {
        return telnet.getInputStream();
    }

    @Override
    public OutputStream outputStream() {
        return telnet.getOutputStream();
    }

}
