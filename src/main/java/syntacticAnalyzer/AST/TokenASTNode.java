package syntacticAnalyzer.AST;

import lexer.Token;

public class TokenASTNode extends  ASTNode{

    private Token value;

    public TokenASTNode(ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild, Token value) {
        super(parent, rightSibling, firstSibling, firstChild);
        this.value = value;
    }

    public TokenASTNode(Token value) {
        super();
        this.value = value;
    }

    public Token getToken() {
        return value;
    }

    public String getValue() {
        return value.getType().toString();
    }
}
