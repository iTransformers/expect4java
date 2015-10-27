package net.itransformers.expect4java;

public interface ExpectContext {
    void exp_continue();
    void exp_continue_reset_timer();
    String getBuffer();
    String getMatch(int groupnum);
    String getMatch();
}
