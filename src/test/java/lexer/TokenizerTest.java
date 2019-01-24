package lexer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TokenizerTest {

    @Test
    public void TokenTest () {
        Token tIf = new Token(Token.TokenType.IF, "if", 0);
        assertEquals("IF", tIf.getType().toString());
        //change type
        tIf.setType(Token.TokenType.ELSE);
        assertEquals("ELSE", tIf.getType().toString());
        assertFalse("IF" == tIf.getType().toString());
    }
}
