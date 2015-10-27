package net.itransformers.expect4java.matches;

import net.itransformers.expect4java.Closure;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

public class GlobMatch extends RegExpMatch {

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
