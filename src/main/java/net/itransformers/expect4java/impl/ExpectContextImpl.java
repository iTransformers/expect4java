/*
 * ExpectContextImpl.java
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
