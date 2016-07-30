/*
 * TelnetCLIConnection.java
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

import net.itransformers.expect4java.cliconnection.CLIConnection;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class TelnetCLIConnection implements CLIConnection{

    protected TelnetClient telnet = new TelnetClient();

    @Override
    public void connect(Map<String, Object> params) throws IOException {
//        logger.info("Open telnet connection to: " + host + ":" + port);
        try {
            if (!params.containsKey("port")) {
                throw new IllegalArgumentException("no port parameter is specified");
            }
            Integer port = null;
            Object portObj = params.get("port");
            if (portObj instanceof String) {
                port = Integer.parseInt((String) portObj);
            } else if (portObj instanceof Integer) {
                port = (Integer) portObj;
            } else {
                throw new IllegalArgumentException("Port parameter should be Integer or String: "+ portObj);
            }
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
