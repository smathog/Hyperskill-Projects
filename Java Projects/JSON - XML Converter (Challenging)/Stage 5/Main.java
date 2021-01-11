package converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            var filePath = Paths.get("test.txt");
            String fileContents = Files.readString(filePath);
           // expandJSON(fileContents);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
