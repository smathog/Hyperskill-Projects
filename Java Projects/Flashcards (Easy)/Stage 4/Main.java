package flashcards;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the number of cards: ");
        int numCards = scanner.nextInt();
        scanner.nextLine();

        //Build up a deck of cards
        FlashCardDeck deck = new FlashCardDeck();
        deck.buildDeck(numCards);

        //Quiz based on deck of cards
        deck.runQuiz();
    }
}

class FlashCardDeck {
    private final Map<String, String> termMap = new LinkedHashMap<>();
    private final Map<String, String> definitionMap = new LinkedHashMap<>();
    Scanner scanner = new Scanner(System.in);

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
            System.out.println("The card \"" + term + "\" already exists. Try again: ");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDuplicateDefinition(String definition) {
        if (definitionMap.containsKey(definition)) {
            System.out.println("The definition \"" + definition + "\" already exists. Try again: ");
            return false;
        } else {
            return true;
        }
    }

    public void addCard(String term, String definition) {
        termMap.put(term, definition);
        definitionMap.put(definition, term);
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

