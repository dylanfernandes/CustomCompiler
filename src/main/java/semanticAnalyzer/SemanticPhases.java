package semanticAnalyzer;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.SymTabCreationVisitor;
import semanticAnalyzer.visitor.TypeCheckingVisitor;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SemanticPhases {
    private SymbolTable symbolTable;
    private String errorOutput;
    private  boolean hasError;

    public void startPhases(ProgASTNode progASTNode) {
        creation(progASTNode);
        errorOutput = "";
        hasError = false;
        typeChecking(progASTNode);
    }
    public void creation(ProgASTNode progASTNode) {
        SymTabCreationVisitor symTabCreationVisitor = new SymTabCreationVisitor();
        symTabCreationVisitor.visit(progASTNode);
        symbolTable = progASTNode.getGlobalTable();
    }

    private void typeChecking(ProgASTNode progASTNode) {
        TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor();
        typeCheckingVisitor.visit(progASTNode);
        errorOutput = typeCheckingVisitor.getErrorOutput();
        hasError = typeCheckingVisitor.hasError();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public boolean hasError() {
        return hasError;
    }
}
