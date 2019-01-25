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

    /********************************
        ID TEST
     ********************************/

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

    /********************************
     INT TEST
     ********************************/

    @Test
    public void INTTest_0(){
        Tokenizer tokenizer = new Tokenizer("0");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("0", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void INTTest_NUM(){
        Tokenizer tokenizer = new Tokenizer("123435456");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("123435456", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void INTTest_TWONUM(){
        Tokenizer tokenizer = new Tokenizer("0123435456");
        Token token0 = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token0.getType());
        assertEquals("0", token0.getLexeme());
        assertEquals(0, token0.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token1.getType());
        assertEquals("123435456", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }

    @Test
    public void Div_Test(){
        Tokenizer tokenizer = new Tokenizer("/");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }
}
