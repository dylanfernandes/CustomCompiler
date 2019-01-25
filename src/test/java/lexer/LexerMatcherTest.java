package lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class LexerMatcherTest {

    @Test
    public void AlphaTest() {
        assertTrue(LexerMatcher.isAlpaha("a"));
        assertTrue(LexerMatcher.isAlpaha("abcx"));
        assertFalse(LexerMatcher.isAlpaha("\u00C6"));
        assertFalse(LexerMatcher.isAlpaha("123"));
    }

    @Test
    public void NumericTest() {
        assertTrue(LexerMatcher.isNumeric("123123436758780"));
        assertTrue(LexerMatcher.isNumeric("1"));
        assertFalse(LexerMatcher.isNumeric("asd"));
    }

    @Test
    public void AlphaNumTest() {
        assertTrue(LexerMatcher.isAlphaNum("123123436758780"));
        assertTrue(LexerMatcher.isAlphaNum("1"));
        assertTrue(LexerMatcher.isAlphaNum("asd"));
        assertTrue(LexerMatcher.isAlphaNum("123123436758780_dfsdf"));
        assertFalse(LexerMatcher.isAlphaNum("123\u00C6asdas123"));
    }
}