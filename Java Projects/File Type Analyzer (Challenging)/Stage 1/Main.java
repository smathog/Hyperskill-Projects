package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        String search = args[1];
        String type = args[2];
        try {
            String fileContents = new String(Files.readAllBytes(Paths.get(fileName)));
            if (fileContents.contains(search))
                System.out.println(type);
            else
                System.out.println("Unknown file type");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
