package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.impl.SshCLIConnection;
import net.itransformers.expect4java.impl.Expect4jImpl;
import net.itransformers.expect4java.matches.EofMatch;
import net.itransformers.expect4java.matches.GlobMatch;
import net.itransformers.expect4java.matches.Match;
import net.itransformers.expect4java.matches.RegExpMatch;
import org.apache.oro.text.regex.MalformedPatternException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Vasil Yordanov on 10/27/2015.
 */
public class Expect4JavaTest {
    public void test() throws IOException, MalformedPatternException {
        Expect4j e4j = new Expect4jImpl(new SshCLIConnection(), true);
        e4j.send("sss");
        e4j.expect(new Match[]{
                new RegExpMatch("", c -> {
                    System.out.println("Hello");
                    e4j.send("rrr");
                    e4j.expect(new GlobMatch("", c1 -> {

                    }));
                    c.exp_continue();
                })
        });
    }
    @Test
    public void testEof() throws IOException, MalformedPatternException {
        Expect4j e4j = new Expect4jImpl(new SshCLIConnection(), true);
        final Boolean status = false;
        e4j.send("hello\n");
        e4j.expect(new Match[]{
                new GlobMatch("hello\n", it -> {
                    System.out.println("Hello World!");
                    e4j.getWriter().close();
                    System.out.println("reader closed");
                    it.exp_continue();
                }),
//                new EofMatch(it1->status=true)
        });
        Assert.assertTrue(status);
    }
}
