package semanticAnalyzer;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.SymTabCreationVisitor;
import semanticAnalyzer.visitor.TypeCheckingVisitor;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SemanticPhases {
    private SymbolTable symbolTable;

    public void StartPhases(ProgASTNode progASTNode) {
        creation(progASTNode);
    }
    public void creation(ProgASTNode progASTNode) {
        SymTabCreationVisitor symTabCreationVisitor = new SymTabCreationVisitor();
        symTabCreationVisitor.visit(progASTNode);
        symbolTable = progASTNode.getGlobalTable();
        typeChecking(progASTNode);
    }

    private void typeChecking(ProgASTNode progASTNode) {
        TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor();
        typeCheckingVisitor.visit(progASTNode);

    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
