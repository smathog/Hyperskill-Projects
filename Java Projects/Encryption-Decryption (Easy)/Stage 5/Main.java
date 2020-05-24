package encryptdecrypt;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
 
public class Main {
    public static void main(String[] args) {
        //Parse and handle arguments
        String mode = null;
        String keyString = null;
        String data = null;
        String out = null;
        String in = null;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-mode".equals(args[i])) {
                mode = args[i + 1];
            } else if ("-key".equals(args[i])) {
                keyString = args[i + 1];
            } else if ("-data".equals(args[i])) {
                data = args[i + 1];
            } else if ("-in".equals(args[i])) {
                in = args[i + 1];
            } else if ("-out".equals(args[i])) {
                out = args[i + 1];
            }
        }
        if (mode == null || keyString == null || (data == null && in == null)) {
            System.out.println("Error! Invalid input to main");
        }
        if (mode == null) {
            mode = "enc";
        }
        int key = 0;
        if (keyString != null) {
            key = Integer.parseInt(keyString);
        }
        if (data == null && in == null) {
            data = "";
        }
 
        //===Operations===
 
        //Obtain source string to encrypt or decrypt
        String source = data;
        if (data == null && in != null) {
            //Need to obtain source from in file.
            try {
                source = readFileAsString(in);
            } catch (IOException e) {
                System.out.println("Error: invalid file input " + e.getMessage());
            }
        }
 
        //Build encrypted or decrypted string
        String output;
        if ("dec".equals(mode)) {
            output = decrypt(source, key);
        } else {
            output = encrypt(source, key);
        }
        if (out == null) {
            System.out.println(output);
        } else {
            try (FileWriter writer = new FileWriter(out)) {
                writer.write(output);
            } catch (IOException e) {
                System.out.println("Error: invalid file for output " + e.getMessage());
            }
        }
    }
 
    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
 
    private static String decrypt(String input, int shift) {
        return encrypt(input, -1 * shift);
    }
 
    private static String encrypt(String input, int shift) {
        char[] builder = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            builder[i] = ((char) (input.charAt(i) + shift));
        }
        return new String(builder);
    }
 
    private static boolean isEnglishChar(char c) {
        return (('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z'));
    }
    
    private static String engAlphabet = "abcdefghijklmnopqrstuvwxyz";
}
