/*
 * Expect4jLoggingTest.java
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

package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.CLIConnection;
import net.itransformers.expect4java.cliconnection.impl.EchoCLIConnection;
import net.itransformers.expect4java.cliconnection.impl.LoggableCLIConnection;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by vasko on 09.11.15.
 */
public class Expect4jLoggingTest {
    private Expect4jImpl e4j;
    private CLIConnection cliConnection;
    private InMemoryCLIStreamLogger inLogger;
    private InMemoryCLIStreamLogger outLogger;

    @Before
    public void setUp() throws IOException, Expect4jException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        inLogger = new InMemoryCLIStreamLogger("(localhost) >>> ");
        outLogger = new InMemoryCLIStreamLogger("(nms) <<< ");
        cliConnection = new LoggableCLIConnection(new EchoCLIConnection(), inLogger, outLogger);
        cliConnection.connect(new HashMap<>());

        e4j = new Expect4jImpl(cliConnection);


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
    public void testLogging() throws IOException, MalformedPatternException {
        final MutableBoolean status = new MutableBoolean(false);
        e4j.send("h");
        e4j.send("ello\n");
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