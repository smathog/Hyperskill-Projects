package banking;

import java.util.Scanner;

public class Menu {
    private final Backend be;

    public Menu(String fileName) {
        Scanner scanner = new Scanner(System.in);
        be = Backend.getInstance(fileName);
        menuLoop(scanner);
    }

    private void menuLoop(Scanner scanner) {
        innerLoop:
        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            String choice = scanner.nextLine();
            System.out.println();
            switch (choice) {
                case "0":
                    break innerLoop;
                case "1": {
                    Card card = be.generateCard();
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(card.getCardNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(card.getPin());
                    break;
                }
                case "2": {
                    System.out.println("Enter your card number:");
                    String number = scanner.nextLine();
                    System.out.println("Enter your pin:");
                    String PIN = scanner.nextLine();
                    Card card = be.getCard(number);
                    System.out.println();
                    if (card == null || !card.getPin().equals(PIN))
                        System.out.println("Wrong card number or PIN!");
                    else {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        if (!accountMenu(card, scanner))
                            break innerLoop;
                        else
                            break;
                    }
                }
                default:
                    break;
            }
            System.out.println();
        }
        System.out.println("Bye!");
        be.closeConnection();
    }

    private boolean accountMenu(Card card, Scanner scanner) {
        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            String choice = scanner.nextLine();
            System.out.println();
            switch (choice) {
                case "0":
                    return false;
                case "1":
                    System.out.println("Balance: " + card.getBalance());
                    break;
                case "2":
                    System.out.println("You have successfully logged out!");
                    return true;
                default:
                    break;
            }
            System.out.println();
        }
    }
}
