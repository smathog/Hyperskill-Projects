package converter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        mainloop:
        while (true) {
            System.out.print("Do you want to convert /from decimal or /to decimal? (To quit type /exit)");
            String choice = scanner.nextLine();
            switch (choice) {
                case "/from":
                    fromDecimal(scanner);
                    break;
                case "/to":
                    toDecimal(scanner);
                    break;
                case "/exit":
                    break mainloop;
                default:
                    break;
            }
            System.out.println();
        }
    }

    private static void fromDecimal(Scanner scanner) {
        System.out.print("Enter number in decimal system: ");
        int num = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter target base: ");
        int radix = Integer.parseInt(scanner.nextLine());
        System.out.printf("Conversion result: %s%n", Integer.toString(num, radix));
    }

    private static void toDecimal(Scanner scanner) {
        System.out.print("Enter source number: ");
        String num = scanner.nextLine();
        System.out.print("Enter source base: ");
        int base = Integer.parseInt(scanner.nextLine());
        System.out.printf("Conversion to decimal result: %d%n", Integer.valueOf(num, base));
    }
}
