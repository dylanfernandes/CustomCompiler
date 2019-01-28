package lexer;

import java.io.*;
import java.util.Scanner;

public class LexerDriver {

    private String inputLocation = "src/input/input.txt";
    private String outputLocation = "src/output/output.txt";
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
        while (!tokenizer.isEndOfInput()){
            token = tokenizer.nextToken();
            output += printTokenContent(token);
        }
        writeOutput(output);
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

    String getInput() throws FileNotFoundException {
        String content = new Scanner(new File(inputLocation)).useDelimiter("\\Z").next();
        return content;
    }

    boolean writeOutput(String content) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputLocation), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        return true;
    }
}
