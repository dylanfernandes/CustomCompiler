package syntacticAnalyzer.AST;

import semanticAnalyzer.visitor.Visitor;

public abstract class BasicASTNode extends ASTNode{
    private String value;

    public BasicASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(parent, rightSibling, firstSibling, firstChild);
        this.value = value;
    }

    public BasicASTNode(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
