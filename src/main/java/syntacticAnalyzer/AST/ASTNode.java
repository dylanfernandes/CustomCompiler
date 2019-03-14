package syntacticAnalyzer.AST;

import lexer.Token;

abstract public class ASTNode {
    private ASTNode parent;
    private ASTNode rightSibling;
    private ASTNode firstSibling;
    private ASTNode firstChild;

    abstract public String getValue();

    public ASTNode( ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        this.parent = parent;
        this.rightSibling = rightSibling;
        this.firstSibling = firstSibling;
        this.firstChild = firstChild;
    }

    public ASTNode() {
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
        if(firstSibling == null) {
            sibling.firstSibling = this;
        } else {
            sibling.firstSibling = this.firstSibling;
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
            if(lastChild != null) {
                lastChild.makeSiblings(child);
            }
            lastChild = child;
        }
    }

    public String print() {
        String printedTree = "";
        ASTNode temp;
        if(this.firstChild != null){
            printedTree += this.getValue();
            temp = firstChild;
            printedTree += " -> ";
            do {
                printedTree += temp.getValue() + " ";
                temp = temp.getRightSibling();
            }
            while (temp != null);
            printedTree += "\n";
            printedTree += this.firstChild.print();
        }
        if(this.rightSibling != null) {
            printedTree += this.rightSibling.print();
        }

        return printedTree;
    }
}

