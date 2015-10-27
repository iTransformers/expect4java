package net.itransformers.expect4java.impl;

import net.itransformers.expect4java.ExpectContext;
import org.apache.oro.text.regex.MatchResult;

public class ExpectContextImpl implements ExpectContext {
    private MatchResult match;
    private String buffer;
    private boolean expContinue;
    private boolean resetTimer;

    public ExpectContextImpl(String buffer) {
        this(null, buffer);
    }
    public ExpectContextImpl(MatchResult match, String buffer) {
        this.match = match;
        this.buffer = buffer;
        this.expContinue = false;
        this.resetTimer = false;
    }

    @Override
    public void exp_continue() {
        expContinue = true;
    }

    @Override
    public void exp_continue_reset_timer() {
        expContinue = true;
        resetTimer = true;
    }

    @Override
    public String getBuffer() {
        return buffer;
    }

    @Override
    public String getMatch(int groupnum) {
        return match == null ? null : match.group(groupnum);
    }

    @Override
    public String getMatch() {
        return getMatch(0);
    }

    public boolean isExpContinue() {
        return expContinue;
    }

    public boolean isResetTimer() {
        return resetTimer;
    }
}
