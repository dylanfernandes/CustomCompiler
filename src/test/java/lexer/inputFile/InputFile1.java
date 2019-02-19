package lexer.inputFile;

import lexer.LexerDriver;
import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;
import utils.FileIOUtils;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class InputFile1 {

    @Test
    public void testTokenRetriveal() throws FileNotFoundException {
        LexerDriver lexerDriver = new LexerDriver("src/input/examples/input1.txt");
        String input = FileIOUtils.getInput(lexerDriver.getInputLocation());
        Tokenizer tokenizer = new Tokenizer(input);

        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.INTEGER, token.getType());
        assertEquals("integer", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.MAIN, token.getType());
        assertEquals("main", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OPAR, token.getType());
        assertEquals("(", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CPAR, token.getType());
        assertEquals(")", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OBRA, token.getType());
        assertEquals("{", token.getLexeme());


        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CMT, token.getType());
        assertEquals("//comment", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ID, token.getType());
        assertEquals("a", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ASSGN, token.getType());
        assertEquals("=", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.FLO, token.getType());
        assertEquals("2.3e-10", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEWLINE, token.getType());
        assertEquals("\n", token.getLexeme());

        token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CBRA, token.getType());
        assertEquals("}", token.getLexeme());
    }
}
