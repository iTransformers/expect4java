/*
 * EchoCLIConnection.java
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

import java.io.*;
import java.util.Map;

public class EchoCLIConnection implements CLIConnection{
    public static int BUFFER = 4*1024;
    protected PipedInputStream inputStream = new PipedInputStream(BUFFER);
    protected PipedOutputStream outputStream;

    @Override
    public void connect(Map<String, Object> params) throws IOException {
        outputStream = new PipedOutputStream(inputStream);
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
