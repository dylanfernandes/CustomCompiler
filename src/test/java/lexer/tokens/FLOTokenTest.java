package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FLOTokenTest {

    /********************************
     FLOAT TEST
     ********************************/

    @Test
    public void FLOATTest_Zero_Dot_Zero(){
        Tokenizer tokenizer = new Tokenizer("0.0");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("0.0", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_Num_Dot_Zero(){
        Tokenizer tokenizer = new Tokenizer("2.0");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("2.0", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_Num_Dot_ZeroNum(){
        Tokenizer tokenizer = new Tokenizer("3.014159");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.014159", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_ZeroEnd(){
        Tokenizer tokenizer = new Tokenizer("3.0141590");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.014159", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token1.getType());
        assertEquals("0", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }

    @Test
    public void FLOATTest_Num_Dot_Num(){
        Tokenizer tokenizer = new Tokenizer("3.14159");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14159", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_ExponentSign(){
        Tokenizer tokenizer = new Tokenizer("3.14159e+20");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14159e+20", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_ExponentNoSign(){
        Tokenizer tokenizer = new Tokenizer("3.14159e20");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14159e20", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void FLOATTest_ExponentSignIncomplete(){
        Tokenizer tokenizer = new Tokenizer("3.14159e+");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14159", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token1.getType());
        assertEquals("e", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());

        Token token2 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ADD, token2.getType());
        assertEquals("+", token2.getLexeme());
        assertEquals(0, token2.getLineNumber());
    }

    @Test
    public void FLOATTest_ExponentNoSignIncomplete(){
        Tokenizer tokenizer = new Tokenizer("3.14159e");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14159", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token1.getType());
        assertEquals("e", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }

}
