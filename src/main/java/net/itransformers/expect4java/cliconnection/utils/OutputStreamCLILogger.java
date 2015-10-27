package net.itransformers.expect4java.cliconnection.utils;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCLILogger extends OutputStream {
    Logger logger = Logger.getLogger(OutputStreamCLILogger.class.getName());

    private ByteArrayOutputStream os;
    private boolean isOutputLogging;

    public OutputStreamCLILogger(boolean isOutputLogging) {
        this.isOutputLogging = isOutputLogging;
        os = new ByteArrayOutputStream();
    }
    public OutputStreamCLILogger(boolean isOutputLogging, int size) {
        this.isOutputLogging = isOutputLogging;
        os = new ByteArrayOutputStream(size);
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

    private void doFlush(){
        if (isOutputLogging) {
            logger.info(">>> " + os.toString());
        } else {
            logger.info("<<< " + os.toString());
        }
        os.reset();
    }

    private void doWrite(int b) throws IOException {
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
