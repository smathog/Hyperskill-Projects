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
        loadPeople();
        while (!runMenu());
    }

    private void loadPeople() {
        System.out.println("Enter the number of people: ");
        int numPeople = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter all people: ");
        for (int i = 0; i < numPeople; ++i)
            people.add(new Person(scanner.nextLine()));
        System.out.println();
    }

    private boolean runMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
        String choice = scanner.nextLine();
        System.out.println();
        switch (choice) {
            case "1":
                System.out.println("Enter a name or email to search all suitable people.");
                {
                    String term = scanner.nextLine();
                    var results = searchImpl(term);
                    if (results.isEmpty())
                        System.out.println("No matching people found.");
                    else {
                        for (Person p : results)
                            System.out.println(p);
                    }
                }
                break;
            case "2":
                System.out.println("=== List of people ===");
                for (Person p : people)
                    System.out.println(p);
                break;
            case "0":
                System.out.println("Bye!");
                return true;
            default:
                System.out.println("Incorrect option! Try again.");
                break;
        }
        System.out.println();
        return false;
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
