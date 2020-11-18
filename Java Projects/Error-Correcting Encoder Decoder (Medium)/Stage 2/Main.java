package correcter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        System.out.println(message);
        String encoded = EncoderDecoder.tripleEncode(message);
        System.out.println(encoded);
        String sent = EncoderDecoder.oneErrorPerThreeCharacters(encoded);
        System.out.println(sent);
        String decoded = EncoderDecoder.tripleDecode(sent);
        System.out.println(decoded);
    }
}
