package syntacticAnalyzer.AST.semanticNodes;

import semanticAnalyzer.visitor.Visitor;
import syntacticAnalyzer.AST.ASTNode;

public class ProgASTNode extends ASTNode {
    private String value;
    public String getValue() {
        return null;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
