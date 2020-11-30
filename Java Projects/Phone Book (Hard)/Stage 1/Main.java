package phonebook;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> directory = new ArrayList<>();
        ArrayList<String> find = new ArrayList<>();
        try (Scanner fs1 = new Scanner(new File("directory.txt"));
             Scanner fs2 = new Scanner(new File("find.txt"))) {
            while (fs1.hasNextLine())
                directory.add(fs1.nextLine());
            while (fs2.hasNextLine())
                find.add(fs2.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
        }
        System.out.println("Start searching...");
        long startTime = System.currentTimeMillis();
        long numMatches = find.stream().filter(s -> directory.stream().anyMatch(d -> d.contains(s))).count();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        elapsedTime -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime);
        elapsedTime -= TimeUnit.SECONDS.toMillis(seconds);
        System.out.print("Found " + numMatches + " / " + find.size() + " entries. Time taken: ");
        System.out.println(String.format("%d min. %d sec. %d ms.", minutes, seconds, elapsedTime));
    }
}
