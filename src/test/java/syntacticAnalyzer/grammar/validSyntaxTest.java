package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;
import syntacticAnalyzer.Parser;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class validSyntaxTest {

    Parser parser = new Parser();

    LexerDriver lexerDriver;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
    }

    @Test
    public void validEmptyProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClass() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExt() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob : Foo {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExtSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Bar {}; \n" +
                "class Blob : Foo, Bar {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExtMore() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob : Foo, Bar {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassVariable() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test;}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassVariableSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test; \n" +
                "Blob blob; \n" +
                "float fl;}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassFunction() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test();}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassVariableFunctionSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test; \n" +
                "Blob blob; \n" +
                "float fl; \n" +
                "integer test();\n" +
                "float test2(); \n" +
                "Blobl test3(); }; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefParamProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefParamSeveralProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test, float test2){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefVarDeclProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test){float test2 = test;}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefSrProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob Bar :: test(){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarDeclIdProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarArray() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id[9]; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }


    @Test
    public void validVarDeclTypeProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validStatementProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { read(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validWriteProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { write(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validIfProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { \n" +
                "if(id) then \n" +
                "{return(id);} \n" +
                "else \n" +
                "{return(id);}; \n" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validForProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { \n" +
                "for(id id = id; id < id; id = id);\n" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertTrue(parser.isParseGood());
    }
}
