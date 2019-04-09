import codeGeneration.CodeGenerationDriver;
import lexer.LexerDriver;
import semanticAnalyzer.SemanticDriver;
import syntacticAnalyzer.AST.semanticNodes.ProgASTNode;
import syntacticAnalyzer.ParserDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String fileLocation;
        boolean validFile = false;
        do {
            fileLocation = getFileLocation();
            validFile = validateFile(fileLocation);
            if (validFile) {
                LexerDriver lex = new LexerDriver(fileLocation);
                ParserDriver parserDriver = new ParserDriver();
                SemanticDriver semanticDriver = new SemanticDriver();
                CodeGenerationDriver codeGenerationDriver = new CodeGenerationDriver();

                lex.start();
                parserDriver.start(lex.getTokens());

                if (parserDriver.isParseGood())
                    semanticDriver.start((ProgASTNode) parserDriver.getAST());

                if (!semanticDriver.hasError())
                    codeGenerationDriver.start((ProgASTNode) parserDriver.getAST());
            } else {
                System.out.println("Invalid file, please re-enter!\n");
            }
        }
        while (!validFile);

    }

    private static boolean validateFile(String location){
        File file = new File(location);
        return file.exists() && !file.isDirectory();
    }

    private static String getFileLocation(){
        String location = "";
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a file name(must be located in input directory, empty entry will take default file): ");
        location = reader.nextLine();
        if(location.length() > 0)
            location = "src/input/" + location;
        else
            location = "src/input/input.txt";
        return location;
    }
}
