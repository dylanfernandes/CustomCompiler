package syntacticAnalyzer.AST;

import lexer.Token;

public class ASTNode {
    private ASTNode parent;
    private ASTNode rightSibling;
    private ASTNode firstSibling;
    private Token value;

    public ASTNode(Token value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling) {
        this.value = value;
        this.parent = parent;
        this.rightSibling = rightSibling;
        this.firstSibling = firstSibling;
    }

    public ASTNode(Token value) {
        this.value = value;
        this.parent = null;
        this.rightSibling = null;
        this.firstSibling = null;
    }

    public ASTNode getParent() {
        return parent;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public ASTNode getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(ASTNode rightSibling) {
        this.rightSibling = rightSibling;
    }

    public ASTNode getFirstSibling() {
        return firstSibling;
    }

    public void setFirstSibling(ASTNode firstSibling) {
        this.firstSibling = firstSibling;
    }

    public Token getValue() {
        return value;
    }

    public void setValue(Token value) {
        this.value = value;
    }
}
