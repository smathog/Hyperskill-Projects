package phonebook;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+\\s");
        ArrayList<Comparable<String>> directory = new ArrayList<>();
        ArrayList<String> find = new ArrayList<>();
        try (Scanner fs1 = new Scanner(new File("directory.txt"));
             Scanner fs2 = new Scanner(new File("find.txt"))) {
            while (fs1.hasNextLine()) {
                String line = fs1.nextLine();
                Matcher m = p.matcher(line);
                m.find();
                directory.add(new PhoneEntry<>(line.substring(m.end()), m.group()));
            }
            while (fs2.hasNextLine())
                find.add(fs2.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
        }

        System.out.println("Start searching (linear search)...");
        TimedAction linearSearch = new TimedAction(find, directory, Searcher::linearSearch);
        System.out.print("Found " + linearSearch.getResult() + " entries. Time taken: " + linearSearch.elapsedTime());

        System.out.println("");

        System.out.println("Start searching (bubble sort + jump search)...");
        final TimedAction search;
        TimedSort<Comparable<String>> sort = new TimedSort<>(directory, Sorter::bubbleSort,
                (o1, o2) -> o1.compareTo(((PhoneEntry<String, String>) o2).getFirst()),
                10 * linearSearch.getT().getTimeElapsedTotalMS());
        sort.invoke();
        if (sort.getState())
            search = new TimedAction(find, directory, Searcher::jumpSearch);
        else
            search = new TimedAction(find, directory, Searcher::linearSearch);
        System.out.println("Found " + search.getResult() + " entries. Time taken: " + new Timer(search.getT(), sort.getT()).elapsedTime());
        System.out.print("Sorting time: " + sort.elapsedTime());
        if (sort.getState())
            System.out.println();
        else
            System.out.println(" - STOPPED, moved to linear search");
        System.out.println("Searching time: " + search.elapsedTime());

    }
}
