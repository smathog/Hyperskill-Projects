package converter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number in decimal system: ");
        int num = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter target base: ");
        int radix = Integer.parseInt(scanner.nextLine());
        System.out.printf("Conversion result: %s%n", Integer.toString(num, radix));
    }
}
