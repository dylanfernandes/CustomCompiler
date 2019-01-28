package lexer;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class LexerDriverTest {

    @Test
    public void getInputTest() throws FileNotFoundException {
        LexerDriver lex = new LexerDriver("src/test/java/lexer/input/testInput.txt");
        assertEquals("Test test" , lex.getInput());
    }

    @Test
    public void getOutputTest() {
        LexerDriver lex = new LexerDriver();
        assertTrue("Test" , lex.writeOutput("Test", "src/test/java/lexer/output/testOutput.txt"));
    }

    @Test
    public void printContentTest () {
        Token token = new Token(Token.TokenType.INT, "123", 55);
        assertEquals("[55:INT:123]",LexerDriver.printTokenContent(token));
    }

    @Test
    public void printAtoCCTest () {
        Token token = new Token(Token.TokenType.ID, "Test", 12);
        assertEquals("ID",LexerDriver.printAtoCC(token));
    }
}
