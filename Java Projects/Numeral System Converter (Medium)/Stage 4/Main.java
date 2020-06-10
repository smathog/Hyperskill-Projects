package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int inRadix = scanner.nextInt();
        scanner.nextLine();
        String num = scanner.nextLine();
        int outRadix = scanner.nextInt();
        int decimal = inRadix == 1 ? num.length() : Integer.parseInt(num, inRadix);
        System.out.println(outRadix == 1 ? "1".repeat(decimal): Integer.toString(decimal, outRadix));
    }
}
