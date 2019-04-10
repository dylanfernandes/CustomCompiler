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
     3.1.3 Free Functions
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

    /*************************************
     3.1.4 Member Functions
     *************************************/
    @Test
    public void validMemberFunctionDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer Blob::func()" +
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
    public void validMemberFunctionWithVarsAndParamsDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer Blob::func(integer var, float bar)" +
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

    /*************************************
     3.1.5 Class Declarations
     *************************************/
    @Test
    public void validClassDeclarationProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassDeclarationMembersProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{" +
                "integer bar;" +
                "float temp;" +
                "Foo foo;};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassDeclarationMembersMethodsProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{" +
                "integer bar;" +
                "float temp;" +
                "Foo foo;" +
                "integer Calc();" +
                "Foo compute(integer a[5], float b);};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassDeclarationInheritProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test:Blob{};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validClassDeclarationInheritSeveralProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test:Blob, Foo{};" +
                "main { };");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    /*************************************
     3.1.6 Expressions
     *************************************/
    @Test
    public void validExpressionArithProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "integer foo;" +
                "integer bar;" +
                "foo = 5 * 7 /9 + bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validExpressionRelAndLogicProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "foo = bar < 7 && !bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validExpressionRelAndLogic2Program() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "foo = bar <> 7 || !bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validExpressionRelAndLogic3Program() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "foo = bar > 7 || !bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validExpressionRelAndLogic4Program() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "foo = bar <= 7 && bar;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    /*************************************
     3.1.7 Conditional Statements
     *************************************/
    @Test
    public void validCondStatementProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "if(1)then else;};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validCondStatementBodyProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "if(1)then{var = 1;}else{var =2;};};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    @Test
    public void validCondStatementNestedBodyProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "if(1)then{if(1)then else;}else{var =2;};};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }

    /*************************************
     3.1.8 Loop Statements
     *************************************/
    @Test
    public void validLoopStatementEmptyProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "for(integer i = 0;i > 4;i = i - 1);" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validLoopStatementProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "for(integer i = 0;i > 4;i = i - 1){var =1;};" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validLoopStatementNestedProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "for(integer i = 0;i > 4;i = i - 1)" +
                "for(integer i = 0;i > 4;i = i - 1);;" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    /*************************************
     3.1.9 Read/Write/Return Statements
     *************************************/
    @Test
    public void validReadProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "read(var);};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validWriteProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "write(var*var);};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validReturnProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "return(var*var/var+1);};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    /*************************************
     3.1.10 Class Member Access
     *************************************/
    @Test
    public void validClassAccessProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob.bar;" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validClassAccessNestedProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob.bar.foo;" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validClassAccessNestedArrayProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob.bar.foo[5];" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    /*************************************
     3.1.11 Array Access
     *************************************/
    @Test
    public void validArrayAccessProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob[5];" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validArrayMultiDimensionAccessProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob[5][4][7];" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
    @Test
    public void validArrayMultiDimensionExpressionAccessProgram() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { " +
                "var = blob[5*7/bar][4][7-temp/foo];" +
                "};");
        parser .setTokenList(tokens);

        parser.parse();
        assertFalse(parser.isFoundError());
        assertTrue(parser.isParseGood());
    }
}
