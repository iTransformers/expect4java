package net.itransformers.expect4java.matches;

import net.itransformers.expect4java.Closure;

public class TimeoutMatch extends Match{
    Long timeout;

    public TimeoutMatch(Long timeout) {
        this(timeout, null);
    }

    public TimeoutMatch(Closure closure) {
        this(null, closure);
    }

    public TimeoutMatch(Long timeout, Closure closure) {
        super(closure);
        this.timeout = timeout;
    }

    public Long getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "TimeoutMatch{" +
                "timeout=" + timeout +
                '}';
    }
}
