/*
 * LoggableCLIConnection.java
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
    private final char isLineTerminator;
    private final char osLineTerminator;
    private char lineTerminator;

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
        this(cliConnection, inStreamLogger, outStreamLogger, '\n', '\n');
    }

    public LoggableCLIConnection(CLIConnection cliConnection,
                                 CLIStreamLogger inStreamLogger,
                                 CLIStreamLogger outStreamLogger,
                                 char isLineTerminator,
                                 char osLineTerminator) {
        this.cliConnection = cliConnection;
        this.inStreamLogger = inStreamLogger;
        this.outStreamLogger = outStreamLogger;
        this.isLineTerminator = isLineTerminator;
        this.osLineTerminator = osLineTerminator;
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
        is = new TeeInputStream(origIs, new OutputStreamCLILogger(inStreamLogger, isLineTerminator));
        os = new TeeOutputStream(origOs, new OutputStreamCLILogger(outStreamLogger, osLineTerminator));
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
