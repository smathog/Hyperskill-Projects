package budget;

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.*;
import java.util.stream.*;

public class BudgetManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern costPattern = Pattern.compile("(?<=\\$)\\d+(\\.\\d+)?");
    private final HashMap<Expense.Type, ArrayList<Expense>> expenses;
    private final HashMap<Expense.Type, Double> expensesByType;
    private double currentIncome;
    private double currentExpenses;

    public BudgetManager() {
        expenses = new HashMap<>();
        expensesByType = new HashMap<>();
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
                    purchaseByType();
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

    private void purchaseByType() {
        while (true) {
            System.out.println("Choose the type of purchase: ");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) Back");
            String choice = scanner.nextLine();
            Expense.Type type = null;
            switch (choice) {
                case "1":
                    type = Expense.Type.FOOD;
                    break;
                case "2":
                    type = Expense.Type.CLOTHES;
                    break;
                case "3":
                    type = Expense.Type.ENTERTAINMENT;
                    break;
                case "4":
                    type = Expense.Type.OTHER;
                    break;
                case "5":
                    return;
            }
            System.out.println();
            addPurchase(type);
            System.out.println();
        }
    }

    private void addPurchase(Expense.Type t) {
        System.out.println("Enter purchase name: ");
        String purchase = scanner.nextLine();
        System.out.println("Enter its price: ");
        double price = Double.parseDouble(scanner.nextLine());
        Expense newPurchase = new Expense(purchase, price, t);
        expenses.putIfAbsent(newPurchase.getType(), new ArrayList<>());
        expenses.get(t).add(newPurchase);
        currentExpenses += price;
        expensesByType.putIfAbsent(t, price + expensesByType.getOrDefault(t, 0d));
        System.out.println("Purchase was added!");
    }

    private void showPurchases() {
        while (true) {
            System.out.println("Choose the type of purchases");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");
            String choice = scanner.nextLine();
            System.out.println();
            Expense.Type t = null;
            switch (choice) {
                case "1":
                    t = Expense.Type.FOOD;
                    break;
                case "2":
                    t = Expense.Type.CLOTHES;
                    break;
                case "3":
                    t = Expense.Type.ENTERTAINMENT;
                    break;
                case "4":
                    t = Expense.Type.OTHER;
                    break;
            }
            switch (choice) {
                case "1":
                case "2":
                case "3":
                case "4":
                    System.out.println(t.name + ": ");
                    if (!expenses.containsKey(t))
                        System.out.println("Purchase list is empty!");
                    else {
                        expenses.get(t).forEach(System.out::println);
                        System.out.println("Total sum: $" + expensesByType.get(t));
                    }
                    break;
                case "5":
                    System.out.println("All: ");
                    if (expenses.isEmpty())
                        System.out.println("Purchase list is empty!");
                    else {
                        expenses.forEach((k, v) -> v.forEach(System.out::println));
                        System.out.println("Total sum: $" + currentExpenses);
                    }
                    break;
                case "6":
                    return;
            }
            System.out.println();
        }
    }

    private void showBalance() {
        System.out.println("Balance: $" + String.format("%,.2f", currentIncome - currentExpenses));
    }
}
