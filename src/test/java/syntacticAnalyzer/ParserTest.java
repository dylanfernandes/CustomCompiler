package syntacticAnalyzer;

import lexer.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void nextTokenEmpty() {
        List<Token> tokens = new ArrayList<Token>();
        Parser parser = new Parser(tokens);
        assertEquals(null, parser.nextToken());
        assertFalse(parser.match(Token.TokenType.ID));
    }

    @Test
    public void nextTokenValid() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        Token aToken2 = new Token(Token.TokenType.INT, "12", 1);
        tokens.add(aToken);
        tokens.add(aToken2);
        Parser parser = new Parser(tokens);
        assertEquals(aToken2, parser.nextToken());
    }

    @Test
    public void match() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        tokens.add(aToken);
        Parser parser = new Parser(tokens);
        assertTrue(parser.match(Token.TokenType.ID));
    }

    @Test
    public void skipErrorsFirst() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        tokens.add(aToken);
        List<Token.TokenType> first = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.ID);
        Parser parser = new Parser(tokens);
        assertTrue(parser.skipErrors(first, null));
    }

    @Test
    public void skipErrorsFollow() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        tokens.add(aToken);

        Token aToken2 = new Token(Token.TokenType.INTEGER, "test", 1);
        tokens.add(aToken);


        List<Token.TokenType> first = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.INTEGER);

        List<Token.TokenType> follow = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.ID);

        Parser parser = new Parser(tokens);
        assertTrue(parser.skipErrors(first, follow));
    }

    @Test
    public void skipErrorsInvalid() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.NEQ, "test", 1);
        tokens.add(aToken);

        List<Token.TokenType> first = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.INTEGER);

        List<Token.TokenType> follow = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.ID);

        Parser parser = new Parser(tokens);
        assertFalse(parser.skipErrors(first, follow));
    }

    @Test
    public void skipErrorsFollowIndirect() {
        List<Token> tokens = new ArrayList<Token>();
        Token aToken = new Token(Token.TokenType.ID, "test", 1);
        tokens.add(aToken);

        List<Token.TokenType> first = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.INTEGER);

        List<Token.TokenType> follow = new ArrayList<Token.TokenType>();
        first.add(Token.TokenType.ID);

        Parser parser = new Parser(tokens);
        assertTrue(parser.skipErrors(first, follow));
    }

}