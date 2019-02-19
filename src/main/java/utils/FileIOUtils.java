package utils;

import java.io.*;
import java.util.Scanner;

public class FileIOUtils {

    public static String getInput(String inputLocation) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(inputLocation));
        String content = "";
        String temp;
        while ( (temp = scanner.nextLine()) != null) {
            // No need to convert to char array before printing
            content += temp;
            if (scanner.hasNext()) {
                content += '\n';
            }
            else
                break;
        }
        return content;
    }

    public static boolean writeOutput(String content, String output) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(output), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        return true;
    }
}
