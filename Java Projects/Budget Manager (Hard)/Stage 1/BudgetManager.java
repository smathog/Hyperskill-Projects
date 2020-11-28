package budget;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.*;

public class BudgetManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern costPattern = Pattern.compile("(?<=\\$)\\d+(\\.\\d+)?");
    private final ArrayList<String> expenseList;

    public BudgetManager() {
        expenseList = new ArrayList<>();
    }

    public void countExpenses() {
        while (scanner.hasNextLine())
            expenseList.add(scanner.nextLine());
        double totalCost = 0d;
        for (String s : expenseList) {
            System.out.println(s);
            var m = costPattern.matcher(s);
            m.find();
            totalCost += Double.parseDouble(m.group());
        }
        System.out.println();
        System.out.println("Total: $" + String.format("%,.2f", totalCost));
    }
}
