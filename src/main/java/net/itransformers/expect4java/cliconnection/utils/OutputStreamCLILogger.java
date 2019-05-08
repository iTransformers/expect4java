/*
 * OutputStreamCLILogger.java
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

package net.itransformers.expect4java.cliconnection.utils;

import net.itransformers.expect4java.cliconnection.CLIStreamLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCLILogger extends OutputStream {
    CLIStreamLogger logger;
    private char lineTerminator;

    protected ByteArrayOutputStream os;

    public OutputStreamCLILogger(CLIStreamLogger logger, char lineTerminator) {
        this.lineTerminator = lineTerminator;
        os = new ByteArrayOutputStream();
        this.logger = logger;
    }

    public OutputStreamCLILogger(CLIStreamLogger logger, int size, char lineTerminator) {
        os = new ByteArrayOutputStream(size);
        this.logger = logger;
        this.lineTerminator = lineTerminator;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        super.write(bytes);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (b == lineTerminator){
            doWrite(b);
            doFlush();
        } else {
            doWrite(b);
        }
    }

    protected void doFlush(){
        logger.log(os.toString());
        os.reset();
    }

    protected void doWrite(int b) throws IOException {
        if (b < 32){ // non printable characters
            switch (b) {
                case 0x0D : os.write(("[\\r]").getBytes()); break;
                case 0x09 : os.write(("[\\t]").getBytes()); break;
                case 0x0A : os.write(("[\\n]").getBytes()); break;
                case 0x0C : os.write(("[\\f]").getBytes()); break;
                case 0x07 : os.write(("[\\a]").getBytes()); break;
                case 0x1B : os.write(("[\\e]").getBytes()); break;
                case 0x1D : os.write(("[\\]]").getBytes()); break;
                default: os.write((String.format("[\\x%02X]",b)).getBytes());
            }
        } else { // printable characters
            os.write(b);
        }
    }

    @Override
    public void close() throws IOException {
        if (os != null) {
            doFlush();
            os = null;
        }
    }

    @Override
    public void flush() throws IOException {
        doFlush();
    }
}
