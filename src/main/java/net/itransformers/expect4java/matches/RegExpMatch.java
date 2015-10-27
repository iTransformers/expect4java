package net.itransformers.expect4java.matches;

import net.itransformers.expect4java.Closure;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;

public class RegExpMatch extends Match {
    private static Perl5Compiler compiler = new Perl5Compiler();
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
