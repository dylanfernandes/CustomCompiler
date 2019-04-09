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

    /*************************************
        5.1.1 Basic Types
     *************************************/
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

    /*************************************
     5.1.2 Array Basic Types
     *************************************/
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
    @Test
    public void multiArrayMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("main { float blob[5][4]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 160", codeGenerationVisitor.getMoonCode().trim());
    }


    /*************************************
     5.1.3 Object Basic Types
     *************************************/
    @Test
    public void objectMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var;}; main { Blob blob; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 4", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void objectManyVarsMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var; float foo; integer temp;}; main { Blob blob; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 16", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void objectBasicArrayMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var[3];}; main { Blob blob; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 12", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void severalObjectMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var;}; class Bar{float temp;}; main { Blob blob; Bar bar;};");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        String[] moonLines = codeGenerationVisitor.getMoonCode().split("\n");

        assertEquals(2, moonLines.length);
        assertEquals("blob res 4", moonLines[0]);
        assertEquals("bar res 8", moonLines[1]);
    }

    /*************************************
     5.1.6 Object Array Types
     *************************************/
    @Test
    public void objectArrayMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var;}; main { Blob blob[2]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 8", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void objectArraySeveralVarsMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var; float temp;}; main { Blob blob[2]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 24", codeGenerationVisitor.getMoonCode().trim());
    }

    @Test
    public void objectMultiArraySeveralVarsMemoryAllocation() {
        List<Token> tokens = lexerDriver.getTokensFromInput("class Blob{integer var; float temp;}; main { Blob blob[2][4]; };");
        parserDriver.start(tokens);

        semanticPhases.startPhases((ProgASTNode) parserDriver.getAST());
        codeGenerationVisitor.visit((ProgASTNode) parserDriver.getAST());

        assertEquals("blob res 96", codeGenerationVisitor.getMoonCode().trim());
    }
}