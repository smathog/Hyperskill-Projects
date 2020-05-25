package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Build up a deck of cards
        System.out.println("Input the number of cards: ");
        int numCards = scanner.nextInt();
        //need to discard newline
        scanner.nextLine();
        FlashCardDeck newDeck = new FlashCardDeck(numCards);
        for (int i = 0; i < numCards; i++) {
            System.out.println("The card #" + (i + 1) + ": ");
            String term = scanner.nextLine();
            System.out.println("The definition of the card #" + (i + 1) + ": ");
            String definition = scanner.nextLine();
            try {
                newDeck.addCard(term, definition);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error: attempt to add card failed" );
                System.out.println("Exception message: " + e.getMessage());
                return;
            }
        }

        //Quiz based on deck of cards
        for (int i = 0 ; i < numCards; i++) {
            FlashCard currentCard;
            try {
                currentCard = newDeck.cardAt(i);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Error: attempted to access card with invalid index");
                System.out.println("Exception message: " + e.getMessage());
                return;
            }
            System.out.println("Print the definition of \"" + currentCard.getTerm() + "\":");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase(currentCard.definition())) {
                System.out.println("Correct answer.");
            } else {
                System.out.println("Wrong answer. The correct one is \"" + currentCard.definition() +"\".");
            }
        }
    }
}

class FlashCardDeck {
    private FlashCard[] deck = null;
    int numCards = 0;

    public FlashCardDeck(int size) {
        this.deck = new FlashCard[size];
    }

    public void addCard(String term, String definition) throws IndexOutOfBoundsException {
        if (numCards < deck.length) {
            deck[numCards] = new FlashCard(term, definition);
            numCards++;
        } else {
            throw new IndexOutOfBoundsException("Attempted to add card past deck capacity");
        }
    }

    public FlashCard cardAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > numCards || index > deck.length) {
            throw new IndexOutOfBoundsException("Attempted to draw card with invalid index " + index);
        } else {
            return deck[index];
        }
    }
}

class FlashCard {
    private String term;
    private String definition;

    public FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public String getTerm() {
        return this.term;
    }

    public String definition() {
        return this.definition;
    }
}

