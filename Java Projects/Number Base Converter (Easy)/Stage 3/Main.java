package converter;

import java.math.BigInteger;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
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
        System.out.printf("Enter a number in base %d to convert to base %d (To go back type /back)",
                source_base, target_base);
        String input = scanner.nextLine();
        if ("/back".equals(input)) {
            return false;
        } else {
            BigInteger num = new BigInteger(input, source_base);
            System.out.printf("Conversion result: %s%n", num.toString(target_base));
            return true;
        }
    }
}
