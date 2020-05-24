package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write how many ml of water the coffee machine has:");
        int numPossibleCups = scanner.nextInt() / 200;
        System.out.println("Write how many ml of milk the coffee machine has:");
        int mlMilk = scanner.nextInt();
        numPossibleCups = (mlMilk / 50) < numPossibleCups ? (mlMilk / 50) : numPossibleCups;
        System.out.println("Write how many grams of coffee beans the coffee machine has:");
        int gramsBeans = scanner.nextInt();
        numPossibleCups = (gramsBeans / 15) < numPossibleCups ? (gramsBeans / 15) : numPossibleCups;
        System.out.println("Write how many cups of coffee you will need:");
        int numCups = scanner.nextInt();
        if (numPossibleCups == numCups) {
            System.out.println("Yes, I can make that amount of coffee");
        } else if (numPossibleCups > numCups) {
            System.out.println("Yes, I can make that amount of coffee (and even " + (numPossibleCups - numCups) + " more than that)");
        } else {
            System.out.println("No, I can only make " + numPossibleCups + " cup(s) of coffee");
        }
        
    }
}
