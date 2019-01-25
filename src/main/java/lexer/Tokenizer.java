package lexer;

import com.sun.deploy.util.StringUtils;

public class Tokenizer {
    private String currentLexeme;
    private int currentLine;
    private String input;
    private int inputPosition;
    private boolean endOfInput;

    public Tokenizer(String input) {
        this.input = input;
        this.currentLexeme = "";
        this.currentLine = 0;
        this.inputPosition = -1;
        this.endOfInput = false;
    }

    public Character nextChar() {
        //String index starts at 0, no next at input.length()
        if(!isEndOfInput()) {
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

    private Token getNumericToken(char firstDigit) {
        //TODO Implement according to NUMERIC DFA
        return  null;
    }

    private Token getAlphaToken(char firstChar) {
        char current = firstChar;
        while (LexerMatcher.isAlphaNum(current) && !isEndOfInput()){
            currentLexeme += current;
            current = nextChar();
        }

        //Reached end of string, last char valid
        if(LexerMatcher.isAlphaNum(current)) {
            currentLexeme += current;
        }
        else {
            //not alphaNum, backup for further tonkenizing
            backupChar();
        }
        //TODO Implement Reserved Word check
        //check if reserved word (all alpha and lowercase)
        if(LexerMatcher.isAlpaha(currentLexeme) && currentLexeme == currentLexeme.toLowerCase())
            return null;
        else
            return createToken(Token.TokenType.ID);
    }

    public Token nextToken() {
        Character current = nextChar();
        char charValue;
        if(current != null) {
            charValue = current.charValue();
            if(LexerMatcher.isNumeric(charValue)){
                return getNumericToken(charValue);
            }
            if(LexerMatcher.isAlpaha(charValue)) {
                return getAlphaToken(charValue);
            }
            switch(charValue){
                case '.':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.POIN);
                case ',':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.COMM);
                //skip unrecognized characters
                case '\n':
                case '\r':
                    //new line started in input
                    currentLexeme = "";
                    this.currentLine++;
                    return nextToken();
                default:
                    //skip over character
                    currentLexeme = "";
                    return nextToken();
            }
        }
        //No tokens left in string
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

    public boolean isEndOfInput() {
        //strin index starts at 0, last character at input.length()-1
        endOfInput = input.length()-1 <= inputPosition;
        return endOfInput;
    }

}
