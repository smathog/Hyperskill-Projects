package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        printState();
        System.out.println("Write action (buy, fill, take):");
        String action = scanner.next();
        switch (action) {
            case "buy":
                buy();
                break;
            case "fill":
                fill();
                break;
            case "take":
                take();
                break;
        }
        System.out.println();
        printState();
    }
    
    private static void printState() {
        String[] identifier = {
                "water",
                "milk",
                "coffee beans",
                "disposable cups",
                "money"
        };
        System.out.println("The coffee machine has:");
        for (int i = 0; i < 5; i++) {
            System.out.println(inventory[i] + " of " + identifier[i]);
        }
    }

    private static void take() {
        System.out.println("I give you " + inventory[4]);
        inventory[4] = 0;
    }

    private static void fill() {
        String[] statements = {
                "Write how many ml of water do you want to add:",
                "Write how many ml of milk do you want to add:",
                "Write how many grams of coffee beans do you want to add:",
                "Write how many disposable cups of coffee do you want to add:"
        };
        for (int i = 0; i < 4; i++) {
            System.out.println(statements[i]);
            inventory[i] += scanner.nextInt();
        }
    }

    private static void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                buyEspresso();
                break;
            case 2:
                buyLatte();
                break;
            case 3:
                buyCappuccino();
                break;
        }
    }

    private static void buyEspresso() {
        inventory[0] -= 250;
        inventory[2] -= 16;
        inventory[3] -= 1;
        inventory[4] += 4;
    }

    private static void buyLatte() {
        inventory[0] -= 350;
        inventory[1] -= 75;
        inventory[2] -= 20;
        inventory[3] -= 1;
        inventory[4] += 7;
    }

    private static void buyCappuccino() {
        inventory[0] -= 200;
        inventory[1] -= 100;
        inventory[2] -= 12;
        inventory[3] -= 1;
        inventory[4] += 6;
    }

    private static Scanner scanner = new Scanner(System.in);
    private static int[] inventory = {400, 540, 120, 9, 550};
}
