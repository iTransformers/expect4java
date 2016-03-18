/*
 * GlobMatch.java
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

package net.itransformers.expect4java.matches;

import net.itransformers.expect4java.Closure;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

public class GlobMatch extends RegExpMatch {

    public GlobMatch(String pattern) throws MalformedPatternException {
        super(pattern, null);
    }

    public GlobMatch(String pattern, Closure closure) throws MalformedPatternException {
        super(pattern, closure);
    }

    protected Pattern compilePattern(String patternStr) throws MalformedPatternException {
        int globOptions = GlobCompiler.QUESTION_MATCHES_ZERO_OR_ONE_MASK;
        char [] patternCh = patternStr.toCharArray();
        String perl5PatternStr = GlobCompiler.globToPerl5(patternCh, globOptions);
        return super.compilePattern(perl5PatternStr);
    }

    @Override
    public String toString() {
        return "GlobMatch{" +
                "patternStr='" + patternStr + '\'' +
                '}';
    }
}
