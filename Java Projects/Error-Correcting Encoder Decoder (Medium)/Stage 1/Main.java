package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(oneErrorPerThreeCharacters(scanner.nextLine()));
    }

    private static String oneErrorPerThreeCharacters(String input) {
        Random r = new Random();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i += 3)
            for (int j = i; j - i < 3 && j < chars.length; ++j)
                if (r.nextInt(3) == 0 || j - i == 2) {
                    chars[j] = randomChar();
                    break;
                }
        return new String(chars);

    }

    private static char randomChar() {
        Random r = new Random();
        int selection = r.nextInt(63);
        if (selection < 26)
            return (char) ('a' + selection);
        else if (selection < 52)
            return (char) ('A' + (selection - 26));
        else if (selection == 52)
            return ' ';
        else
            return (char) ('0' + (selection - 53));
    }
}
