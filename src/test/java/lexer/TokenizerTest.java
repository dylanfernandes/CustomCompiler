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


}
