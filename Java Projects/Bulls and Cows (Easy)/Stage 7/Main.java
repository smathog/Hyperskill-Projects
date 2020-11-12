package bullscows;

import java.util.Scanner;
import java.util.HashSet;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        game();
    }

    private static void game() {
        Scanner scanner = new Scanner(System.in);

        //Generate random code
        System.out.println("Input the length of the secret code: ");
        int length;
        try {
            length = Integer.parseInt(scanner.nextLine());
            if (length < 1) {
                System.out.println("Error: cannot have a code of length less than 1");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: this is not a valid number.");
            return;
        }
        System.out.println("Input the number of possible symbols in the code: ");
        int numSymbols;
        try {
            numSymbols = Integer.parseInt(scanner.nextLine());
            if (numSymbols < 1 || numSymbols > 36) {
                System.out.println("Error: invalid number of possible symbols");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: this is not a valid number.");
            return;
        }
        String code = generatePseudoRandomCode(length, numSymbols);
        if (code == null) {
            System.out.println("Error: it's not possible to generate a secret code longer than the " +
                    "number of available digits.");
            return;
        }
        System.out.println("The secret is prepared: "
                + "*".repeat(length) + " "
                + generateRepresentation(numSymbols)
                + ".");

        //Play the game
        int turn = 1;
        while (true) {
            System.out.println("Turn " + turn + ": ");
            String guess = scanner.nextLine();
            if (!containsValidSymbols(guess, numSymbols)) {
                System.out.println("Error: guess contains invalid symbols!");
                continue;
            }
            if (grader(guess, code))
                break;
        }
        System.out.println("Congratulations! You guessed the secret code.");
    }

    private static String generatePseudoRandomCode(int length, int numSymbols) {
        //Handle invalid request
        if (length > numSymbols)
            return null;

        //Setup necessary objects for generating code String
        HashSet<Character> uniqueDigits = new HashSet<>();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        //Handle first character (cannot be 0)
        char first = intToChar(r.nextInt(numSymbols - 1) + 1);
        sb.append(first);
        uniqueDigits.add(first);

        //Handle remaining characters
        while (sb.length() < length) {
            char temp = intToChar(r.nextInt(numSymbols));
            if (!uniqueDigits.contains(temp)) {
                uniqueDigits.add(temp);
                sb.append(temp);
            }
        }

        ///Return completed code
        return sb.toString();
    }

    private static boolean grader(String guess, String code) {
        //Length error handling
        if (!(guess.length() == code.length())) {
            System.out.println("Error: an answer must have the same length as the secret code!");
            return false;
        }
        //Set up counting variables
        int numCows = 0;
        int numBulls = 0;

        //Count and symbol error handling
        for (int index = 0; index < guess.length(); ++index) {
            if (code.indexOf(guess.charAt(index)) == -1)
                continue;
            else
                if (code.charAt(index) == guess.charAt(index))
                    ++numBulls;
                else
                    ++numCows;
        }

        //Grading printout
        if (numCows == 0 && numBulls == 0) {
            System.out.println("Grade: None.");
        } else if (numCows > 0 && numBulls == 0) {
            System.out.println("Grade: " + numCows + " cow(s).");
        } else if (numCows == 0 && numBulls > 0) {
            System.out.println("Grade: " + numBulls + " bull(s).");
        } else {
            System.out.println("Grade: " + numBulls + " bull(s) "
            + "and " + numCows + " cow(s).");
        }

        //Return true if all bulls, otherwise false
        return numBulls == code.length();
    }

    private static char intToChar(int input) {
        if (input < 10)
            return (char) ('0' + input);
        else
            return (char) ('a' + input - 10);
    }

    private static String generateRepresentation(int numValidDigits) {
        StringBuilder sb = new StringBuilder();
        sb.append("(0-");
        if (numValidDigits <= 10)
            sb.append(numValidDigits - 1);
        else {
            sb.append("9, a-");
            sb.append(intToChar(numValidDigits - 1));
        }
        sb.append(")");
        return sb.toString();
    }

    private static boolean containsValidSymbols(String answer, int numValidDigits) {
        for (int index = 0; index < answer.length(); ++index) {
            char ch = answer.charAt(index);
            if (numValidDigits <= 10 && !('0' <= ch && ch <= intToChar(numValidDigits - 1)))
                return false;
            else if (numValidDigits > 10) {
                if (!(('0' <= ch && ch <= intToChar(9)) || ('a' <= ch && ch <= intToChar(numValidDigits - 1))))
                    return false;
            }
        }
        return true;
    }
}
