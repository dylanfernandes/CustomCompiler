package lexer;

public class Token {

    public enum TokenType
    {
        ID,
        INT, FLO,
        CMT,
        ERROR, NEWLINE,
        POIN, COMM, SEMI, COLO,
        ADD, SUB, MULT, DIV, NOT,
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
        READ, WRITE,
        EPSILON
    };

    private TokenType type;
    private int lineNumber;
    private String lexeme;

    public Token() {}

    public Token(TokenType tokenType, String lexeme, int lineNumber){
        this.type = tokenType;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public void setToken(Token token){
        setType(token.getType());
        setLineNumber(token.getLineNumber());
        setLexeme(token.getLexeme());
    }
}