package lexer.inputFile;

import lexer.LexerDriver;
import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;
import utils.FileIOUtils;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class InputFile3Test {

    @Test
    public void testTokenRetriveal() throws FileNotFoundException {
        LexerDriver lexerDriver = new LexerDriver("src/input/examples/input3.txt");
        int numZero = 11;
        String input = FileIOUtils.getInput(lexerDriver.getInputLocation());
        Tokenizer tokenizer = new Tokenizer(input);

        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ERROR, token.getType());
        assertEquals("_", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("blob", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ASSGN, token.getType());
        assertEquals("=", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("t", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ERROR, token.getType());
        assertEquals("ë", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("st", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("/*Ę Foo bsrr ćç*/", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("15.23", token.getLexeme());

        for (int i = 0;i < numZero; i++) {
            token = tokenizer.nextToken();
            assertEquals(Token.TokenType.INT, token.getType());
            assertEquals("0", token.getLexeme());
        }

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("Integer", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INTEGER, token.getType());
        assertEquals("integer", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//*//", token.getLexeme());
    }
}
