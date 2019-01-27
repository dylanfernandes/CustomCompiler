package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleTokenTest {
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
    public void DivTest(){
        Tokenizer tokenizer = new Tokenizer("/");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }
}
