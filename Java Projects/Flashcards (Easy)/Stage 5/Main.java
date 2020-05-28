package flashcards;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        FlashCardDeck deck = new FlashCardDeck();
        while (deck.handleInput()) {
            System.out.println();
        }
    }
}

class FlashCardDeck {
    private final Map<String, String> termMap = new LinkedHashMap<>();
    private final Map<String, String> definitionMap = new LinkedHashMap<>();
    Scanner scanner = new Scanner(System.in);

    public boolean handleInput() {
        System.out.println("Input the action (add, remove, import, export, ask, exit): ");
        String command = scanner.nextLine();
        if ("add".equals(command)) {
            addCardWithChecks();
            return true;
        } else if ("remove".equals(command)) {
            removeCardWithChecks();
            return true;
        } else if ("import".equals(command)) {
            importCardsFromFile();
            return true;
        } else if ("export".equals(command)) {
            exportCardsToFile();
            return true;
        } else if ("ask".equals(command)) {
            quizNQuestions();
            return true;
        } else if ("exit".equals(command)) {
            System.out.println("Bye bye!");
            return false;
        } else {
            System.out.println("Invalid command input. Try again.");
            return true;
        }
    }

    public int size() {
        return termMap.size();
    }

    public void buildDeck(int numCards) {
        for (int i = 0; i < numCards; i++) {
            String term = null;
            System.out.println("The card #" + (i + 1) + ":");
            do {
                term = scanner.nextLine();
            } while (!checkDuplicateTerm(term));
            String definition = null;
            System.out.println("The definition of the card #" + (i + 1) + ":");
            do {
                definition = scanner.nextLine();
            } while (!checkDuplicateDefinition(definition));
            addCard(term, definition);
        }
    }

    public void runQuiz() {
        for (var pair : termMap.entrySet()) {
            System.out.println("Print the definition of \"" + pair.getKey() + "\":");
            checkResponse(pair.getKey(), scanner.nextLine());
        }
    }

    public boolean checkDuplicateTerm(String term) {
        if (termMap.containsKey(term)) {
            System.out.println("The card \"" + term + "\" already exists.");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDuplicateDefinition(String definition) {
        if (definitionMap.containsKey(definition)) {
            System.out.println("The definition \"" + definition + "\" already exists.");
            return false;
        } else {
            return true;
        }
    }

    public void addCardWithChecks() {
        System.out.println("The card: ");
        String term = scanner.nextLine();
        //Ensure is not a duplicate term
        if (!checkDuplicateTerm(term)) {
            return;
        }
        System.out.println("The definition of the card: ");
        String definition = scanner.nextLine();
        if (!checkDuplicateDefinition(definition)) {
            return;
        }
        addCard(term, definition);
        System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
    }

    public void addCard(String term, String definition) {
        termMap.put(term, definition);
        definitionMap.put(definition, term);
    }

    public void removeCardWithChecks() {
        System.out.println("The card: ");
        String term = scanner.nextLine();
        if (termMap.containsKey(term)) {
            String definition = termMap.get(term);
            termMap.remove(term);
            definitionMap.remove(definition);
            System.out.println("The card has been removed.");
        } else {
            System.out.println("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    public void importCardsFromFile() {
        System.out.println("File name: ");
        String filename = scanner.nextLine();
        File importFile = new File(filename);
        if (!importFile.exists()) {
            System.out.println("File not found.");
            return;
        } else {
            try (Scanner fileScanner = new Scanner(importFile)) {
                int counter = 0;
                while (fileScanner.hasNext()) {
                    String term = fileScanner.nextLine();
                    String definition = fileScanner.nextLine();
                    if (termMap.containsKey(term)) {
                        String oldDefinition = termMap.get(term);
                        definitionMap.remove(oldDefinition);
                    }
                    termMap.put(term, definition);
                    definitionMap.put(definition, term);
                    counter++;
                }
                System.out.println(counter + " cards have been loaded.");
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }
        }
    }

    public void exportCardsToFile() {
        System.out.println("File name: ");
        String filename = scanner.nextLine();
        File exportFile = new File(filename);
        try (PrintWriter printWriter = new PrintWriter(exportFile)) {
            int counter = 0;
            for (var pair : termMap.entrySet()) {
                printWriter.println(pair.getKey());
                printWriter.println(pair.getValue());
                counter++;
            }
            System.out.println(counter + " cards have been saved.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found : " + e.getMessage());
        }
    }

    public void quizNQuestions() {
        System.out.println("How many times to ask?");
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] termArray = new String[termMap.size()];
        System.arraycopy(termMap.keySet().toArray(), 0, termArray, 0, termMap.size());
        Random random = new Random(1);
        for (int i = 0; i < n; i++) {
            int questionNum = random.nextInt(termArray.length);
            String term = termArray[questionNum];
            System.out.println("Print the definition of \"" + term + "\":");
            String definition = scanner.nextLine();
            checkResponse(term, definition);
        }
    }

    public boolean checkResponse(String term, String definition) {
        if (!definitionMap.containsKey(definition)) {
            System.out.println("Wrong answer. The correct one is \"" + termMap.get(term) + "\".");
            return false;
        } else if (!definition.equalsIgnoreCase(termMap.get(term))) {
            System.out.println("Wrong answer. The correct one is \"" + termMap.get(term)
                    + "\", you've just written the definition of \"" + definitionMap.get(definition)
                    + "\".");
            return false;
        } else {
            System.out.println("Correct answer.");
            return true;
        }
    }
}

