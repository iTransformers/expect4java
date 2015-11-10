package net.itransformers.expect4java.cliconnection.utils;

import org.apache.log4j.Logger;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends FilterInputStream {

    Logger logger = Logger.getLogger(TeeInputStream.class.getName());
    private final OutputStream branch;


    public TeeInputStream(InputStream input, OutputStream branch) {
        super(input);
        this.branch = branch;
    }

    public void close() throws IOException {
        logger.info("Closing input stream.");
        super.close();
    }

    public int read() throws IOException {
        int ch = super.read();
        if (ch != -1) {
            branch.write(ch);
        }
        return ch;
    }

    public int read(byte[] bts, int st, int end) throws IOException {
        int n = super.read(bts, st, end);
        if (n != -1) {
            branch.write(bts, st, n);
        }
        return n;
    }

    public int read(byte[] bts) throws IOException {
        int n = super.read(bts);
        if (n != -1) {
            branch.write(bts, 0, n);
        }
        return n;
    }

}
