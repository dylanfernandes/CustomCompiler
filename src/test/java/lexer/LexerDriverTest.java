package lexer;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LexerDriverTest {

    @Test
    public void getInputTest() throws FileNotFoundException {
        LexerDriver lex = new LexerDriver();
        assertEquals("Test" , lex.getInput());
    }

    @Test
    public void getOutputTest() {
        LexerDriver lex = new LexerDriver();
        assertTrue("Test" , lex.writeOutput("Test"));
    }
}
