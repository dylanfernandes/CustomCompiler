package lexer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectedExamplesTests {
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

}
