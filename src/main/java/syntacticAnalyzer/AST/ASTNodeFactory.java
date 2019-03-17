package syntacticAnalyzer.AST;

import lexer.Token;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;

public class ASTNodeFactory {

    public static ASTNode getASTNode(String value) {
        return new StringASTNode(value);
    }

    public static ASTNode getASTNode(Token value) {
        return new TokenASTNode(value);
    }
}
