package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexerDriver {

    public LexerDriver() {
    }

    String getInput() throws FileNotFoundException {
        String content = new Scanner(new File("src/input/input.txt")).useDelimiter("\\Z").next();
        return content;
    }
}
