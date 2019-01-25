package lexer;

import java.util.regex.Pattern;

public final class LexerMatcher {

    private static String alpha = "^[a-zA-Z]*$";
    private static String numeric = "^[0-9]*$";
    private static String alphaNum = "^[0-9a-zA-Z_]*$";
    private static String tokenable = "^[\\.,+-\\\\*/!><=&|:{}\\[\\]()]*$";

    private LexerMatcher() {

    }

    public static boolean isAlpaha(char ch) {
        return isAlpaha(ch+"");
    }

    public static boolean isAlpaha(String string) {
        return Pattern.compile(alpha).matcher(string).matches();
    }

    public static boolean isNumeric(char ch) {
        return isNumeric(ch+"");
    }

    public static boolean isNumeric(String string) {
        return Pattern.compile(numeric).matcher(string).matches();
    }

    public static boolean isAlphaNum(char ch) {
        return isAlphaNum(ch+"");
    }

    public static boolean isAlphaNum(String string) {
        return Pattern.compile(alphaNum).matcher(string).matches();
    }
    public static boolean isTokenable(char ch) {
        return isTokenable(ch+"");
    }

    public static boolean isTokenable(String string) {
        return Pattern.compile(tokenable).matcher(string).matches();
    }

    public static boolean isSpecialChar(char ch){
        return (ch == '\n' || ch == '\b' || ch == '\f' || ch == '\t' || ch == '\r');
    }



}
