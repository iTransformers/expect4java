package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.CLIStreamLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasko on 09.11.15.
 */
public class InMemoryCLIStreamLogger implements CLIStreamLogger {
    private final String prefix;

    public InMemoryCLIStreamLogger(String prefix) {
        this.prefix = prefix;
    }

    private List<String> messages = new ArrayList<>();
    @Override
    public void log(String message) {
        System.out.println(prefix + message);
        messages.add(prefix + message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
