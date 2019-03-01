package syntacticAnalyzer.AST;

import lexer.Token;

public class StringASTNode extends ASTNode{

    private String value;

    public StringASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(parent, rightSibling, firstSibling, firstChild);
        this.value = value;
    }

    public StringASTNode(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
