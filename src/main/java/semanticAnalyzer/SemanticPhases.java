package semanticAnalyzer;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.SymTabCreationVisitor;
import semanticAnalyzer.visitor.TypeCheckingVisitor;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SemanticPhases {
    private String creationOutput;
    private String typeOutput;
    private  boolean hasError;
    private SymbolTable symbolTable;

    public SemanticPhases() {
        creationOutput = "";
        typeOutput = "";
        hasError = false;
    }

    public void startPhases(ProgASTNode progASTNode) {
        creation(progASTNode);
        if(!hasError)
            typeChecking(progASTNode);
        else
            typeOutput = "Error in creation phase";
    }
    public void creation(ProgASTNode progASTNode) {
        SymTabCreationVisitor symTabCreationVisitor = new SymTabCreationVisitor();
        symTabCreationVisitor.visit(progASTNode);
        symbolTable = progASTNode.getGlobalTable();
        creationOutput = symTabCreationVisitor.print();
        hasError = symTabCreationVisitor.hasError();
    }

    private void typeChecking(ProgASTNode progASTNode) {
        TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor();
        typeCheckingVisitor.visit(progASTNode);
        typeOutput = typeCheckingVisitor.getErrorOutput();
        hasError = typeCheckingVisitor.hasError();
    }

    public String getCreationOutput() {
        return creationOutput;
    }

    public String getTypeOutput() {
        return typeOutput;
    }

    public boolean hasError() {
        return hasError;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
