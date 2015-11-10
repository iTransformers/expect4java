package net.itransformers.expect4java.cliconnection.utils;

import org.apache.log4j.Logger;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends FilterOutputStream {
    Logger logger = Logger.getLogger(TeeOutputStream.class.getName());
    private final OutputStream branch;


    public TeeOutputStream(OutputStream output, OutputStream branch) {
        super(output);
        this.branch = branch;
    }

    public void close() throws IOException {
        logger.info("Closing output stream.");
        super.close();
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        branch.write(b);
    }

}
