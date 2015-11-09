package net.itransformers.expect4java.cliconnection.utils;

import net.itransformers.expect4java.cliconnection.CLIConnectionLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCLILogger extends OutputStream {
    CLIConnectionLogger logger;

    protected ByteArrayOutputStream os;

    public OutputStreamCLILogger(CLIConnectionLogger logger) {
        os = new ByteArrayOutputStream();
        this.logger = logger;
    }

    public OutputStreamCLILogger(CLIConnectionLogger logger, int size) {
        os = new ByteArrayOutputStream(size);
        this.logger = logger;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        super.write(bytes);
        doFlush();
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
            doWrite(b[off + i]);
        }
        doFlush();
    }

    @Override
    public void write(int b) throws IOException {
        doWrite(b);
        if (b == '\r' || b == '\n'){
            doFlush();
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
}
