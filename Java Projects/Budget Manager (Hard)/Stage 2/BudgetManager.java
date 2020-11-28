package budget;

import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.*;
import java.util.stream.*;

public class BudgetManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern costPattern = Pattern.compile("(?<=\\$)\\d+(\\.\\d+)?");
    private final HashMap<String, Double> expenses;
    private double currentIncome;
    private double currentExpenses;

    public BudgetManager() {
        expenses = new HashMap<>();
        menuLoop();
    }

    private void menuLoop() {
        loop:
        while (true) {
            System.out.println("Choose your action: ");
            System.out.println("1) Add income");
            System.out.println("2) Add purchase");
            System.out.println("3) Show list of purchases");
            System.out.println("4) Balance");
            System.out.println("0) Exit");
            String choice = scanner.nextLine();
            System.out.println();
            switch (choice) {
                case "1":
                    addIncome();
                    break;
                case "2":
                    addPurchase();
                    break;
                case "3":
                    showPurchases();
                    break;
                case "4":
                    showBalance();
                    break;
                case "0":
                    break loop;
                default:
                    break;
            }
            System.out.println();
        }
        System.out.println("Bye!");
    }

    private void addIncome() {
        System.out.println("Enter income : ");
        currentIncome += Integer.parseInt(scanner.nextLine());
        System.out.println("Income was added!");
    }

    private void addPurchase() {
        System.out.println("Enter purchase name: ");
        String purchase = scanner.nextLine();
        System.out.println("Enter its price: ");
        double price = Double.parseDouble(scanner.nextLine());
        expenses.put(purchase, price);
        currentExpenses += price;
        System.out.println("Purchase was added!");
    }

    private void showPurchases() {
        if (expenses.isEmpty())
            System.out.println("Purchase list is empty");
        else
            expenses.forEach((k, v) -> System.out.println(k + " $" + String.format("%,.2f", v)));
    }

    private void showBalance() {
        System.out.println("Balance: $" + String.format("%,.2f", currentIncome - currentExpenses));
    }
}
