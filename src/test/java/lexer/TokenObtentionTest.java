package lexer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenObtentionTest {

    @Test
    public void NullTest (){
        Tokenizer tokenizer = new Tokenizer("");
        Token token = tokenizer.nextToken();
        assertEquals(null, token);
    }

    @Test
    public void PointTest (){
        Tokenizer tokenizer = new Tokenizer(".");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.POIN, token.getType());
        assertEquals(".", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void CommaTest (){
        Tokenizer tokenizer = new Tokenizer(",");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.COMM, token.getType());
        assertEquals(",", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void IDTest_ReservedWord (){
        Tokenizer tokenizer = new Tokenizer("If");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("If", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void IDTest_AlphaNum (){
        Tokenizer tokenizer = new Tokenizer("Abc_To_The_123");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("Abc_To_The_123", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void IDTest_SpaceEnd (){
        Tokenizer tokenizer = new Tokenizer("aToken ");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("aToken", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }
}
