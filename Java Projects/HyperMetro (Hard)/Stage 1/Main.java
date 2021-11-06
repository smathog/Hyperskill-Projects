package metro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Path filePath = Paths.get(args[0]);
        if (Files.notExists(filePath)) {
            System.out.println("Error! Such a file doesn't exist!");
        } else {
            // Get file as LinkedList of Strings...
            try {
                LinkedList<String> stations = Files.lines(filePath)
                        .collect(Collectors.toCollection(LinkedList::new));
                for (int i = -1; i < stations.size() - 1; ++i) {
                    if (i == -1) {
                        System.out.printf("depot - %s - %s%n", stations.get(0), stations.get(1));
                    } else if (i == stations.size() - 2) {
                        System.out.printf("%s - %s - depot%n", stations.get(i), stations.get(i + 1));
                    } else {
                        System.out.printf("%s - %s - %s%n", stations.get(i), stations.get(i + 1), stations.get(i + 2));
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.getStackTrace();
            }
        }
    }
}
