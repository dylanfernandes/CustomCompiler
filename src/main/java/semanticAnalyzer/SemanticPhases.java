package semanticAnalyzer;

import semanticAnalyzer.SymbolTable.SymbolTable;
import semanticAnalyzer.visitor.SymTabCreationVisitor;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SemanticPhases {
    private SymbolTable symbolTable;

    public void creation(ProgASTNode progASTNode) {
        SymTabCreationVisitor symTabCreationVisitor = new SymTabCreationVisitor();
        symTabCreationVisitor.visit(progASTNode);
        symbolTable = progASTNode.getGlobalTable();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
