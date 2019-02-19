package lexer;

import utils.FileIOUtils;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class LexerDriver {

    private String inputLocation = "src/input/input.txt";
    private String outputLocation = "src/output/output.txt";
    private String AtoCCLocation = "src/output/AtoCCOutput.txt";
    private String errorLocation = "src/output/error.txt";

    public LexerDriver() {
    }

    public LexerDriver(String location) {
        inputLocation = location;
    }
    public void start(String input) throws FileNotFoundException {
        inputLocation = input;
        start();
    }

    public void start() throws FileNotFoundException {
        Tokenizer tokenizer = new Tokenizer(FileIOUtils.getInput(inputLocation));
        Token token;
        String output = "";
        String AtoCCOutput = "";
        while (!tokenizer.isEndOfInput()){
            token = tokenizer.nextToken();
            if(token.getType() == Token.TokenType.NEWLINE) {
                output += '\n';
                AtoCCOutput += '\n';
            }
            else {
                output += printTokenContent(token);
                AtoCCOutput += printAtoCC(token) + " ";
            }
        }
        writeErrorList(tokenizer.getErrorList());
        FileIOUtils.writeOutput(output, outputLocation);
        FileIOUtils.writeOutput(AtoCCOutput, AtoCCLocation);
    }

    public static String printTokenContent (Token token) {
        String content;
        content = "[";
        content += token.getLineNumber() + ":";
        content +=  token.getType().toString() + ":";
        content += token.getLexeme();
        content += "]";
        return content;
    }

    public static String printAtoCC (Token token) {
        Token.TokenType tokenType= token.getType();
        switch (tokenType) {
            case FLO:
            case INT:
            case ID:
            case CMT:
            case ERROR:
                return tokenType.toString();
            default:
                return token.getLexeme();
        }
    }

    private void writeErrorList(List<TokenError> errorList) {
        String errorContent = "";
        if (errorList.size() == 0 ) {
            errorContent = "No errors!";
        }
        else {
            for (int i = 0; i <errorList.size();i++) {
                errorContent += errorList.get(i).getErrorMessage() + "\n";
            }
        }
        FileIOUtils.writeOutput(errorContent, errorLocation);
    }

    public String getInputLocation() {
        return inputLocation;
    }

    public void setInputLocation(String inputLocation) {
        this.inputLocation = inputLocation;
    }
}
