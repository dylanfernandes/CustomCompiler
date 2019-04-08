package codeGeneration;

import lexer.LexerDriver;
import lexer.Token;
import org.junit.Before;
import org.junit.Test;
import semanticAnalyzer.SemanticPhases;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import syntacticAnalyzer.ParserDriver;

import java.util.List;

import static org.junit.Assert.*;

public class CodeGenerationVisitorTest {
    ParserDriver parserDriver;

    LexerDriver lexerDriver;

    SemanticPhases semanticPhases;

    CodeGenerationVisitor codeGenerationVisitor;

    @Before
    public void setup() {
        lexerDriver = new LexerDriver();
        parserDriver = new ParserDriver();
        semanticPhases = new SemanticPhases();
        codeGenerationVisitor = new CodeGenerationVisitor();
    }

    @Test
    public void integerMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer blob; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 4", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void integerSeveralMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer blob; integer bar;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        String[] moonLines = codeGenerationVisitor.getMoonCode().split("\n");

        assertEquals(2, moonLines.length);
        assertEquals("blob res 4", moonLines[0]);
        assertEquals("bar res 4", moonLines[1]);
    }

    @Test
    public void floatMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { float blob; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 8", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void floatSeveralMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { float blob; float bar;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        String[] moonLines = codeGenerationVisitor.getMoonCode().split("\n");

        assertEquals(2, moonLines.length);
        assertEquals("blob res 8", moonLines[0]);
        assertEquals("bar res 8", moonLines[1]);
    }

    @Test
    public void severalBasicTypeMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer blob; float bar;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        String[] moonLines = codeGenerationVisitor.getMoonCode().split("\n");

        assertEquals(2, moonLines.length);
        assertEquals("blob res 4", moonLines[0]);
        assertEquals("bar res 8", moonLines[1]);
    }

    @Test
    public void integerArrayMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer blob[2]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 8", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void integerArrayLargeMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { integer blob[12]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 48", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void floatArrayMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { float blob[5]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 40", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void floatArrayLargeMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { float blob[15]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 120", codeGenerationVisitor.getMoonCode().trim());
    }

}