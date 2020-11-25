package calculator;

import java.util.*;

public class Calculator {
    private static Scanner scanner = new Scanner(System.in);

    public Calculator() {
    }

    public void execute() {
        executionLoop:
        while (true) {
            String[] args = processString(scanner.nextLine());
            if (args.length == 1) {
                if (args[0] == null || args[0].isEmpty())
                    continue;
                else if ("/help".equals(args[0])) {
                    System.out.println("The program calculates the sum and difference of numbers");
                } else if ("/exit".equals(args[0])) {
                    System.out.println("Bye!");
                    break executionLoop;
                } else {
                    System.out.println(args[0]);
                }
            } else {
                System.out.println(operate(args));
            }
        }
    }

    public String[] processString(String line) {
        line = line.replaceAll("\\s+", " ");
        line = line.replaceAll("\\++", "+");
        line = line.replaceAll("(--)+-", "-");
        line = line.replaceAll("(--)+", "+");
        return line.split(" ");
    }

    public int operate(String[] toProcess) {
        int sum = 0;
        boolean subtract = false;
        for (int i = 0; i < toProcess.length; ++i) {
            if ("+".equals(toProcess[i])) {
                subtract = false;
            } else if ("-".equals(toProcess[i])) {
                subtract = true;
            } else {
                if (subtract) {
                    sum -= Integer.parseInt(toProcess[i]);
                    subtract = false;
                } else {
                    sum += Integer.parseInt(toProcess[i]);
                }
            }
        }
        return sum;
    }
}
