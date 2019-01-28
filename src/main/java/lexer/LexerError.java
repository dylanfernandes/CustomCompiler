package lexer;

public class LexerError extends Token {
    private String errorMessage;
    public LexerError( String lexeme, int lineNumber, String errorMessage) {
        super(TokenType.ERROR, lexeme, lineNumber);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage + ":" + getLineNumber();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
