package lexer;

import org.junit.Test;
import utils.FileIOUtils;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class LexerDriverTest {

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
