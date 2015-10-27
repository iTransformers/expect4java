package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.impl.EchoCLIConnection;
import net.itransformers.expect4java.impl.Expect4jException;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.GlobMatch;
import net.itransformers.expect4java.matches.Match;
import net.itransformers.expect4java.matches.RegExpMatch;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.oro.text.regex.MalformedPatternException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Vasil Yordanov on 10/27/2015.
 */
public class Expect4JavaTest {
    private Expect4jImpl e4j;
    private EchoCLIConnection cliConnection;

    @Before
    public void setUp() throws IOException, Expect4jException {
        cliConnection = new EchoCLIConnection();
        cliConnection.connect(new HashMap<>());
        e4j = new Expect4jImpl(cliConnection, true);


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
    }

    @Test
    public void testRegexpArrTwoMatch() throws IOException, MalformedPatternException {
        final MutableBoolean status = new MutableBoolean(false);
        final MutableBoolean firsMatch = new MutableBoolean(false);
        e4j.send("hello World\n");
        e4j.send("hello2 World\n");
        e4j.expect(new Match[]{
                new RegExpMatch("hello ([^\n]*)\n", (ExpectContext it) -> {
                    System.out.println("Hello " + it.getMatch(1));
                    firsMatch.setValue(true);
                    it.exp_continue();
                }),
                new RegExpMatch("hello2 ([^\n]*)\n", (ExpectContext context2) -> {
                    System.out.println("Hello2 " + context2.getMatch(1));
                    if (firsMatch.booleanValue()) status.setValue(true);
                })
        });

        Assert.assertTrue(status.booleanValue());
    }

    @Test
    public void tearDown() throws IOException {
        e4j.close();
        cliConnection.disconnect();
    }
}
