package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import syntacticAnalyzer.Parser;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class invalidSyntaxTest {

    Parser parser = new Parser();

    LexerDriver lexerDriver;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
    }

    @Test
    public void invalidClassVariableFunctionSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test; \n" +
                "Blob blob; \n" +
                "integer test();\n" +
                "float fl; \n" +
                "float test2(); \n" +
                "Blobl test3(); }; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isParseGood());
    }

    @Test
    public void invalidVarArray() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id[id]; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isParseGood());
    }

    @Test
    public void invalidFuncDefSrProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob Bar test(){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        //error in syntax found
        assertTrue(parser.isFoundError());
        //compiler recovered from error
        assertTrue(parser.isParseGood());
    }
}
