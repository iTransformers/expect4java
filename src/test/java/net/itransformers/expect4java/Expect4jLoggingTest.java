package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.CLIConnectionLogger;
import net.itransformers.expect4java.cliconnection.impl.EchoCLIConnection;
import net.itransformers.expect4java.impl.Expect4jException;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.GlobMatch;
import net.itransformers.expect4java.matches.Match;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.oro.text.regex.MalformedPatternException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by vasko on 09.11.15.
 */
public class Expect4jLoggingTest {
    private Expect4jImpl e4j;
    private EchoCLIConnection cliConnection;
    private InMemoryCLIConnectionLogger inLogger;
    private InMemoryCLIConnectionLogger outLogger;

    @Before
    public void setUp() throws IOException, Expect4jException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        cliConnection = new EchoCLIConnection();
        cliConnection.connect(new HashMap<>());
        inLogger = new InMemoryCLIConnectionLogger("(localhost) >>> ");
        outLogger = new InMemoryCLIConnectionLogger("(nms) <<< ");

        e4j = new Expect4jImpl(cliConnection, inLogger, outLogger);


    }

    @Test
    public void testEof() throws IOException, MalformedPatternException, Expect4jException {
        final MutableBoolean status = new MutableBoolean(false);
        e4j.send("hello\n");
        e4j.expect(new Match[]{
                new GlobMatch("hello\n", it -> {
                    System.out.println("Hello World!");
                    e4j.getWriter().close();
                    System.out.println("reader closed");
                    it.exp_continue();
                }),
                new EofMatch(it1 -> status.setValue(true))
        });
        Assert.assertTrue(status.booleanValue());
        Assert.assertEquals(1, inLogger.getMessages().size());
        Assert.assertEquals("(localhost) >>> hello[\\n]", inLogger.getMessages().get(0));
        Assert.assertEquals(1, outLogger.getMessages().size());
        Assert.assertEquals("(nms) <<< hello[\\n]", outLogger.getMessages().get(0));
    }


    @Test
    public void tearDown() throws IOException {
        e4j.close();
        cliConnection.disconnect();
    }
}