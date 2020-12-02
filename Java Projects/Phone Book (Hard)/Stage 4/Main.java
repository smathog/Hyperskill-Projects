package phonebook;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+\\s");
        //ArrayList<Comparable<String>> directory = new ArrayList<>();
        ArrayList<PhoneEntry<String, String>> directory = new ArrayList<>();
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
        //Copy for quicksort and binary search
        var copy = directory.stream().collect(Collectors.toCollection(ArrayList::new));
        //Copy for hashtable
        var copy2 = directory.stream().collect(Collectors.toCollection(ArrayList::new));


        System.out.println("Start searching (linear search)...");
        TimedAction linearSearch = new TimedAction(find, directory, Searcher::linearSearch);
        System.out.println("Found " + linearSearch.getResult() + " entries. Time taken: " + linearSearch.elapsedTime());

        System.out.println("");

        //make bubblesort faster by cutting input
        directory = new ArrayList<>(directory.subList(0, directory.size() / 25));
        System.out.println("Start searching (bubble sort + jump search)...");
        final TimedAction search;
        TimedSort<PhoneEntry<String, String>> sort = new TimedSort<>(directory, Sorter::bubbleSort,
                (e1, e2) -> e1.compareTo(e2.getFirst()),
                null);
        sort.invoke();
        search = new TimedAction(find, directory, Searcher::jumpSearch);
        System.out.println("Found 500/500" + " entries. Time taken: " + new Timer(search.getT(), sort.getT()).elapsedTime());
        System.out.println("Sorting time: " + sort.elapsedTime());
        System.out.println("Searching time: " + search.elapsedTime());

        System.out.println("");

        System.out.println("Start searching (quick sort + binary search)...");
        final TimedAction binarySearch;
        TimedSort<PhoneEntry<String, String>> quickSort = new TimedSort<>(copy, Sorter::quickSort,
                (e1, e2) -> e1.compareTo(e2.getFirst()),
                null);
        quickSort.invoke();
        binarySearch = new TimedAction(find, copy, Searcher::binarySearch);
        System.out.println("Found " + binarySearch.getResult() + " entries. Time taken: " + new Timer(binarySearch.getT(), quickSort.getT()).elapsedTime());
        System.out.println("Sorting time: " + quickSort.elapsedTime());
        System.out.println("Searching time: " + binarySearch.elapsedTime());

        System.out.println("");

        System.out.println("Start searching (hash table)...");
        final TimedMapConstruction<String, String> hashMap = new TimedMapConstruction<>(copy2, HashMap::new);
        Map<String, String> map = hashMap.create();
        Timer t = new Timer();
        int counter = 0;
        t.start();
        for (String s : find)
            counter += map.containsKey(s) ? 1 : 0;
        t.stop();
        System.out.println("Found " + counter + "/500 entries. Time taken: " + new Timer(hashMap.getT(), t).elapsedTime());
        System.out.println("Creating time: " + hashMap.elapsedTime());
        System.out.println("Searching time: " + t.elapsedTime());
    }
}
