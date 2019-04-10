package codeGeneration;

import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import utils.FileIOUtils;

public class CodeGenerationDriver {
    private String codeGenerationOutputLocation = "src/output/codeGenerationOutput.txt";
    private String outputLocation = "src/moon/temp.m";

    public void start(ProgASTNode progASTNode) {
        CodeGenerationVisitor codeGenerationVisitor = new CodeGenerationVisitor();
        codeGenerationVisitor.visit(progASTNode);
        FileIOUtils.writeOutput(codeGenerationVisitor.getMoonCode(), codeGenerationOutputLocation);
        FileIOUtils.writeOutput(codeGenerationVisitor.getMoonCode(), outputLocation);
    }
}
