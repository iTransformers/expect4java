package net.itransformers.expect4java.matches;


import net.itransformers.expect4java.Closure;

public abstract class Match {
    
    Closure closure;

    public Match(Closure closure) {
        this.closure = closure;
    }

    public Closure getClosure() {
        return closure;
    }
}
