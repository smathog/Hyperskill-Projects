package calculator;

import java.util.*;

public class Calculator {
    private static Scanner scanner = new Scanner(System.in);

    public Calculator() {
    }

    public void execute() {
        executionLoop:
        while (true) {
            String[] args = scanner.nextLine().split(" ");
            if (args.length == 1) {
                if (args[0] == null || args[0].isEmpty())
                    continue;
                else if ("/help".equals(args[0])) {
                    System.out.println("The program calculates the sum of numbers");
                } else if ("/exit".equals(args[0])) {
                    System.out.println("Bye!");
                    break executionLoop;
                } else {
                    System.out.println(getSum(args));
                }
            } else {
                System.out.println(getSum(args));
            }
        }
    }

    public int getSum(String[] args) {
        return Arrays.stream(args)
                .mapToInt(Integer::parseInt)
                .sum();
    }
}
