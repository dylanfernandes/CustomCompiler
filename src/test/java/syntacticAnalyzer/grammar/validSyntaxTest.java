package syntacticAnalyzer.grammar;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnitRunner;
import syntacticAnalyzer.Parser;
import syntacticAnalyzer.ParserAST;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class validSyntaxTest {

    ParserAST parser = new ParserAST();

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
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClass() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExt() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob : Foo {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExtSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Bar {}; \n" +
                "class Blob : Foo, Bar {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassExtMore() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob : Foo, Bar {}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassVariable() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test;}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
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
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassFunction() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob { integer test();}; \n" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
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
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefParamProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefParamSeveralProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test, float test2){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefVarDeclProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob test(integer test){float test2 = test;}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        //assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validFuncDefSrProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("Blob Bar :: test(){}; main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarDeclIdProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validlIdNestProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {id = id.id(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }


    @Test
    public void validVarArray() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { Blob id[9]; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }


    @Test
    public void validVarDeclTypeProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer id; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validStatementProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { read(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validWriteProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { write(id); };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
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
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validForProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { \n" +
                "for(id id = id; id < id; id = id);\n" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validInherit() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Utility : InheritedUtility {}; main{};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validLargeArray() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main{integer var1[4][5][7][8][9][1][0]; };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validArrayParam() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test {integer findMax(integer array[100]);  };\n" +
                "main{}; \n ");
        parser .setTokenList(tokens);

        parser.parse();

        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validVarIndice() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main{id[id];};");
        parser .setTokenList(tokens);

        parser.parse();

        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validAssignArray() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main{id[id] = id;};");
        parser .setTokenList(tokens);

        parser.parse();

        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validArith() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main{value = 100 * (2 + 3.0 / 7.0006); };");
        parser .setTokenList(tokens);

        parser.parse();

        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validArithCond() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main{value = 1.05 + ((2.04 * 2.47) - 3.0) + 7.0006 > 1 && ! - 1; };");
        parser .setTokenList(tokens);

        parser.parse();

        //assertFalse(parser.isFoundError());
        //assertTrue(parser.isParseGood());
    }
}
