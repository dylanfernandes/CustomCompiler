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

}