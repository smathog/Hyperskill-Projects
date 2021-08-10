package numbers;

import java.text.MessageFormat;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.function.IntPredicate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a natural number: ");
        int num = Integer.parseInt(scanner.nextLine());
        if (num > 0) {
            LinkedHashMap<String, IntPredicate> propertiesMap = new LinkedHashMap<>();
            propertiesMap.put("even", Main::isEven);
            propertiesMap.put("odd", Main::isOdd);
            propertiesMap.put("buzz", Main::isBuzz);
            propertiesMap.put("duck", Main::isDuck);
            System.out.println(MessageFormat.format("Properties of {0}", num));
            propertiesMap.forEach((key, value) -> System.out.println(MessageFormat.format("{0}: {1}", key, value.test(num))));
        } else {
            System.out.println("This number is not natural!");
        }
    }

    public static boolean isEven(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a natural number", num));
        } else {
            return num % 2 == 0;
        }
    }

    public static boolean isOdd(int num) {
        return !isEven(num);
    }

    public static boolean isBuzz(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a natural number", num));
        } else {
            return num % 7 == 0 || num % 10 == 7;
        }
    }

    public static boolean isDuck(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a natural number", num));
        } else {
            return Integer.toString(num).chars().anyMatch(c -> (char) c == '0');
        }
    }
}
