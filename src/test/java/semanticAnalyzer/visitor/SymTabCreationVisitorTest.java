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
        assertEquals("integer", func.getEntryType().getElementType());
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
        assertEquals("integer", func.getEntryType().getElementType());

        assertEquals(1, symbolTable.find("blob"));
        func2 = symbolTable.search("blob");

        assertEquals(EntryKind.FUNCTION, func2.getEntryKind());
        assertEquals("blob", func2.getName());
        assertEquals("Foo", func2.getEntryType().getElementType());
    }

}