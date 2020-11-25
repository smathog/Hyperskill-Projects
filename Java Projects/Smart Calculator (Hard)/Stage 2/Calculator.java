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
            if (args.length == 1 && "/exit".equals(args[0])) {
                System.out.println("Bye!");
                break executionLoop;
            } else if (args.length == 1 && args[0].isEmpty()) {
                continue;
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
