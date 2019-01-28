package lexer;

public class TokenError extends Token {
    private String errorMessage;
    public TokenError(String lexeme, int lineNumber, String errorMessage) {
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
