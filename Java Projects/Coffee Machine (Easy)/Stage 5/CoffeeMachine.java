package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        boolean exitNow = false;
        while (!exitNow) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.next();
            System.out.println();
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
                case "remaining":
                    remaining();
                    break;
                case "exit":
                    exitNow = true;
                    break;
            }
            System.out.println();
        }
    }
    
    private static void remaining() {
        String[] identifier = {
                "water",
                "milk",
                "coffee beans",
                "disposable cups",
                "money"
        };
        System.out.println("The coffee machine has:");
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                System.out.print('$');
            }
            System.out.println(inventory[i] + " of " + identifier[i]);
        }
    }

    private static void take() {
        System.out.println("I give you " + inventory[4]);
        adjustInventory(0, 0, 0, 0, -inventory[4]);
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
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, " +
                "back - to main menu:");
        String choice = scanner.next();
        boolean success = false;
        switch (choice) {
            case "1":
                success = buyEspresso();
                break;
            case "2":
                success = buyLatte();
                break;
            case "3":
                success = buyCappuccino();
                break;
            case "back":
                return;
        }
        if (success) {
            System.out.println("I have enough resources, making you a coffee!");
        }
    }

    private static boolean buyEspresso() {
        if (checkResources(250, 0, 16)) {
            adjustInventory(-250, 0, -16, -1, 4);
            return true;
        } else {
            return false;
        }
    }

    private static boolean buyLatte() {
        if (checkResources(350, 75, 20))
        {
            adjustInventory(-350, -75, -20, -1, 7);
            return true;
        } else {
            return false;
        }
    }

    private static boolean buyCappuccino() {
        if (checkResources(200, 100, 12)) {
            adjustInventory(-200, -100, -12, -1, 6);
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkResources(int water, int milk, int beans) {
        if (water > inventory[0]) {
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (milk > inventory[1]) {
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (beans > inventory[2]) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        } else if (1 > inventory[3]) {
            System.out.println("Sorry, not enough disposable cups!");
            return false;
        }
        return true;
    }

    private static void adjustInventory(int water, int milk, int beans, int cups, int cash) {
        inventory[0] += water;
        inventory[1] += milk;
        inventory[2] += beans;
        inventory[3] += cups;
        inventory[4] += cash;
    }

    private static Scanner scanner = new Scanner(System.in);
    private static int[] inventory = {400, 540, 120, 9, 550};
}
