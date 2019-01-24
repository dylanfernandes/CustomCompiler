package lexer;

public class Token {

    public enum TokenType
    {
        ID,
        INT, FLO,
        CMT,
        ERROR,
        POIN, COMM, SEMI, COLO,
        ADD, SUB, MULT, DIV,
        ASSGN,GRE, LES,
        EQ, NEQ, LESSEQ, GREEQ,
        AND, OR,
        OPAR, CPAR,
        OBRA, CBRA,
        OSBRA, CSBRA,
        DCOLO,
        IF, THEN, ELSE, FOR, RETURN,
        CLASS, MAIN,
        INTEGER, FLOAT,
        READ, WRITE
    };

    private TokenType type;

    public Token(TokenType tokenType){
        this.type = tokenType;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}