import lexer.LexerDriver;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        LexerDriver lex = new LexerDriver();
        lex.start();
    }
}
