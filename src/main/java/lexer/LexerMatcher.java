package lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LexerMatcher {

    private static String alpha = "^[a-zA-z]*$";
    private static String numeric = "^[0-9]*$";
    private static String alphaNum = "^[0-9a-z0-9_]*$";

    private LexerMatcher() {

    }

    public static boolean isAlpaha(String string) {
        return Pattern.compile(alpha).matcher(string).matches();
    }

    public static boolean isNumeric(String string) {
        return Pattern.compile(numeric).matcher(string).matches();
    }

    public static boolean isAlphaNum(String string) {
        return Pattern.compile(alphaNum).matcher(string).matches();
    }

}
