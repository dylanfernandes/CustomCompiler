package utils;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class FileIOUtilsTest {

    @Test
    public void getInputTest() throws FileNotFoundException {
        assertEquals("Test test" , FileIOUtils.getInput("src/test/java/lexer/input/testInput.txt"));
    }

    @Test
    public void getOutputTest() {
        assertTrue("Test" , FileIOUtils.writeOutput("Test", "src/test/java/lexer/output/testOutput.txt"));
    }
}