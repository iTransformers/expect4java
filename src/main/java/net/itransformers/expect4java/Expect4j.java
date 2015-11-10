package net.itransformers.expect4java;


import net.itransformers.expect4java.matches.Match;
import net.itransformers.expect4java.matches.TimeoutMatch;

import java.io.*;

public interface Expect4j {
    void send(String str) throws IOException;
    int expect(Match match);
    int expect(Match[] matches);
    void setTimeout(TimeoutMatch timeoutMatch);
    void close() throws IOException;
    Reader getReader();
    Writer getWriter();
}
