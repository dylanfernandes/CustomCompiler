package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IDTokenTest {

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

    @Test
    public void IDTest_NewLine (){
        Tokenizer tokenizer = new Tokenizer("aToken\n");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("aToken", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }
}
