package syntacticAnalyzer.AST;

import lexer.Token;

public class ASTNode {
    private ASTNode parent;
    private ASTNode rightSibling;
    private ASTNode firstSibling;
    private ASTNode firstChild;
    private Token value;

    public ASTNode(Token value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        this.value = value;
        this.parent = parent;
        this.rightSibling = rightSibling;
        this.firstSibling = firstSibling;
        this.firstChild = firstChild;
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

    public ASTNode getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(ASTNode firstChild) {
        this.firstChild = firstChild;
    }

    public void adoptChildren(ASTNode childrenHead) {
        this.firstChild = childrenHead;
        childrenHead.parent = this;

        ASTNode child = childrenHead.getRightSibling();
        while (child != null) {
            child.setParent(this);
            child = child.getRightSibling();
        }
    }

    public void makeSiblings(ASTNode sibling) {
        if(firstChild == null) {
            sibling.firstChild = this;
        } else {
            sibling.firstChild = this.firstChild;
        }
        this.rightSibling = sibling;
    }

    public void makeFamily(ASTNode... children){
        ASTNode lastChild = null;
        for (ASTNode child: children) {
            if(this.firstChild == null) {
                this.firstChild = child;
            }
            child.setParent(this);
            if(lastChild == null) {
                lastChild = child;
            } else {
                lastChild.makeSiblings(child);
            }
        }
    }
}

