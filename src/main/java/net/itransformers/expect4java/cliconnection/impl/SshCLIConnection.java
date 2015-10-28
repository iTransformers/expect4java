package net.itransformers.expect4java.cliconnection.impl;

import com.jcraft.jsch.*;
import net.itransformers.expect4java.cliconnection.CLIConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

public class SshCLIConnection implements CLIConnection {
    public static final int DEFAULT_TIMEOUT = 60000;
    private ChannelShell channel;
    Session session;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void connect(Map<String, Object> params) throws IOException {
        String address = (String) params.get("address");
        if (address == null) {
            throw new RuntimeException("Missing parameter: address");
        }
        if (!(params.get("port") instanceof Integer)) {
            throw new RuntimeException("invalid format of port parameter: "+ address);
        }
        Integer port = (Integer)params.get("port");
        String username = (String) params.get("username");
        if (username == null) {
            throw new RuntimeException("Missing parameter: username");
        }
        String password = (String) params.get("password");
        Integer timeout = 5000;
        if (params.get("timeout") != null && !( params.get("timeout") instanceof Integer)) {
            throw new RuntimeException("invalid format of timeout parameter: "+ params.get("timeout"));
        }
        timeout = (Integer) params.get("timeout");
        JSch jsch = new JSch();
        Hashtable<String,String> config;
        if (params.get("config") != null && !(params.get("config") instanceof Hashtable)){
            throw new RuntimeException("invalid config parameter");
        } else {
            config = (Hashtable<String, String>) params.get("config");
        }

        UserInfo userInfo = null;
        if (params.get("userInfo") != null && (params.get("userInfo") instanceof UserInfo)){
            userInfo = (UserInfo) params.get("userInfo");
        }


        try {
            session = jsch.getSession(username, address, port);
            if (password != null) {
                session.setPassword(password);
            }
            if (config != null) {
                session.setConfig(config);
            }
            if (userInfo != null) {
                session.setUserInfo(userInfo);
            }
            if (timeout != null) {
                session.connect(timeout);
            }
            channel = (ChannelShell) session.openChannel("shell");
            inputStream = channel.getInputStream();
            outputStream = channel.getOutputStream();
            channel.connect();
        } catch (JSchException e) {
            throw new IOException(e);
        }

    }

    public InputStream inputStream() {
        return inputStream;
    }

    public OutputStream outputStream() {
        return outputStream;
    }

    public void disconnect() throws IOException {
        try { if (channel != null) channel.disconnect(); } catch (RuntimeException e) { e.printStackTrace();}
        try { if (session != null) session.disconnect(); } catch (RuntimeException e) { e.printStackTrace();}
    }

}
