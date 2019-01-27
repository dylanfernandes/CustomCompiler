package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReservedWordTokenTest {

    /********************************
     RESERVED WORDS TEST
     ********************************/
    @Test
    public void ReservedWordTest_IF(){
        Tokenizer tokenizer = new Tokenizer("if");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.IF, token.getType());
        assertEquals("if", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_THEN(){
        Tokenizer tokenizer = new Tokenizer("then");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.THEN, token.getType());
        assertEquals("then", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_ELSE(){
        Tokenizer tokenizer = new Tokenizer("else");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ELSE, token.getType());
        assertEquals("else", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_FOR(){
        Tokenizer tokenizer = new Tokenizer("for");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FOR, token.getType());
        assertEquals("for", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_CLASS(){
        Tokenizer tokenizer = new Tokenizer("class");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CLASS, token.getType());
        assertEquals("class", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_INTEGER(){
        Tokenizer tokenizer = new Tokenizer("integer");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INTEGER, token.getType());
        assertEquals("integer", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_FLOAT(){
        Tokenizer tokenizer = new Tokenizer("float");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLOAT, token.getType());
        assertEquals("float", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_READ(){
        Tokenizer tokenizer = new Tokenizer("read");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.READ, token.getType());
        assertEquals("read", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_WRITE(){
        Tokenizer tokenizer = new Tokenizer("write");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.WRITE, token.getType());
        assertEquals("write", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_RETURN(){
        Tokenizer tokenizer = new Tokenizer("return");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.RETURN, token.getType());
        assertEquals("return", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ReservedWordTest_MAIN(){
        Tokenizer tokenizer = new Tokenizer("main");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.MAIN, token.getType());
        assertEquals("main", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

}
