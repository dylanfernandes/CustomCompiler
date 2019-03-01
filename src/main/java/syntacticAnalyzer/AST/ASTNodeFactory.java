package syntacticAnalyzer.AST;

import lexer.Token;

public class ASTNodeFactory {

    public static ASTNode getASTNode(String value) {
        return new StringASTNode(value);
    }

    public static ASTNode getASTNode(Token value) {
        return new TokenASTNode(value);
    }
}
