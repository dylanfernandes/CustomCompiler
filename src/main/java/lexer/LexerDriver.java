package lexer;

import java.io.*;
import java.util.Scanner;

public class LexerDriver {

    private String inputLocation = "src/input/input.txt";
    private String outputLocation = "src/output/output.txt";
    private String AtoCCLocation = "src/output/AtoCCOutput.txt";

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
        Tokenizer tokenizer = new Tokenizer(getInput());
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
        writeOutput(output, outputLocation);
        writeOutput(AtoCCOutput, AtoCCLocation);
    }

    String printTokenContent (Token token) {
        String content;
        content = "[";
        content += token.getLineNumber() + ":";
        content +=  token.getType().toString() + ":";
        content += token.getLexeme();
        content += "]";
        return content;
    }

    String printAtoCC (Token token) {
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

    String getInput() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(inputLocation));
        String content = "";
        String temp;
        while ( (temp = scanner.nextLine()) != null) {
            // No need to convert to char array before printing
            content += temp;
            if (scanner.hasNext()) {
                content += '\n';
            }
            else
                break;
        }
        return content;
    }

    boolean writeOutput(String content, String output) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(output), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        return true;
    }
}
