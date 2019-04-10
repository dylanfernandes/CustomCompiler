package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import syntacticAnalyzer.ParserAST;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DemoTests {
    ParserAST parser;

    LexerDriver lexerDriver;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
        parser = new ParserAST();
    }

    /*************************************
     3.1.1 Declarations
     *************************************/

    @Test
    public void validDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer test; " +
                "float temp; " +
                "Foo foo;" +
                " integer arr[2]; " +
                "Bar bar[5];" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    /*************************************
     3.1.1 Free Functions
     *************************************/

    @Test
    public void validFreeFunctionDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer func(){};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFreeFunctionWithVarsDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer func()" +
                "{" +
                "integer foo; " +
                "float temp;" +
                "};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFreeFunctionWithVarsAndParamsDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer func(integer var, float bar)" +
                "{" +
                "integer foo; " +
                "float temp;" +
                "};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
}
