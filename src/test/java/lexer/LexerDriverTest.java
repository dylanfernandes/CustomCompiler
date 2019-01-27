package lexer;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class LexerDriverTest {

    @Test
    public void getInputTest() throws FileNotFoundException {
        LexerDriver lex = new LexerDriver();
        assertEquals("Test\ntest", lex.getInput());
    }
}
