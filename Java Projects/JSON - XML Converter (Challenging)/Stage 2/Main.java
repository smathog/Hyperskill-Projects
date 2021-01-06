package converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            String fileContents = new String(Files.readAllBytes(Path.of("test.txt")));
            String output = Converter.convert(fileContents);
            System.out.println(output);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
