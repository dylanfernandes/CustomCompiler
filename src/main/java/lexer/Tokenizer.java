package lexer;


import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private String currentLexeme;
    private int currentLine;
    private String input;
    private int inputPosition;
    private boolean endOfInput;
    private List<String> reservedWords;

    public Tokenizer(String input) {
        resetTokenizer(input);
        initializeReservedWords();
    }

    public void resetTokenizer(String input) {
        this.input = input;
        this.currentLexeme = "";
        this.currentLine = 0;
        this.inputPosition = -1;
        this.endOfInput = false;
    }

    private void initializeReservedWords() {
        reservedWords = new ArrayList<String>();
        this.reservedWords.add("if");
        this.reservedWords.add("then");
        this.reservedWords.add("else");
        this.reservedWords.add("for");
        this.reservedWords.add("class");
        this.reservedWords.add("integer");
        this.reservedWords.add("float");
        this.reservedWords.add("read");
        this.reservedWords.add("write");
        this.reservedWords.add("return");
        this.reservedWords.add("main");
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
        if(backup())
        {
            //update current lexeme
            if (currentLexeme != null && currentLexeme.length() > 0) {
                currentLexeme = currentLexeme.substring(0, currentLexeme.length() - 1);
            }
            return true;
        }
        return false;
    }

    public boolean backup() {
        if(inputPosition >= 0)
        {
            inputPosition--;
            return true;
        }
        return false;
    }

    private Token getNumericToken(char firstDigit) {
        Character current = firstDigit;
        Character afterInt;
        if(firstDigit != '0') {
            while(LexerMatcher.isNumeric(current) && !isEndOfInput()) {
                currentLexeme += current;
                current = nextChar();
            }
        }

        if (LexerMatcher.isNumeric(current)) {
            currentLexeme += current;
            afterInt = nextChar();
        }
        else
            afterInt = current;



        if (afterInt != null && afterInt == '.') {
            current = nextChar();
            if(current == null || !LexerMatcher.isNumeric(current)) {
                //int that ends before point
                backup();

            }
            else {
                return getFloat(current);
            }
        }
        else
            backup();
        return  createToken(Token.TokenType.INT);
    }

    private Token getFloat(char firstDigit) {
        //TODO Implement float according to NUMERIC DFA
        Character current = nextChar();
        currentLexeme += "." + firstDigit;
        if (firstDigit == '0') {
            if (current == null || !LexerMatcher.isNumeric(current)) {
                return createToken(Token.TokenType.FLO);
            }
        }

        while (current != null && LexerMatcher.isNumeric(current)){
            currentLexeme += current;
            current = nextChar();
        }
        //float with exponential notation
        if (current != null && current == 'e') {
            return getExponent();
        }
        else if (currentLexeme.charAt(currentLexeme.length()-1) == '0') {
            //remove 0 from float
            backupChar();
        }
        else {
            backup();
        }
        return createToken(Token.TokenType.FLO);

    }

    private Token getExponent() {
        Character current = nextChar();
        Character sign = null;

        if(current != null && (current == '+' || current == '-')) {
            sign = current;
            current = nextChar();
        }
        //character not part of float
        if (current == null  || !LexerMatcher.isNumeric(current)) {
            //extra backup for sign
            if (sign != null) {
                backup();
            }
            backup();
            return createToken(Token.TokenType.FLO);
        }

        currentLexeme += "e";

        if(sign != null) {
            currentLexeme += sign;
        }

        while (current != null && LexerMatcher.isNumeric(current)){
            currentLexeme += current;
            current = nextChar();
        }
        return createToken(Token.TokenType.FLO);
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
            backup();
        }

        if(isReservedWord()) {
            Token.TokenType reservedToken = Token.TokenType.valueOf(currentLexeme.toUpperCase());
            return createToken(reservedToken);
        }
        else
            return createToken(Token.TokenType.ID);
    }

    private Token getSingleLineComment() {
        Character next = nextChar();
        while (next != null && next != '\n' && next != '\r') {
            currentLexeme += next;
            next = nextChar();
        }
        return createToken(Token.TokenType.CMT);
    }

    private Token getMultiLineComment() {
        //remember where comment started in case not properly closed
        int startPos = inputPosition - 1;
        Character next = nextChar();
        Character temp;

        while (next != null) {
            //check for multiline comment termination
            if(next == '*') {
                temp = nextChar();
                if (temp != null) {
                    if (temp == '/') {
                        currentLexeme += next;
                        currentLexeme += temp;
                        return createToken(Token.TokenType.CMT);
                    } else
                        backup();
                }
            }
            currentLexeme += next;
            next = nextChar();
        }

        //Comment not properly closed
        //reset start position, first token is division symbol
        inputPosition = startPos;
        currentLexeme = "/";
        return createToken(Token.TokenType.DIV);
    }

    //Division or comment
    private Token getSlashToken(){
        currentLexeme += "/";
        if(isEndOfInput()){
            return createToken(Token.TokenType.DIV);
        }
        Character next = nextChar();
        switch (next) {
            case '/':
                currentLexeme += next;
                return getSingleLineComment();
            case '*':
                currentLexeme += next;
                return getMultiLineComment();
            default:
                return createToken(Token.TokenType.DIV);

        }
    }

    private boolean isReservedWord() {
        //check if reserved word (all alpha, all lowercase and in list of reserved words)
        return (LexerMatcher.isAlpaha(currentLexeme) && currentLexeme == currentLexeme.toLowerCase() &&reservedWords.contains(currentLexeme));
    }

    public Token nextToken() {
        Character current = nextChar();
        char charValue;
        if(current != null) {
            charValue = current.charValue();
            //digits
            if(LexerMatcher.isNumeric(charValue)){
                return getNumericToken(charValue);
            }
            //ID's or reserved words
            if(LexerMatcher.isAlpaha(charValue)) {
                return getAlphaToken(charValue);
            }

            switch(charValue){
                case '/':
                    return getSlashToken();
                case '.':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.POIN);
                case ',':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.COMM);
                case'*':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.MULT);
                case'+':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.ADD);
                case'-':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.SUB);
                case '!':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.NOT);
                case ';':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.SEMI);
                case '(':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.OPAR);
                case ')':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.CPAR);
                case '{':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.OBRA);
                case '}':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.CBRA);
                case '[':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.OSBRA);
                case ']':
                    currentLexeme = current.toString();
                    return createToken(Token.TokenType.CSBRA);

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
        //string index starts at 0, last character at input.length()-1
        endOfInput = input.length()-1 <= inputPosition;
        return endOfInput;
    }

}
