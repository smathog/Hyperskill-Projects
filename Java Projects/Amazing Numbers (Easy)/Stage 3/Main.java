package numbers;

import java.math.BigInteger;
import java.text.MessageFormat;
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
    }


    public static void main(String[] args) {
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println();
        mainMenu();
    }

    public static void mainMenu() {
        System.out.println("Supported requests: ");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter 0 to exit.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.print("Enter a request: ");
            BigInteger num = new BigInteger(scanner.nextLine());
            System.out.println();
            if (num.compareTo(BigInteger.ZERO) < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
            } else if (num.compareTo(BigInteger.ZERO) == 0) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println(MessageFormat.format("Properties of {0}", num));
                propertiesMap.forEach((key, val) -> System.out.println(
                                MessageFormat.format("{0}: {1}", key, val.test(num))));
            }
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
}
