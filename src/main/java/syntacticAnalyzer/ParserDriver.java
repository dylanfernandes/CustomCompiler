package syntacticAnalyzer;

import lexer.Token;
import syntacticAnalyzer.AST.ASTNode;
import utils.FileIOUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class ParserDriver {
    private String outputLocation = "src/output/syntaxOutput.txt";
    private ParserAST parser;

    public void start(List<Token> tokens) {
        parser = new ParserAST(tokens);
        parser.parse();
        FileIOUtils.writeOutput(parser.printAST(), outputLocation);
    }

    public ASTNode getAST() {
        return parser.getAST();
    }
    public boolean isParseGood() {
        return parser.isParseGood();
    }
}
