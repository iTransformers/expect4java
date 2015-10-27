package net.itransformers.expect4java.impl;

/**
 * Created by vasko on 27.10.15.
 */
public class Expect4jException  extends Exception{
    public Expect4jException() {
    }

    public Expect4jException(String message) {
        super(message);
    }

    public Expect4jException(String message, Throwable cause) {
        super(message, cause);
    }

    public Expect4jException(Throwable cause) {
        super(cause);
    }

    public Expect4jException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
