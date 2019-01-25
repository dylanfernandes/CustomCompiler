package lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerMatcherTest {

    @Test
    public void AlphaTest() {
        assertTrue(LexerMatcher.isAlpaha('a'));
        assertTrue(LexerMatcher.isAlpaha("abcx"));

        assertFalse(LexerMatcher.isAlpaha('\u00C6'));
        assertFalse(LexerMatcher.isAlpaha("123"));
        assertFalse(LexerMatcher.isAlpaha("abctothe123"));
        assertFalse(LexerMatcher.isAlpaha("abc_to_the_123"));
    }

    @Test
    public void NumericTest() {
        assertTrue(LexerMatcher.isNumeric("123123436758780"));
        assertTrue(LexerMatcher.isNumeric('1'));
        assertFalse(LexerMatcher.isNumeric("asd"));
    }

    @Test
    public void AlphaNumTest() {
        assertTrue(LexerMatcher.isAlphaNum("123123436758780"));
        assertTrue(LexerMatcher.isAlphaNum('1'));
        assertTrue(LexerMatcher.isAlphaNum("If"));
        assertFalse(LexerMatcher.isAlpaha('\u00C6'));
        assertTrue(LexerMatcher.isAlphaNum("asd"));
        assertTrue(LexerMatcher.isAlphaNum("123123436758780_dfsdf"));
        assertFalse(LexerMatcher.isAlphaNum("123\u00C6asdas123"));
    }

    @Test
    public void TokenableTest() {
        assertTrue(LexerMatcher.isTokenable('='));
        assertTrue(LexerMatcher.isTokenable('/'));
        assertTrue(LexerMatcher.isTokenable("<="));
        assertTrue(LexerMatcher.isTokenable(">="));
        assertTrue(LexerMatcher.isTokenable(".,+*/!><=&|:{}[]()"));

        assertFalse(LexerMatcher.isTokenable('\u00C6'));
        assertFalse(LexerMatcher.isTokenable('\n'));
    }

    @Test
    public void SpecialCharTest() {
        assertTrue(LexerMatcher.isSpecialChar('\n'));
        assertTrue(LexerMatcher.isSpecialChar('\r'));
        assertTrue(LexerMatcher.isSpecialChar('\b'));
        assertTrue(LexerMatcher.isSpecialChar('\f'));
        assertTrue(LexerMatcher.isSpecialChar('\t'));

        assertFalse(LexerMatcher.isSpecialChar('\u00C6'));
    }

}