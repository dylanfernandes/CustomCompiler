package lexer;

import utils.FileIOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexerDriver {

    private String inputLocation = "src/input/input.txt";
    private String outputLocation = "src/output/output.txt";
    private String AtoCCLocation = "src/output/AtoCCOutput.txt";
    private String errorLocation = "src/output/error.txt";
    private List<Token> tokens;
    private  boolean hasTokens;

    public LexerDriver() {
        tokens = new ArrayList<Token>();
        hasTokens= false;
    }

    public LexerDriver(String location) {
        tokens = new ArrayList<Token>();
        hasTokens= false;
        if(location.length() > 0)
            inputLocation = location;
    }

    public void start(String input) throws FileNotFoundException {
        inputLocation = input;
        start();
    }

    public String getInputLocation() {
        return inputLocation;
    }

    public void setInputLocation(String inputLocation) {
        this.inputLocation = inputLocation;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Token> getTokensFromInput(String input) {
        Tokenizer tokenizer = new Tokenizer(input);
        tokens.clear();
        Token token;
        while (!tokenizer.isEndOfInput()) {
            token = tokenizer.nextToken();
            if(token != null && token.getType() != Token.TokenType.NEWLINE) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public boolean hasTokens() {
        return tokens.size() > 0;
    }

    public void start() throws FileNotFoundException {
        Tokenizer tokenizer = new Tokenizer(FileIOUtils.getInput(inputLocation));
        Token token;
        String output = "";
        String AtoCCOutput = "";
        tokens.clear();

        while (!tokenizer.isEndOfInput()){
            token = tokenizer.nextToken();
            if(token.getType() == Token.TokenType.NEWLINE) {
                output += '\n';
                AtoCCOutput += '\n';
            }
            else {
                tokens.add(token);
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

}
