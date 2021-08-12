package numbers;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.function.Predicate;

public class Main {
    private static final LinkedHashMap<String, Predicate<BigInteger>> propertiesMap = new LinkedHashMap<>();

    // Set up the properties map by associating properties with method references
    static {
        propertiesMap.put("even", Main::isEven);
        propertiesMap.put("odd", Main::isOdd);
        propertiesMap.put("buzz", Main::isBuzz);
        propertiesMap.put("duck", Main::isDuck);
        propertiesMap.put("palindromic", Main::isPalindromic);
        propertiesMap.put("gapful", Main::isGapful);
    }


    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println();
        mainMenu();
    }

    public static void mainMenu() {
        System.out.println("Supported requests: ");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println("  * the first parameter represents a starting number;");
        System.out.println("  * the second parameter shows how many consecutive numbers are to be printed;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.print("Enter a request: ");
            String[] input = scanner.nextLine().split(" ");
            System.out.println();
            // Handle case where only a single number is input:
            if (input.length == 1) {
                BigInteger num = new BigInteger(input[0]);
                if (!evaluateProperties(num, false)) {
                    break;
                }
            } else {
                if (Integer.parseInt(input[1]) <= 0) {
                    System.out.println("The second parameter should be a natural number.");
                    continue;
                }
                int counter = 0;
                while (counter < Integer.parseInt(input[1])) {
                    BigInteger num = new BigInteger(input[0]).add(BigInteger.valueOf(counter));
                    evaluateProperties(num, true);
                    ++counter;
                }
            }
        }
    }

    // Return true if num is negative or positive, false if 0 (signals trigger to end program)
    public static boolean evaluateProperties(BigInteger num, boolean isSequential) {
        if (num.compareTo(BigInteger.ZERO) < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            return true;
        } else if (num.compareTo(BigInteger.ZERO) == 0 && !isSequential) {
            System.out.println("Goodbye!");
            return false;
        } else {
            if (isSequential) {
                var properties = propertiesMap.entrySet().stream()
                        .filter(e -> e.getValue().test(num))
                        .map(Map.Entry::getKey)
                        .toArray(String[]::new);
                String sb = MessageFormat.format("{0} is ", num) +
                        String.join(", ", properties);
                System.out.println(sb);
            } else {
                System.out.println(MessageFormat.format("Properties of {0}", num));
                propertiesMap.forEach((key, val) -> System.out.println(
                        MessageFormat.format("{0}: {1}", key, val.test(num))));
            }
            return true;
        }
    }

    public static boolean isEven(BigInteger num) {
        return num.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }

    public static boolean isOdd(BigInteger num) {
        return !isEven(num);
    }

    public static boolean isBuzz(BigInteger num) {
        return num.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO)
                || num.mod(BigInteger.TEN).equals(BigInteger.valueOf(7));
    }

    public static boolean isDuck(BigInteger num) {
        return num.toString().chars().anyMatch(c -> (char) c == '0');
    }

    public static boolean isPalindromic(BigInteger num) {
        String digits = num.toString();
        for (int i = 0, j = digits.length() - 1; i < j; ++i, --j) {
            if (digits.charAt(i) != digits.charAt(j)) {
                    return false;
            }
        }
        return true;
    }

    public static boolean isGapful(BigInteger num) {
        if (num.compareTo(new BigInteger("100")) >= 0) {
            BigInteger concat = new BigInteger(String.valueOf(num.toString().charAt(0))
                    + num.toString().charAt(num.toString().length() - 1));
            return num.mod(concat).equals(BigInteger.ZERO);
        } else {
            return false;
        }
    }
}
