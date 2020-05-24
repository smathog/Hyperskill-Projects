package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        while (true) {
            if (state == ValidStates.IDLE) {
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                state = ValidStates.PROCESSING;
            } else if (state != ValidStates.OFF) {
                processInput(scanner.next());
            } else if (state == ValidStates.OFF) {
                break;
            }
        }
    }

    public static void processInput(String userInput) {
        if (state == ValidStates.PROCESSING) {
            System.out.println();
            switch (userInput) {
                case "buy":
                    System.out.println("What do you want to buy? 1 - espresso, " +
                            "2 - latte, 3 - cappuccino, back - to main menu: ");
                    state = ValidStates.BUYING;
                    break;
                case "fill":
                    state = ValidStates.FILLING;
                    fill(-1);
                    break;
                case "take":
                    take();
                    state = ValidStates.IDLE;
                    System.out.println();
                    break;
                case "remaining":
                    remaining();
                    state = ValidStates.IDLE;
                    System.out.println();
                    break;
                case "exit":
                    state = ValidStates.OFF;
                    return;
            }
        } else if (state == ValidStates.FILLING && fillState != FillStates.NA) {
            fill(Integer.parseInt(userInput));
            if (fillState == FillStates.NA) {
                state = ValidStates.IDLE;
                System.out.println();
            }
        } else if (state == ValidStates.BUYING) {
            buy(userInput);
            state = ValidStates.IDLE;
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

    private static void fill(int input) {
        switch (fillState) {
            case NA:
                System.out.println("Write how many ml of water do you want to add:");
                fillState = FillStates.WATER;
                break;
            case WATER:
                inventory[0] += input;
                System.out.println("Write how many ml of milk do you want to add:");
                fillState = FillStates.MILK;
                break;
            case MILK:
                inventory[1] += input;
                System.out.println("Write how many grams of coffee beans do you want to add:");
                fillState = FillStates.BEANS;
                break;
            case BEANS:
                inventory[2] += input;
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                fillState = FillStates.CUPS;
                break;
            case CUPS:
                inventory[3] += input;
                fillState = FillStates.NA;
        }
    }

    private static void buy(String choice) {
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

    private enum ValidStates {
        IDLE, FILLING, BUYING, PROCESSING, OFF
    }

    private enum FillStates {
        NA, WATER, MILK, BEANS, CUPS
    }

    private static ValidStates state = ValidStates.IDLE;
    private static FillStates fillState = FillStates.NA;
    private static Scanner scanner = new Scanner(System.in);
    private static int[] inventory = {400, 540, 120, 9, 550};
}
