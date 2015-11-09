package net.itransformers.expect4java;

import net.itransformers.expect4java.cliconnection.CLIConnectionLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasko on 09.11.15.
 */
public class InMemoryCLIConnectionLogger implements CLIConnectionLogger {
    private final String prefix;

    public InMemoryCLIConnectionLogger(String prefix) {
        this.prefix = prefix;
    }

    private List<String> messages = new ArrayList<>();
    @Override
    public void log(String message) {
        messages.add(prefix + message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
