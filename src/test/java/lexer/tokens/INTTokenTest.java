package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class INTTokenTest {
    /*************************************
     2.1.1 Integers
     *************************************/

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
    public void INTTest_Point(){
        Tokenizer tokenizer = new Tokenizer("123435456.");
        Token token0 = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token0.getType());
        assertEquals("123435456", token0.getLexeme());
        assertEquals(0, token0.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.POIN, token1.getType());
        assertEquals(".", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }

    @Test
    public void INTTest_NewLine(){
        Tokenizer tokenizer = new Tokenizer("123435456\n");
        Token token0 = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token0.getType());
        assertEquals("123435456", token0.getLexeme());
        assertEquals(0, token0.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token1.getType());
        assertEquals("\n", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }




}
