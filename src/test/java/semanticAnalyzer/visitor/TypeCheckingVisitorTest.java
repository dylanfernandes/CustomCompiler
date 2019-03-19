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
}