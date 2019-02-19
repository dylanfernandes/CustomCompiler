package syntacticAnalyzer;

import lexer.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void nextTokenEmpty() {
        List<Token> tokens = new ArrayList<Token>();
        Parser parser = new Parser(tokens);
        assertEquals(null, parser.nextToken());
    }

    @Test
    public void nextTokenValid() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        tokens.add(aToken);
        Parser parser = new Parser(tokens);
        assertEquals(aToken, parser.nextToken());
    }

    @Test
    public void match() {
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        assertTrue(Parser.match(aToken, Token.TokenType.ID));
    }
}