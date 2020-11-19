package correcter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*
        EncoderDecoder.binView("Test".getBytes());
        EncoderDecoder.binView(EncoderDecoder.hamming74Encode("Test".getBytes()));
         */

        Scanner scanner = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String mode = scanner.nextLine();
        System.out.println();
        switch (mode) {
            case "encode":
                EncoderDecoder.encode("send.txt", "encoded.txt", EncoderDecoder::hamming74Encode);
                break;
            case "send":
                EncoderDecoder.send("encoded.txt","received.txt");
                break;
            case "decode":
                EncoderDecoder.decode("received.txt", "decoded.txt", EncoderDecoder::hamming74Decode);
            default:
                break;
        }
    }
}
