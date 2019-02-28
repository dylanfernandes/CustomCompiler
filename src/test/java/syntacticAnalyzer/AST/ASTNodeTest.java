package syntacticAnalyzer.AST;

import lexer.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class ASTNodeTest {

    @Test
    public void adoptChildren() {
        Token r1 = new Token(Token.TokenType.IF, "IF", 1);
        Token t1 = new Token(Token.TokenType.ID, "TEST", 1);
        Token t2 = new Token(Token.TokenType.INT, "123", 1);
        ASTNode root = new ASTNode(r1);
        ASTNode sibling1 = new ASTNode(t1);
        ASTNode sibling2 = new ASTNode(t2);

        sibling1.makeSiblings(sibling2);
        root.adoptChildren(sibling1);

        assertEquals(root, sibling1.getParent());
        assertEquals(root, sibling2.getParent());

        assertEquals(sibling1, root.getFirstChild());

        assertEquals(null, sibling1.getFirstChild());
        assertEquals(sibling1, sibling2.getFirstChild());
        assertEquals(sibling2, sibling1.getRightSibling());
    }

    @Test
    public void makeSiblings() {
        Token t1 = new Token(Token.TokenType.ID, "TEST", 1);
        Token t2 = new Token(Token.TokenType.INT, "123", 1);
        ASTNode sibling1 = new ASTNode(t1);
        ASTNode sibling2 = new ASTNode(t2);

        sibling1.makeSiblings(sibling2);
        assertEquals(null, sibling1.getFirstChild());
        assertEquals(sibling1, sibling2.getFirstChild());
        assertEquals(sibling2, sibling1.getRightSibling());
    }

    @Test
    public void makeFamily() {
        Token r1 = new Token(Token.TokenType.IF, "IF", 1);
        Token t1 = new Token(Token.TokenType.ID, "TEST", 1);
        Token t2 = new Token(Token.TokenType.INT, "123", 1);
        ASTNode root = new ASTNode(r1);
        ASTNode sibling1 = new ASTNode(t1);
        ASTNode sibling2 = new ASTNode(t2);

        root.makeFamily(sibling1, sibling2);

        assertEquals(root, sibling1.getParent());
        assertEquals(root, sibling2.getParent());

        assertEquals(sibling1, root.getFirstChild());

        assertEquals(null, sibling1.getFirstChild());
        assertEquals(sibling1, sibling2.getFirstChild());
        assertEquals(sibling2, sibling1.getRightSibling());
    }
}