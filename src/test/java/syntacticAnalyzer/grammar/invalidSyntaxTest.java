package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import syntacticAnalyzer.Parser;
import syntacticAnalyzer.ParserAST;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class invalidSyntaxTest {

    ParserAST parser = new ParserAST();

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
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer id[]; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isParseGood());
    }

    /*************************************
     3.2.3 Error Recovery
     *************************************/
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
    @Test
    public void invalidExtraParenthesisProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main { integer bar();};");
        parser .setTokenList(tokens);

        parser.parse();
        //error in syntax found
        assertTrue(parser.isFoundError());
        //compiler recovered from error
        assertTrue(parser.isParseGood());
    }
    @Test
    public void invalidEAssignProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main { var == bar + 2;};");
        parser .setTokenList(tokens);

        parser.parse();
        //error in syntax found
        assertTrue(parser.isFoundError());
        //compiler recovered from error
        assertTrue(parser.isParseGood());
    }
    @Test
    public void invalidExpressionProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main { var = bar */ 2;};");
        parser .setTokenList(tokens);

        parser.parse();
        //error in syntax found
        assertTrue(parser.isFoundError());
        //compiler recovered from error
        assertTrue(parser.isParseGood());
    }
    @Test
    public void invalidExtraClassProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main { class test bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        //error in syntax found
        assertTrue(parser.isFoundError());
        //compiler recovered from error
        assertTrue(parser.isParseGood());
    }
}
