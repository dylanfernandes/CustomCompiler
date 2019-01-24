package lexer;

public class Tokenizer {
    private Character backupChar;
    private String currentLexeme;
    private int currentLine;
    private String input;
    private int inputPosition;

    public Tokenizer(String input) {
        this.input = input;
        this.currentLexeme = "";
        this.backupChar = null;
        this.currentLine = 0;
        this.inputPosition = -1;
    }

    public Character nextChar()
    {
        if(input.length()-1 > inputPosition)
        {
            if(inputPosition > 0)
            {
                backupChar = input.charAt(inputPosition);
            }
            inputPosition++;
            return input.charAt(inputPosition);
        }
        return null;
    }
    public Character getBackupChar() {
        return backupChar;
    }

    public void setBackupChar(Character backupChar) {
        this.backupChar = backupChar;
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
}
