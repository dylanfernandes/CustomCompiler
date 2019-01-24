package lexer;

public class Tokenizer {
    private String backupChar;
    private String currentLexeme;
    private int currentLine;
    private String input;

    public Tokenizer(String input) {
        this.input = input;
        this.currentLexeme = "";
        this.backupChar = "";
        this.currentLine = 0;
    }

    public String getBackupChar() {
        return backupChar;
    }

    public void setBackupChar(String backupChar) {
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
