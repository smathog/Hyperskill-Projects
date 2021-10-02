package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {
    private final static int SCALE = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String input = scanner.nextLine();
            if ("/exit".equals(input)) {
                break;
            } else {
                String[] nums = input.split(" ");
                final int source_base = Integer.parseInt(nums[0]);
                final int target_base = Integer.parseInt(nums[1]);
                while (convert(source_base, target_base, scanner)) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    private static boolean convert(int source_base, int target_base, Scanner scanner) {
        System.out.printf("Enter a number in base %d to convert to base %d (To go back type /back) ",
                source_base, target_base);
        String input = scanner.nextLine();
        if ("/back".equals(input)) {
            return false;
        } else {
            String[] parts = input.split("\\.");

            // parts[0] always exists, so just convert that:
            BigInteger intPart = new BigInteger(parts[0], source_base);
            StringBuilder sb = new StringBuilder(intPart.toString(target_base));

            // if parts[1] exists, convert from source_base to target_base:
            if (parts.length == 2) {
                String target = parts[1].substring(0, SCALE);
                sb.append(convertFromDecimal(target_base, convertToDecimal(source_base, target)));
            }

            System.out.printf("Conversion result: %s%n", sb);
            return true;
        }
    }

    private static double convertToDecimal(int source_base, String target) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < target.length(); ++i) {
            BigDecimal numerator = new BigDecimal(Character.getNumericValue(target.charAt(i)));
            total = total.add(numerator.multiply(BigDecimal.valueOf(Math.pow(source_base, -(i + 1)))));
        }
        return total.setScale(SCALE, RoundingMode.UP).doubleValue();
    }

    private static String convertFromDecimal(int target_base, double target) {
        StringBuilder rep = new StringBuilder(".");
        for (int i = 0; i < 5; ++i) {
               double temp = target * target_base;
               rep.append(Integer.toString( (int) temp, target_base));
               target = temp - (int) temp;
        }
        return rep.toString();
    }
}
