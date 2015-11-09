package net.itransformers.expect4java.impl;

import net.itransformers.expect4java.Closure;
import net.itransformers.expect4java.Expect4j;
import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.CLIConnectionLogger;
import net.itransformers.expect4java.cliconnection.utils.OutputStreamCLILogger;
import net.itransformers.expect4java.cliconnection.utils.TeeInputStream;
import net.itransformers.expect4java.cliconnection.utils.TeeOutputStream;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.Match;
import net.itransformers.expect4java.matches.RegExpMatch;
import net.itransformers.expect4java.matches.TimeoutMatch;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class Expect4jImpl implements Expect4j, Runnable {

    public static final long DEFAULT_TIMEOUT = 1000l;
    private final Reader reader;
    private final Writer writer;
    private TimeoutMatch defaultTimeoutMatch = new TimeoutMatch(DEFAULT_TIMEOUT);
    StringBuffer buffer = new StringBuffer(256);
    boolean eofFound = false;
    boolean finished = false;
    Logger logger;

    public Expect4jImpl(CLIConnection cliConnection) throws Expect4jException {
        this(cliConnection,
                message -> {
                    LoggerFactory.getLogger(Expect4jImpl.class).info(">>> " + message);
                },
                message -> {
                    LoggerFactory.getLogger(Expect4jImpl.class).info("<<< " + message);
                }
        );
    }

    public Expect4jImpl(CLIConnection cliConnection, CLIConnectionLogger inConnLogger, CLIConnectionLogger outConnLogger) throws Expect4jException {
        this.logger = LoggerFactory.getLogger(Expect4jImpl.class);
        InputStream is = cliConnection.inputStream();
        OutputStream os = cliConnection.outputStream();
        if (is == null) {
            throw new Expect4jException("The input stream in the connection is null");
        }
        if (os == null) {
            throw new Expect4jException("The output stream in the connection is null");
        }
        if (inConnLogger != null) {
            is = new TeeInputStream(is, new OutputStreamCLILogger(inConnLogger));
        }
        if (outConnLogger != null) {
            os = new TeeOutputStream(os, new OutputStreamCLILogger(outConnLogger));
        }
        this.reader = new InputStreamReader(is);
        this.writer = new OutputStreamWriter(os);
        init();
    }

    public Expect4jImpl(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
        init();
    }

    private void init() {
        Thread consumerThread = new Thread(this);
        consumerThread.start();
    }


    @Override
    public void send(String str) throws IOException {
//        logger.info("Sending: "+ str); // logged by tee logger
        writer.write(str);
        writer.flush();
    }

    @Override
    public int expect(Match match) {
        return this.expect(new Match[]{match});
    }

    @Override
    public int expect(Match[] matches) {
        if (matches == null || matches.length == 0)
            throw new IllegalArgumentException("Input argument cannot be null or zero length.");
        StopWatch stopWatch = new StopWatch();
        logger.debug("Watch started...");
        stopWatch.start();
        Perl5Matcher matcher = new Perl5Matcher();
        int i;
        while (true) {
            synchronized (this) {
                boolean hasMatch = false;
                ExpectContextImpl expectContext = null;
                for (i = 0; i < matches.length; i++) {
                    if (matches[i] instanceof RegExpMatch) {
                        RegExpMatch regExpMatch = (RegExpMatch) matches[i];
                        logger.debug("Checking match No: " + i + ", " + regExpMatch.toString().replace("\r", "[\\r]").replace("\n", "\\n"));
                        String input = buffer.toString();
                        logger.debug("Input: " + input.replace("\r", "[\\r]").replace("\n", "\\n"));
                        if (matcher.contains(input, regExpMatch.getPattern())) {
                            MatchResult result = matcher.getMatch();
                            buffer = new StringBuffer();
                            buffer.append(input.substring(result.beginOffset(0) + result.end(0)));
                            logger.debug("Matched! Invoking match closure...");
                            expectContext = invokeClosure(regExpMatch, input, result);
                            hasMatch = true;
                            break;
                        }
                    } else if (matches[i] instanceof EofMatch) {
                        logger.debug("Checking match No: " + i + ", EofMatch");
                        if (eofFound) {
                            logger.debug("EOF found! Invoking match closure...");
                            EofMatch eofMatch = (EofMatch) matches[i];
                            expectContext = invokeClosure(eofMatch, buffer.toString(), null);
                            hasMatch = true;
                            break;
                        }
                    }
                }
                if (!hasMatch) {
                    TimeoutMatch timeoutMatch = findTimeoutMatch(matches);
                    long deltaTime = timeoutMatch.getTimeout() - stopWatch.getTime();
                    logger.debug("First pass no match found. Delta time=" + deltaTime);
                    if (deltaTime <= 0) {
                        if (timeoutMatch.getClosure() == null) {
                            throw new RuntimeException("Expect timeouted, while expecting: " +
                                    matchesToDump(matches) + " input buffer:" + buffer.toString());
                        }
                        logger.debug("Timeout exceeded. Invoking timeout closure");
                        expectContext = invokeClosure(timeoutMatch, buffer.toString(), null);
                        logger.debug("exp_continue: " + expectContext.isExpContinue() +
                                ", reset_timer: " + expectContext.isResetTimer());
                        if (expectContext.isExpContinue() && expectContext.isResetTimer()) {
                            logger.debug("Resetting stopWatch");
                            stopWatch.reset();
                        } else {
                            logger.debug("Exit expect method due to timeout");
                            break;
                        }
                    } else {
                        logger.debug("wait for input for " + deltaTime + " ms");
                        waitForInput(deltaTime);
                    }
                } else {
                    logger.debug("First pass no match found. ");
                    logger.debug("exp_continue: " + expectContext.isExpContinue() +
                            ", reset_timer: " + expectContext.isResetTimer());
                    if (!expectContext.isExpContinue()) {
                        logger.debug("Exit expect method due to exp_continue=false");
                        break;
                    } else {
                        if (expectContext.isResetTimer()) {
                            logger.debug("Resetting stopWatch");
                            stopWatch.reset();
                        }
                        logger.debug("continue expect method due to exp_continue=true");
                    }
                }
            }
        }
        return i;
    }

    private synchronized void waitForInput(Long timeout) {
        try {
            wait(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private TimeoutMatch findTimeoutMatch(Match[] matches) {
        for (Match match : matches) {
            if (match instanceof TimeoutMatch) {
                return (TimeoutMatch) match;
            }
        }
        return defaultTimeoutMatch;
    }

    private ExpectContextImpl invokeClosure(Match match, String input, MatchResult result) {
        ExpectContextImpl expectContext;
        try {
            expectContext = new ExpectContextImpl(result, input);
            Closure closure = match.getClosure();
            if (closure != null) {
                closure.run(expectContext);
            } else {
                logger.debug("No closure defined for this match. Skipping call it");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return expectContext;
    }


    @Override
    public void setTimeout(TimeoutMatch timeoutMatch) {
        if (timeoutMatch == null) {
            throw new IllegalArgumentException("Default timeout cannot be null.");
        }
        this.defaultTimeoutMatch = timeoutMatch;
    }

    @Override
    public void run() {
        try {
            while (!finished) {
                char cs[] = new char[256];
                int size = reader.read(cs);
                synchronized (this) {
                    if (size != -1) {
                        buffer.append(cs, 0, size);
                    } else {
                        eofFound = true;
                        break;
                    }
                    notifyAll();
                }
            }
        } catch (IOException e) {
            logger.debug("IO Error in run method", e);
        }
    }

    public void close() {
        finished = true;

    }

    private String matchesToDump(Match[] matches) {
        StringBuilder sb = new StringBuilder();
        for (Match match : matches) {
            sb.append(match.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
    }
}
