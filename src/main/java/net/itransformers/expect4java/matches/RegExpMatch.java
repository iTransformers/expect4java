/*
 * RegExpMatch.java
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
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;

public class RegExpMatch extends Match {
    private Perl5Compiler compiler = new Perl5Compiler();
    Pattern pattern;
    protected String patternStr;

    public RegExpMatch(String patternStr, Closure closure) throws MalformedPatternException {
        super(closure);
        this.patternStr = patternStr;
        pattern = compilePattern(patternStr);
    }

    public RegExpMatch(String patternStr) throws MalformedPatternException {
        this(patternStr,null);
    }

    protected Pattern compilePattern(String patternStr) throws MalformedPatternException {
        return compiler.compile(patternStr, Perl5Compiler.SINGLELINE_MASK); // |Perl5Compiler.MULTILINE_MASK
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "RegExpMatch{" +
                "patternStr='" + patternStr + '\'' +
                '}';
    }
}
