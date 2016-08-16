package net.itransformers.expect4java.cliconnection.impl;

import net.itransformers.expect4java.cliconnection.CLIConnection;

import java.io.*;
import java.util.Map;

public class CrossPipedCLIConnection implements CLIConnection{
    public static int BUFFER = 4*1024;
    protected PipedInputStream inputStream = new PipedInputStream(BUFFER);
    protected PipedOutputStream outputStream;

    @Override
    public void connect(Map<String, Object> params) throws IOException {
        PipedInputStream is = (PipedInputStream) params.get("input");
        outputStream = new PipedOutputStream(is);
    }

    @Override
    public void disconnect() throws IOException {
        outputStream.close();
        inputStream.close();
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public OutputStream outputStream() {
        return outputStream;
    }
}
