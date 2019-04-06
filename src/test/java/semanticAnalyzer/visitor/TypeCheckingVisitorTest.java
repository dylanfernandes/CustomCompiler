package semanticAnalyzer.visitor;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import semanticAnalyzer.SemanticPhases;
import semanticAnalyzer.SymbolTable.EntryKind;
import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.SymbolTable.SymbolTableEntry;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import syntacticAnalyzer.ParserDriver;

import java.util.List;

import static org.junit.Assert.*;

public class TypeCheckingVisitorTest {
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
    public void classHeaderInheritDefinedBefore() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class blob{}; class test:blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classHeaderInheritDefinedAfter() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test:blob{}; class blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classHeaderInheritSeveralNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test:blob, bar{}; class blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classHeaderInheritNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test:blob{}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classVarNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{Blob var;}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classVarDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{}; class test{Blob var;}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classFuncNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer func();}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classFuncDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer func();}; integer test::func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classFuncVarDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer classVar; integer func();}; integer test::func(){classVar = 1;}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classFuncVarNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer classVar; integer func();}; integer test::func(){classVarTwo = 1;}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classFuncDefinedValidParamsClass() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{}; class test{integer func(bar test);}; integer test::func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classFuncDefinedInvalidParamsClass() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer func(bar test);}; integer test::func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classFuncDefinedSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{}; class test{integer func(); bar func2();}; integer test::func(){}; bar test::func2(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void classFuncNotDefinedSeveral() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer func(); bar func2();}; integer test::func(){}; bar test::func2(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void classFuncClassNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{integer func();}; integer blob::func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void funcClassNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class test{ }; bar func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void funcClassDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class bar{}; class test{ }; bar func(){}; main { };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main { bar var; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" class bar{}; main { bar var; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassForLoopNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  for(bar id = 0; id < 5 ; id = id + 1) {read(id);};};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassForLoopDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  for(integer id = 0; id < 5 ; id = id + 1) {read(id);};};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarNotDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  test = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarDefined() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  integer test; test = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarNoMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  integer test; test.temp = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarNoArrayMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  integer test; test[0]= 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarArrayMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  integer test[2]; test[0]= 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarArraySeveralMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" main {  integer test[2][3][8]; test[0][1][0]= 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassNoMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" class blob{}; main {  blob test; test.temp = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassMember() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" class blob{ integer temp; }; main {  blob test; test.temp = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarClassMemberNested() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" class Bar{ integer temp; }; class blob{ Bar bar; }; main {  blob test; test.bar.temp = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarNoClassMemberNested() {
        List<Token> tokens = lexerDriver.getTokensFromInput(" class Bar{ integer temp; }; class blob{ Bar bar; }; main {  blob test; test.bar.temp2 = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignBadType() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test; test = 1.5; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignGoodType() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test; test = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignBadTypeFloat() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  float test; test = 1; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignGoodTypeFloat() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  float test; test = 1.5; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarArrayIndiceValid() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test[2]; test[1] = 1;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarArrayIndiceInvalid() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test[2]; test[2.5] = 1;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignNotExist() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test;  test = val;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignExist() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test; integer val; test = val;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignExistBadType() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main {  integer test;  float val; test = val;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignFunctionExist() {
        List<Token> tokens = lexerDriver.getTokensFromInput("integer foo(){}; main { integer val; val = foo();};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertFalse(semanticPhases.hasError());

    }

    @Test
    public void mainVarAssignFunctionNotExist() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer val; val = foo();};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        assertTrue(semanticPhases.hasError());

    }


}