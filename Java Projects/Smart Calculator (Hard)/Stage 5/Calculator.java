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
                else if (parseSingleArgument(args[0]))
                    break executionLoop;
            } else
                try {
                    System.out.println(operate(args));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid expression");
                }
        }
    }

    boolean parseSingleArgument(String arg) {
        //If command
        if (arg.matches("\\/.*")) {
            if ("/help".equals(arg))
                System.out.println("The program calculates the sum and difference of numbers");
            else if ("/exit".equals(arg)) {
                System.out.println("Bye!");
                return true;
            } else
                System.out.println("Unknown command");
        } else {
            try {
                System.out.println(Integer.parseInt(arg));
            } catch (NumberFormatException e) {
                System.out.println("Invalid expression");
            }
        }
        return false;
    }

    public String[] processString(String line) {
        line = line.replaceAll("\\s+", " ");
        line = line.replaceAll("\\++", "+");
        line = line.replaceAll("(--)+-", "-");
        line = line.replaceAll("(--)+", "+");
        return line.split(" ");
    }

    public int operate(String[] toProcess) throws NumberFormatException {
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
