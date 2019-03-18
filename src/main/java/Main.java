import lexer.LexerDriver;
import semanticAnalyzer.SemanticDriver;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import syntacticAnalyzer.ParserDriver;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        LexerDriver lex = new LexerDriver();
        ParserDriver parserDriver = new ParserDriver();
        SemanticDriver semanticDriver = new SemanticDriver();

        lex.start();
        parserDriver.start(lex.getTokens());

        if(parserDriver.isParseGood())
            semanticDriver.start((ProgASTNode) parserDriver.getAST());
    }
}
