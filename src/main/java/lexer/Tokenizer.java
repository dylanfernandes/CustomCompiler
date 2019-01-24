package lexer;

public class Tokenizer {
    private String currentLexeme;
    private int currentLine;
    private String input;
    private int inputPosition;

    public Tokenizer(String input) {
        this.input = input;
        this.currentLexeme = "";
        this.currentLine = 0;
        this.inputPosition = -1;
    }

    public Character nextChar() {
        if(input.length()-1 > inputPosition) {
            inputPosition++;
            return input.charAt(inputPosition);
        }
        return null;
    }

    public boolean backupChar() {
        if(inputPosition >= 0)
        {
            inputPosition--;
            return true;
        }
        return false;
    }

    public Token nextToken() {
        Character current = nextChar();
        if(current != null) {
            switch(current.charValue()){
                case '.':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.POIN);
                case ',':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.COMM);

                default:
                    return null;
            }
        }
        else
            return null;
    }

    public Token createToken(Token.TokenType tokenType){
        Token current =  new Token(tokenType, this.currentLexeme, this.currentLine);
        currentLexeme = "";
        return  current;
    }

    public String getCurrentLexeme() {
        return currentLexeme;
    }

    public void setCurrentLexeme(String currentLexeme) {
        this.currentLexeme = currentLexeme;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int getInputPosition() {
        return inputPosition;
    }

    public void setInputPosition(int inputPosition) {
        this.inputPosition = inputPosition;
    }
}
