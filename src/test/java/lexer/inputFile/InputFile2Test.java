package lexer.inputFile;

import lexer.LexerDriver;
import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class InputFile2Test {

    @Test
    public void testTokenRetriveal() throws FileNotFoundException {
        LexerDriver lexerDriver = new LexerDriver("src/input/examples/input2.txt");
        String input = lexerDriver.getInput();
        Tokenizer tokenizer = new Tokenizer(input);

        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("void", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("test", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OPAR, token.getType());
        assertEquals("(", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CPAR, token.getType());
        assertEquals(")", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OBRA, token.getType());
        assertEquals("{", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("id_0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ASSGN, token.getType());
        assertEquals("=", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("5", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.SEMI, token.getType());
        assertEquals(";", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.IF, token.getType());
        assertEquals("if", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OPAR, token.getType());
        assertEquals("(", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INT, token.getType());
        assertEquals("5", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.EQ, token.getType());
        assertEquals("==", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("id_0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CPAR, token.getType());
        assertEquals(")", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.RETURN, token.getType());
        assertEquals("return", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("id_0", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.SEMI, token.getType());
        assertEquals(";", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.RETURN, token.getType());
        assertEquals("return", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("3.14", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.SEMI, token.getType());
        assertEquals(";", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CBRA, token.getType());
        assertEquals("}", token.getLexeme());
    }
}
