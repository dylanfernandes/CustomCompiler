package lexer;

import org.junit.Test;
import utils.FileIOUtils;

import java.io.FileNotFoundException;
import java.util.List;

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

    @Test
    public void getTokensFromInput() {
        LexerDriver lexerDriver = new LexerDriver();
        List<Token> tokens =  lexerDriver.getTokensFromInput("class test");
        assertEquals(Token.TokenType.CLASS, tokens.get(0).getType());
        assertEquals(Token.TokenType.ID, tokens.get(1).getType());
    }
}
