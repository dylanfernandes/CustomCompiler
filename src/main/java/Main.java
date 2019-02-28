import lexer.LexerDriver;
import syntacticAnalyzer.ParserDriver;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        LexerDriver lex = new LexerDriver();
        ParserDriver parserDriver = new ParserDriver();
        lex.start();
        parserDriver.start(lex.getTokens());
    }
}
