package lexer.tokens;

import lexer.Token;
import lexer.Tokenizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleTokenTest {

    @Test
    public void NullTest (){
        Tokenizer tokenizer = new Tokenizer("");
        Token token = tokenizer.nextToken();
        assertEquals(null, token);
    }

    @Test
    public void PointTest (){
        Tokenizer tokenizer = new Tokenizer(".");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.POIN, token.getType());
        assertEquals(".", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void CommaTest (){
        Tokenizer tokenizer = new Tokenizer(",");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.COMM, token.getType());
        assertEquals(",", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void DivTest(){
        Tokenizer tokenizer = new Tokenizer("/");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DIV, token.getType());
        assertEquals("/", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void MultTest(){
        Tokenizer tokenizer = new Tokenizer("*");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.MULT, token.getType());
        assertEquals("*", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void AddTest(){
        Tokenizer tokenizer = new Tokenizer("+");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ADD, token.getType());
        assertEquals("+", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void SubTest(){
        Tokenizer tokenizer = new Tokenizer("-");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.SUB, token.getType());
        assertEquals("-", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void NotTest(){
        Tokenizer tokenizer = new Tokenizer("!");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NOT, token.getType());
        assertEquals("!", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void SemiTest(){
        Tokenizer tokenizer = new Tokenizer(";");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.SEMI, token.getType());
        assertEquals(";", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void OPARTest(){
        Tokenizer tokenizer = new Tokenizer("(");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OPAR, token.getType());
        assertEquals("(", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void CPARTest(){
        Tokenizer tokenizer = new Tokenizer(")");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CPAR, token.getType());
        assertEquals(")", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void OBRATest(){
        Tokenizer tokenizer = new Tokenizer("{");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OBRA, token.getType());
        assertEquals("{", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void CBRATest(){
        Tokenizer tokenizer = new Tokenizer("}");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CBRA, token.getType());
        assertEquals("}", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void OSBRATest(){
        Tokenizer tokenizer = new Tokenizer("[");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OSBRA, token.getType());
        assertEquals("[", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void CSBRATest(){
        Tokenizer tokenizer = new Tokenizer("]");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.CSBRA, token.getType());
        assertEquals("]", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void AssignTest(){
        Tokenizer tokenizer = new Tokenizer("=");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.ASSGN, token.getType());
        assertEquals("=", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void EqTest(){
        Tokenizer tokenizer = new Tokenizer("==");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.EQ, token.getType());
        assertEquals("==", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void OrTest(){
        Tokenizer tokenizer = new Tokenizer("||");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.OR, token.getType());
        assertEquals("||", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void AndTest(){
        Tokenizer tokenizer = new Tokenizer("&&");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.AND, token.getType());
        assertEquals("&&", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void GREThanTest(){
        Tokenizer tokenizer = new Tokenizer(">");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.GRE, token.getType());
        assertEquals(">", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void GREEQThanTest(){
        Tokenizer tokenizer = new Tokenizer(">=");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.GREEQ, token.getType());
        assertEquals(">=", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void ColonThanTest(){
        Tokenizer tokenizer = new Tokenizer(":");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.COLO, token.getType());
        assertEquals(":", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void DColonThanTest(){
        Tokenizer tokenizer = new Tokenizer("::");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.DCOLO, token.getType());
        assertEquals("::", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void LESThanTest(){
        Tokenizer tokenizer = new Tokenizer("<");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.LES, token.getType());
        assertEquals("<", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void LESSEQThanTest(){
        Tokenizer tokenizer = new Tokenizer("<=");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.LESSEQ, token.getType());
        assertEquals("<=", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    public void NEQThanTest(){
        Tokenizer tokenizer = new Tokenizer("<>");
        Token token = tokenizer.nextToken();
        assertEquals(Token.TokenType.NEQ, token.getType());
        assertEquals("<>", token.getLexeme());
        assertEquals(0, token.getLineNumber());
    }
}
