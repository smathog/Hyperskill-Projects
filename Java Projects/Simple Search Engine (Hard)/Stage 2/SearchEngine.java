package search;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.regex.*;

public class SearchEngine {
    private static final Scanner scanner = new Scanner(System.in);
    private final ArrayList<Person> people;

    public SearchEngine() {
        people = new ArrayList<>();
    }

    public void loadPeople() {
        System.out.println("Enter the number of people: ");
        int numPeople = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter all people: ");
        for (int i = 0; i < numPeople; ++i)
            people.add(new Person(scanner.nextLine()));
        System.out.println();
    }

    public void search() {
        System.out.println("Enter the number of search queries: ");
        int numQueries = Integer.parseInt(scanner.nextLine());
        System.out.println();
        for (int i = 0; i < numQueries; ++i) {
            System.out.println("Enter data to search people: ");
            String term = scanner.nextLine();
            var results = searchImpl(term);
            if (results.isEmpty())
                System.out.println("No matching people found.");
            else {
                System.out.println();
                System.out.println("Found people: ");
                for (Person p : results)
                    System.out.println(p);
            }
            System.out.println();
        }
    }

    private ArrayList<Person> searchImpl(String searchTerm) {
        Pattern p = Pattern.compile(Pattern.quote(searchTerm), Pattern.CASE_INSENSITIVE);
        return people.stream().filter(i -> {
            if (i.getFirstName() != null && p.matcher(i.getFirstName()).find())
                return true;
            else if (i.getLastName() != null && p.matcher(i.getLastName()).find())
                return true;
            else if (i.getEmail() != null && p.matcher(i.getEmail()).find())
                return true;
            else
                return false;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
