package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.regex.*;
import java.util.HashMap;

public class SearchEngine {
    private static final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, ArrayList<Integer>> indexLookup;
    private final ArrayList<Person> people;

    public SearchEngine(String inFile) {
        people = new ArrayList<>();
        indexLookup = new HashMap<>();
        if (inFile == null)
            loadPeople();
        else
            loadPeopleFromFile(inFile);
        generateIndexLookup();
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

    private void loadPeopleFromFile(String inFile) {
        try (Scanner fileScanner = new Scanner(new File(inFile))) {
            while (fileScanner.hasNextLine())
                people.add(new Person(fileScanner.nextLine()));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
        }
    }

    private void generateIndexLookup() {
        for (int index = 0 ; index < people.size(); ++index) {
            Person p = people.get(index);
            for (String s : p.getAllVals()) {
                if (s != null) {
                    indexLookup.putIfAbsent(s.toLowerCase(), new ArrayList<>());
                    indexLookup.get(s.toLowerCase()).add(index);
                }
            }
        }
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
                    var results = searchImplByIndex(term);
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

    private ArrayList<Person> searchImplByIndex(String searchTerm) {
        return indexLookup.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(searchTerm))
                .flatMap(e -> e.getValue().stream())
                .distinct()
                .map(people::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
