package net.itransformers.expect4java.matches;

import net.itransformers.expect4java.Closure;

public class EofMatch extends Match {

    public EofMatch() {
        super(null);
    }

    public EofMatch(Closure closure) {
        super(closure);
    }

    @Override
    public String toString() {
        return "EofMatch{}";
    }
}
