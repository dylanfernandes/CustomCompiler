package semanticAnalyzer;

import semanticAnalyzer.visitor.SymTabCreationVisitor;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class SemanticPhases {
    public void creation(ProgASTNode progASTNode) {
        SymTabCreationVisitor symTabCreationVisitor = new SymTabCreationVisitor();
        symTabCreationVisitor.visit(progASTNode);
    }
}
