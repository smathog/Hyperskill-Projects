package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String operation = scanner.nextLine();
        String input = scanner.nextLine();
        int shift = scanner.nextInt();
        if ("dec".equals(operation)) {
            shift *= -1;
        }
        for (int i = 0; i < input.length(); i++) {
            System.out.print(charAtShift(input.charAt(i), shift));
        }
    }

    private static char charAtShift(char c, int shift) {
        return (char) (c + shift);
    }

    private static boolean isEnglishChar(char c) {
        return (('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z'));
    }
    
    private static String engAlphabet = "abcdefghijklmnopqrstuvwxyz";
}

