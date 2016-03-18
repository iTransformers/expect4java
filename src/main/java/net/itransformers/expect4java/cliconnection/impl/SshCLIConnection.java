/*
 * SshCLIConnection.java
 *
 * Copyright 2016  iTransformers Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.itransformers.expect4java.cliconnection.impl;

import com.jcraft.jsch.*;
import net.itransformers.expect4java.cliconnection.CLIConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

public class SshCLIConnection implements CLIConnection {
    protected ChannelShell channel;
    protected Session session;
    protected InputStream inputStream;
    protected OutputStream outputStream;

    public void connect(Map<String, Object> params) throws IOException {
        String address = (String) params.get("address");
        if (address == null) {
            throw new RuntimeException("Missing parameter: address");
        }
        Integer port = 22;
        if (params.get("port") != null && !(params.get("port") instanceof Integer)) {
            throw new RuntimeException("invalid format of port parameter: "+ params.get("port"));
        } else if (params.get("port") != null){
            port = (Integer)params.get("port");
        }

        String username = (String) params.get("username");
        if (username == null) {
            throw new RuntimeException("Missing parameter: username");
        }
        String password = (String) params.get("password");
        Integer timeout;
        if (params.get("timeout") != null && !( params.get("timeout") instanceof Integer)) {
            throw new RuntimeException("invalid format of timeout parameter: "+ params.get("timeout"));
        } else {
            timeout = (Integer) params.get("timeout");
        }
        JSch jsch = new JSch();
        Hashtable<String,String> config;
        if (params.get("config") != null && !(params.get("config") instanceof Hashtable)){
            throw new RuntimeException("invalid config parameter");
        }
        config = (Hashtable<String, String>) params.get("config");

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
