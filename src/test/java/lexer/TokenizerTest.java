package lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenizerTest {


    @Test
    public void NextCharTest () {
        Tokenizer tokenizer = new Tokenizer("Test \n");
        assertEquals(false, tokenizer.isEndOfInput());
        assertEquals('T', tokenizer.nextChar().charValue());
        assertEquals('e', tokenizer.nextChar().charValue());
        assertEquals('s', tokenizer.nextChar().charValue());
        assertEquals('t', tokenizer.nextChar().charValue());
        assertEquals(' ', tokenizer.nextChar().charValue());
        assertEquals('\n', tokenizer.nextChar().charValue());
        assertEquals(null, tokenizer.nextChar());
        assertEquals(true, tokenizer.isEndOfInput());
    }

    @Test
    public  void BackupTest() {
        Tokenizer tokenizer = new Tokenizer("Test \n");
        assertEquals(false, tokenizer.backup());
        assertFalse(-2 == tokenizer.getInputPosition());
        assertEquals(-1, tokenizer.getInputPosition());

        assertEquals('T', tokenizer.nextChar().charValue());
        assertEquals(0, tokenizer.getInputPosition());
        assertEquals(true, tokenizer.backup());
        assertEquals(-1, tokenizer.getInputPosition());
    }

    @Test
    public void BackupLexemeTest() {
        Tokenizer tokenizer = new Tokenizer("Test \n");
        tokenizer.setCurrentLexeme("Te");
        tokenizer.setInputPosition(3);
        assertEquals(true, tokenizer.backup());
        assertEquals(2, tokenizer.getInputPosition());
        assertEquals("Te", tokenizer.getCurrentLexeme());
    }

    @Test
    public  void BackupCharTest() {
        Tokenizer tokenizer = new Tokenizer("Test \n");
        assertEquals(false, tokenizer.backupChar());
        assertFalse(-2 == tokenizer.getInputPosition());
        assertEquals(-1, tokenizer.getInputPosition());

        assertEquals('T', tokenizer.nextChar().charValue());
        assertEquals(0, tokenizer.getInputPosition());
        assertEquals(true, tokenizer.backupChar());
        assertEquals(-1, tokenizer.getInputPosition());
    }

    @Test
    public void BackupCharLexemeTest() {
        Tokenizer tokenizer = new Tokenizer("Test \n");
        tokenizer.setCurrentLexeme("Te");
        tokenizer.setInputPosition(3);
        assertEquals(true, tokenizer.backupChar());
        assertEquals(2, tokenizer.getInputPosition());
        assertEquals("T", tokenizer.getCurrentLexeme());
    }

    @Test
    public void NewLinesTest() {
        Tokenizer tokenizer = new Tokenizer("Test \n ,\r Else");

        Token token1 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token1.getType());
        assertEquals("Test", token1.getLexeme());
        assertEquals(0, token1.getLineNumber());

        Token token2 = tokenizer.nextToken();
        assertEquals(Token.TokenType.COMM, token2.getType());
        assertEquals(",", token2.getLexeme());
        assertEquals(1, token2.getLineNumber());

        Token token3 = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token3.getType());
        assertEquals("Else", token3.getLexeme());
        assertEquals(2, token3.getLineNumber());
    }

}
