package lexer;

public class Tokenizer {
    public enum Token
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
}
