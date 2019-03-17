package syntacticAnalyzer.AST;

import lexer.Token;
import semanticAnalyzer.visitor.Visitor;

public class StringASTNode extends BasicASTNode{

    public StringASTNode(String value, ASTNode parent, ASTNode rightSibling, ASTNode firstSibling, ASTNode firstChild) {
        super(value, parent, rightSibling, firstSibling, firstChild);
    }

    public StringASTNode(String value) {
        super(value);
    }

    public void accept(Visitor visitor) {
        visitor.visit( this);
    }
}
