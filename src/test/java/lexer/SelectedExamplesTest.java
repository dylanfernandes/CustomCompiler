package lexer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectedExamplesTest {
    @Test
    public void exampleTest1 () {
        Tokenizer tokenizer = new Tokenizer("0123");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("123", token.getLexeme());
    }

    @Test
    public void exampleTest2 () {
        Tokenizer tokenizer = new Tokenizer("01.23");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("1.23", token.getLexeme());
    }

    @Test
    public void exampleTest3 () {
        Tokenizer tokenizer = new Tokenizer("12.340");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("12.34", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());
    }

    @Test
    public void exampleTest4 () {
        Tokenizer tokenizer = new Tokenizer("012.340");
        Token token = token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("12.34", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());
    }

    @Test
    public void exampleTest5 () {
        Tokenizer tokenizer = new Tokenizer("12.34e01");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("12.34e0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("1", token.getLexeme());
    }

    @Test
    public void exampleTest6 () {
        Tokenizer tokenizer = new Tokenizer("12345.6789e-123");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("12345.6789e-123", token.getLexeme());
    }

    @Test
    public void exampleTest7 () {
        Tokenizer tokenizer = new Tokenizer("12345");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("12345", token.getLexeme());
    }

    @Test
    public void exampleTest8 () {
        Tokenizer tokenizer = new Tokenizer("abc");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc", token.getLexeme());
    }

    @Test
    public void exampleTest9 () {
        Tokenizer tokenizer = new Tokenizer("abc1");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc1", token.getLexeme());
    }

    @Test
    public void exampleTest10 () {
        Tokenizer tokenizer = new Tokenizer("abc_1");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc_1", token.getLexeme());
    }

    @Test
    public void exampleTest11 () {
        Tokenizer tokenizer = new Tokenizer("abc1_");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc1_", token.getLexeme());
    }

    @Test
    public void exampleTest12 () {
        Tokenizer tokenizer = new Tokenizer("_abc1");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ERROR, token.getType());
        assertEquals("_", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc1", token.getLexeme());
    }

    @Test
    public void exampleTest13 () {
        Tokenizer tokenizer = new Tokenizer("1abc");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("1", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc", token.getLexeme());
    }

    @Test
    public void exampleTest14 () {
        Tokenizer tokenizer = new Tokenizer("_1abc");
        Token token = token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ERROR, token.getType());
        assertEquals("_", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("1", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("abc", token.getLexeme());
    }
}
