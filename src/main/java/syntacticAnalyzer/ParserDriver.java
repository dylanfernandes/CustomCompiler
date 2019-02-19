package syntacticAnalyzer;

import utils.FileIOUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ParserDriver {

    private String inputLocation = "src/output/output.txt";

    public void start() throws FileNotFoundException {
        FileIOUtils.getInput(inputLocation);
    }

}
