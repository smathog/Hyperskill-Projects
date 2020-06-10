package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        int radix = scanner.nextInt();
        String indicator;
        if (radix == 2) {
            indicator = new String("0b");
        } else if (radix == 8) {
            indicator = new String("0");
        } else {
            indicator = new String("0x");
        }
        System.out.println(indicator + Long.toString(num, radix));
    }
}
