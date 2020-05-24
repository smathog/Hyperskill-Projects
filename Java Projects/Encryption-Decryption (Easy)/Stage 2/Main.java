package encryptdecrypt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int shift = scanner.nextInt();
        for (int i = 0; i < input.length(); i++) {
            if (isEnglishChar(input.charAt(i))) {
                System.out.print(engAlphabet.charAt((input.charAt(i) - 'a' + shift) % engAlphabet.length()));
            } else {
                System.out.print(input.charAt(i));
            }
        }
    }

    private static boolean isEnglishChar(char c) {
        return (('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z'));
    }
    
    private static String engAlphabet = "abcdefghijklmnopqrstuvwxyz";
}

