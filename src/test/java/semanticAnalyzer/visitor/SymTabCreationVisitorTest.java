package semanticAnalyzer.visitor;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import semanticAnalyzer.SemanticPhases;
import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.EntryType;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import syntacticAnalyzer.ParserAST;
import syntacticAnalyzer.ParserDriver;

import java.util.List;

import static org.junit.Assert.*;

public class SymTabCreationVisitorTest {

    ParserDriver parserDriver;

    LexerDriver lexerDriver;

    SemanticPhases semanticPhases;

    SymbolTable symbolTable;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
        parserDriver = new ParserDriver();
        semanticPhases = new SemanticPhases();
    }

    @Test
    public void classHeader() {
        SymbolTableEntry class1;

        List<Token> tokens = lexerDriver.getTokensFromInput("class test{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("test"));
        class1 = symbolTable.search("test");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("test", class1.getName());
    }

    @Test
    public void classVar() {
        SymbolTable bar;
        SymbolTableEntry class1;

        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{integer id;};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("bar"));
        class1 = symbolTable.search("bar");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("bar", class1.getName());

        bar = class1.getLink();
        assertEquals(0, bar.find("id"));
        class1 = bar.search("id");

        assertEquals("id", class1.getName());
        assertEquals(EntryKind.VARIABLE, class1.getEntryKind());
        assertEquals("integer", class1.getEntryType().getElementType().getType());
    }

    @Test
    public void classVarFunc() {
        SymbolTable bar;
        SymbolTable classFunc;
        SymbolTableEntry class1;
        SymbolTableEntry func;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{integer id; foo classFunc(integer bar);};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("bar"));
        class1 = symbolTable.search("bar");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("bar", class1.getName());

        bar = class1.getLink();
        assertEquals(0, bar.find("id"));
        class1 = bar.search("id");

        assertEquals("id", class1.getName());
        assertEquals(EntryKind.VARIABLE, class1.getEntryKind());
        assertEquals("integer", class1.getEntryType().getElementType().getType());


        assertEquals(1, bar.find("classFunc"));
        func = bar.search("classFunc");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("classFunc", func.getName());
        assertEquals("foo", func.getEntryType().getElementType().getType());
        assertEquals("classFunc", func.getLink().getName());

        classFunc = func.getLink();
        assertEquals(0, classFunc.find("bar"));
        param = classFunc.search("bar");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("bar", param.getName());
        assertEquals("integer", param.getEntryType().getElementType().getType());
    }

    @Test
    public void classFuncSeveral() {
        SymbolTable bar;
        SymbolTable classFunc;
        SymbolTableEntry class1;
        SymbolTableEntry func;

        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{foo classFunc();integer classFunc2();};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("bar"));
        class1 = symbolTable.search("bar");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("bar", class1.getName());

        bar = class1.getLink();

        assertEquals(0, bar.find("classFunc"));
        func = bar.search("classFunc");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("classFunc", func.getName());
        assertEquals("foo", func.getEntryType().getElementType().getType());
        assertEquals("classFunc", func.getLink().getName());

        assertEquals(1, bar.find("classFunc2"));
        func = bar.search("classFunc2");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("classFunc2", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("classFunc2", func.getLink().getName());
    }

    @Test
    public void classSeveralVar() {
        SymbolTable bar;
        SymbolTableEntry class1;

        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{integer id; foo bar;};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("bar"));
        class1 = symbolTable.search("bar");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("bar", class1.getName());

        bar = class1.getLink();
        assertEquals(0, bar.find("id"));
        class1 = bar.search("id");

        assertEquals("id", class1.getName());
        assertEquals(EntryKind.VARIABLE, class1.getEntryKind());
        assertEquals("integer", class1.getEntryType().getElementType().getType());

        assertEquals(1, bar.find("bar"));
        class1 = bar.search("bar");

        assertEquals("bar", class1.getName());
        assertEquals(EntryKind.VARIABLE, class1.getEntryKind());
        assertEquals("foo", class1.getEntryType().getElementType().getType());
    }

    @Test
    public void classVarArray() {
        SymbolTable bar;
        SymbolTableEntry class1;

        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{integer id[12];};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("bar"));
        class1 = symbolTable.search("bar");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("bar", class1.getName());

        bar = class1.getLink();
        assertEquals(0, bar.find("id"));
        class1 = bar.search("id");

        assertEquals("id", class1.getName());
        assertEquals(EntryKind.VARIABLE, class1.getEntryKind());
        assertEquals("integer", class1.getEntryType().getElementType().getType());
        assertTrue(class1.getEntryType().getElementType().isArray());
        assertEquals(1, class1.getEntryType().getElementType().getNumDimensions());
        assertEquals("12", class1.getEntryType().getElementType().getSingleDimension(0));
    }

    @Test
    public void classHeaderInherit() {
        SymbolTableEntry class1;
        SymbolTableEntry classInherit;
        SymbolTable classTable;

        List<Token> tokens = lexerDriver.getTokensFromInput("class test:blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("test"));
        class1 = symbolTable.search("test");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("test", class1.getName());
        assertEquals("test", class1.getLink().getName());

        classTable = class1.getLink();

        assertEquals(0, classTable.find("blob"));
        classInherit = classTable.search("blob");

        assertEquals(EntryKind.INHERIT, classInherit.getEntryKind());
        assertEquals("blob", classInherit.getName());
    }

    @Test
    public void classHeaderInheritSeveral() {
        SymbolTableEntry class1;
        SymbolTableEntry classInherit;
        SymbolTable classTable;

        List<Token> tokens = lexerDriver.getTokensFromInput("class test:blob, foo{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("test"));
        class1 = symbolTable.search("test");

        assertEquals(EntryKind.CLASS, class1.getEntryKind());
        assertEquals("test", class1.getName());
        assertEquals("test", class1.getLink().getName());

        classTable = class1.getLink();

        assertEquals(0, classTable.find("blob"));
        classInherit = classTable.search("blob");

        assertEquals(EntryKind.INHERIT, classInherit.getEntryKind());
        assertEquals("blob", classInherit.getName());

        assertEquals(1, classTable.find("foo"));
        classInherit = classTable.search("foo");

        assertEquals(EntryKind.INHERIT, classInherit.getEntryKind());
        assertEquals("foo", classInherit.getName());
    }

    @Test
    public void classHeaderSeveral() {
        SymbolTableEntry class1;
        SymbolTableEntry class2;

        List<Token> tokens = lexerDriver.getTokensFromInput("class test{}; class blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("test"));
        class1 = symbolTable.search("test");

        assertEquals("test", class1.getName());
        assertEquals(EntryKind.CLASS, class1.getEntryKind());

        assertEquals(1, symbolTable.find("blob"));
        class2 = symbolTable.search("blob");

        assertEquals("blob", class2.getName());
        assertEquals(EntryKind.CLASS, class2.getEntryKind());
    }

    @Test
    public void functionDefHeader() {
        SymbolTableEntry func;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer test(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("test"));
        func = symbolTable.search("test");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("test", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
    }

    @Test
    public void classFunctionDefHeader() {
        SymbolTable classTable;
        SymbolTable funcTable;
        SymbolTableEntry func;

        List<Token> tokens = lexerDriver.getTokensFromInput("class foo{ integer blob();}; integer foo::blob(){};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        classTable = symbolTable.search("foo").getLink();

        assertEquals(0, classTable.find("blob"));
        func = classTable.search("blob");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("blob", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("blob", func.getLink().getName());
        funcTable = func.getLink();

        assertEquals(0, funcTable.find("foo"));
        func = funcTable.search("foo");
        assertEquals(EntryKind.INHERIT, func.getEntryKind());
        assertEquals("foo", func.getName());

    }

    @Test
    public void classFunctionInvalidClassDefHeader() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{ integer blob();}; integer foo::blob(){};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classFunctionInvalidFunctionDefHeader() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class foo{ integer blob();}; integer foo::bar(){};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void functionDefHeaderParameter() {
        SymbolTableEntry func;
        SymbolTable funcTable;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(bar varName){};  main {};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        func = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("foo", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("foo", func.getLink().getName());

        funcTable = func.getLink();
        assertEquals(0, funcTable.find("varName"));
        param = funcTable.search("varName");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("varName", param.getName());
        assertEquals("bar", param.getEntryType().getElementType().getType());
    }

    @Test
    public void functionDefHeaderParameterArray() {
        SymbolTableEntry func;
        SymbolTable funcTable;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(bar varName[1]){};  main {};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        func = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("foo", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("foo", func.getLink().getName());

        funcTable = func.getLink();
        assertEquals(0, funcTable.find("varName"));
        param = funcTable.search("varName");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("varName", param.getName());
        assertEquals("bar", param.getEntryType().getElementType().getType());
        assertTrue(param.getEntryType().getElementType().isArray());
        assertEquals(1, param.getEntryType().getElementType().getNumDimensions());
        assertEquals("1", param.getEntryType().getElementType().getSingleDimension(0));
    }

    @Test
    public void functionDefHeaderParameterArraySeveralDimension() {
        SymbolTableEntry func;
        SymbolTable funcTable;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(bar varName[1][2]){};  main {};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        func = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("foo", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("foo", func.getLink().getName());

        funcTable = func.getLink();
        assertEquals(0, funcTable.find("varName"));
        param = funcTable.search("varName");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("varName", param.getName());
        assertEquals("bar", param.getEntryType().getElementType().getType());
        assertTrue(param.getEntryType().getElementType().isArray());
        assertEquals(2, param.getEntryType().getElementType().getNumDimensions());
        assertEquals("1", param.getEntryType().getElementType().getSingleDimension(0));
        assertEquals("2", param.getEntryType().getElementType().getSingleDimension(1));
    }

    @Test
    public void functionDefHeaderParameterSeveral() {
        SymbolTableEntry func;
        SymbolTable funcTable;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(bar varName, blob var2){};  main {};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        func = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("foo", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        assertEquals("foo", func.getLink().getName());

        funcTable = func.getLink();
        assertEquals(0, funcTable.find("varName"));
        param = funcTable.search("varName");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("varName", param.getName());
        assertEquals("bar", param.getEntryType().getElementType().getType());

        assertEquals(1, funcTable.find("var2"));
        param = funcTable.search("var2");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("var2", param.getName());
        assertEquals("blob", param.getEntryType().getElementType().getType());
    }

    @Test
    public void functionDefHeaderParameterTailArray() {
        SymbolTableEntry func;
        SymbolTable funcTable;
        SymbolTableEntry param;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(bar varName, blob var2[5]){};  main {};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        func = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("foo", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());
        //first parameter type
        assertEquals("bar",func.getEntryType().getParameterTypes().get(0).getType());
        assertFalse(func.getEntryType().getParameterTypes().get(0).isArray());
        //second parameter type
        assertEquals("blob",func.getEntryType().getParameterTypes().get(1).getType());
        assertTrue(func.getEntryType().getParameterTypes().get(1).isArray());

        assertEquals("foo", func.getLink().getName());

        funcTable = func.getLink();
        assertEquals(0, funcTable.find("varName"));
        param = funcTable.search("varName");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("varName", param.getName());
        assertEquals("bar", param.getEntryType().getElementType().getType());

        assertEquals(1, funcTable.find("var2"));
        param = funcTable.search("var2");

        assertEquals(EntryKind.PARAMETER, param.getEntryKind());
        assertEquals("var2", param.getName());
        assertEquals("blob", param.getEntryType().getElementType().getType());
        assertTrue(param.getEntryType().getElementType().isArray());
        assertEquals(1, param.getEntryType().getElementType().getNumDimensions());
        assertEquals("5", param.getEntryType().getElementType().getSingleDimension(0));
    }

    @Test
    public void functionDefHeaderSeveral() {
        SymbolTableEntry func;
        SymbolTableEntry func2;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer test(){}; Foo blob(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();

        assertEquals(0, symbolTable.find("test"));
        func = symbolTable.search("test");

        assertEquals(EntryKind.FUNCTION, func.getEntryKind());
        assertEquals("test", func.getName());
        assertEquals("integer", func.getEntryType().getElementType().getType());

        assertEquals(1, symbolTable.find("blob"));
        func2 = symbolTable.search("blob");

        assertEquals(EntryKind.FUNCTION, func2.getEntryKind());
        assertEquals("blob", func2.getName());
        assertEquals("Foo", func2.getEntryType().getElementType().getType());
    }

    @Test
    public void funcBodyVar() {
        SymbolTable bar;
        SymbolTableEntry foo;

        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(){integer id;};main {  };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("foo"));
        foo = symbolTable.search("foo");

        assertEquals(EntryKind.FUNCTION, foo.getEntryKind());
        assertEquals("foo", foo.getName());

        bar = foo.getLink();
        assertEquals(0, bar.find("id"));
        foo = bar.search("id");

        assertEquals("id", foo.getName());
        assertEquals(EntryKind.VARIABLE, foo.getEntryKind());
        assertEquals("integer", foo.getEntryType().getElementType().getType());
    }

    @Test
    public void mainVar() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer blob; };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("blob"));
        main = bar.search("blob");

        assertEquals("blob", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("integer", main.getEntryType().getElementType().getType());
    }

    @Test
    public void mainVarFor() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  for(integer id = 0; id < 5 ; id = id + 1) {read(id);};};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("id"));
        main = bar.search("id");

        assertEquals("id", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("integer", main.getEntryType().getElementType().getType());
    }

    @Test
    public void mainSeveralVarId() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  Blob blob; Foo foo;};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("blob"));
        main = bar.search("blob");

        assertEquals("blob", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("Blob", main.getEntryType().getElementType().getType());

        assertEquals(1, bar.find("foo"));
        main = bar.search("foo");

        assertEquals("foo", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("Foo", main.getEntryType().getElementType().getType());
    }

    @Test
    public void mainVarId() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  Blob blob; };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("blob"));
        main = bar.search("blob");

        assertEquals("blob", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("Blob", main.getEntryType().getElementType().getType());
    }

    @Test
    public void mainVarIArray() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  Blob blob[9]; };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("blob"));
        main = bar.search("blob");

        assertEquals("blob", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("Blob", main.getEntryType().getElementType().getType());
        assertTrue(main.getEntryType().getElementType().isArray());
        assertEquals(1, main.getEntryType().getElementType().getNumDimensions());
        assertEquals("9", main.getEntryType().getElementType().getSingleDimension(0));
    }

    @Test
    public void mainSeveralVar() {
        SymbolTable bar;
        SymbolTableEntry main;

        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer blob; float bar;};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertEquals(0, symbolTable.find("main"));
        main = symbolTable.search("main");

        assertEquals(EntryKind.FUNCTION, main.getEntryKind());
        assertEquals("main", main.getName());

        bar = main.getLink();
        assertEquals(0, bar.find("blob"));
        main = bar.search("blob");

        assertEquals("blob", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("integer", main.getEntryType().getElementType().getType());

        assertEquals(1, bar.find("bar"));
        main = bar.search("bar");

        assertEquals("bar", main.getName());
        assertEquals(EntryKind.VARIABLE, main.getEntryKind());
        assertEquals("float", main.getEntryType().getElementType().getType());
    }
    /*************************************
     4.2.9 Wrong function declaration
     *************************************/
    @Test
    public void classFunctionNotDeclared(){
        List<Token> tokens = lexerDriver.getTokensFromInput("class Foo {}; integer Foo::calc(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertTrue(semanticPhases.hasError());
    }
    /*************************************
     4.2.12 Multiple declared variable
     *************************************/
    @Test
    public void varDeclaredTwice(){
        List<Token> tokens = lexerDriver.getTokensFromInput("main\n" +
                "{\n" +
                "    integer test;\n" +
                "    integer test;\n" +
                "};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertTrue(semanticPhases.hasError());
    }
    @Test
    public void varDeclaredDiffTypeTwice(){
        List<Token> tokens = lexerDriver.getTokensFromInput("main\n" +
                "{\n" +
                "    integer test;\n" +
                "    float test;\n" +
                "};");
        parserDriver.start(tokens);

        semanticPhases.creation((ProgASTNode) parserDriver.getAST());
        symbolTable = semanticPhases.getSymbolTable();
        assertTrue(semanticPhases.hasError());
    }
}