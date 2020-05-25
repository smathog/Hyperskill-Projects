package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String term = scanner.nextLine();
        String definition = scanner.nextLine();
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase(definition)) {
            System.out.println("Your answer is right!");
        } else {
            System.out.println("Your answer is wrong!");
        }
    }
}
