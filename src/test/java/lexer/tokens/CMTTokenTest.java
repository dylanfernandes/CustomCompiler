package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CMTTokenTest {

    /********************************
     SINGLE COMMENT TEST
     ********************************/
    @Test
    public void SingleCommentTest_Empty(){
        Tokenizer tokenizer = new Tokenizer("//");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void SingleCommentTest_Long(){
        Tokenizer tokenizer = new Tokenizer("//thisisallongcomment\n");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//thisisallongcomment", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void SingleCommentTest_Repeat(){
        Tokenizer tokenizer = new Tokenizer("//thisisallongcomment//\n");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//thisisallongcomment//", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void SingleCommentTest_Two(){
        Tokenizer tokenizer = new Tokenizer("//thisisallongcomment\n//Next Comment");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//thisisallongcomment", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token1.getType());
        assertEquals("//Next Comment", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());
    }

    /********************************
     MULTI COMMENT TEST
     ********************************/

    @Test
    public void MultiCommentTest_Empty(){
        Tokenizer tokenizer = new Tokenizer("/*");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.MULT, token1.getType());
        assertEquals("*", token1.getLexeme());
    }

    @Test
    public void MultiCommentTest_Good(){
        Tokenizer tokenizer = new Tokenizer("/*This is a multiline comment*/");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("/*This is a multiline comment*/", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void MultiCommentTest_GoodEmpty(){
        Tokenizer tokenizer = new Tokenizer("/**/");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("/**/", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void MultiCommentTest_NewLine(){
        Tokenizer tokenizer = new Tokenizer("/**/\n");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("/**/", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void MultiCommentTest_Incomplete(){
        Tokenizer tokenizer = new Tokenizer("/*Test");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.MULT, token1.getType());
        assertEquals("*", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());

        Token token2 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token2.getType());
        assertEquals("Test", token2.getLexeme());
        assertEquals(0, token2.getLineNumber());
    }

    @Test
    public void MultiCommentTest_IncompleteStar(){
        Tokenizer tokenizer = new Tokenizer("/*Test*");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.MULT, token1.getType());
        assertEquals("*", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());

        Token token2 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token2.getType());
        assertEquals("Test", token2.getLexeme());
        assertEquals(0, token2.getLineNumber());

        Token token3 = tokenizer.nextToken();
        assertEquals(Token.TokenType.MULT, token3.getType());
        assertEquals("*", token3.getLexeme());
        assertEquals(0, token3.getLineNumber());
    }

}
