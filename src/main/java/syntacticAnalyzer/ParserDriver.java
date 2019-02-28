package syntacticAnalyzer;

import lexer.Token;
import utils.FileIOUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class ParserDriver {
    private String outputLocation = "src/output/syntaxOutput.txt";


    public void start(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        parser.parse();
        FileIOUtils.writeOutput(parser.getSyntax(), outputLocation);
    }

}
