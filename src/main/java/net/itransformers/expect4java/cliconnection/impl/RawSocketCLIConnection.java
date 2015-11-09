package net.itransformers.expect4java.cliconnection.impl;

import net.itransformers.expect4java.cliconnection.CLIConnection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class RawSocketCLIConnection implements CLIConnection {
    protected Socket socket;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected Logger logger = Logger.getLogger(RawSocketCLIConnection.class.getName());

    public RawSocketCLIConnection() {
    }

    public void connect(Map<String, Object> params) throws IOException {
        String address = (String) params.get("address");
        if (address == null) {
            throw new RuntimeException("Missing parameter: address");
        }
        if (!(params.get("port") instanceof Integer)) {
            throw new RuntimeException("invalid format of port in address parameter: "+ address);
        }
        Integer port = (Integer) params.get("port");
        logger.info("Establishing connection ...");
        socket = new Socket(address, port);
        inputStream  = socket.getInputStream();
        outputStream  = socket.getOutputStream();
        logger.info("Connection established");
    }

    public InputStream inputStream(){
        return inputStream;
    }

    public OutputStream outputStream() {
        return outputStream;
    }

    public void disconnect() throws IOException {
        if (socket!=null){
            socket.close();
        }
    }

}
