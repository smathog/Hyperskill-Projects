package correcter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileOutputStream;

public class EncoderDecoder {
    public static String oneErrorPerThreeCharacters(String input) {
        Random r = new Random();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i += 3)
            for (int j = i; j - i < 3 && j < chars.length; ++j)
                if (r.nextInt(3) == 0 || j - i == 2) {
                    chars[j] = randomChar(chars[j]);
                    break;
                }
        return new String(chars);
    }

    public static String tripleEncode(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray())
            sb.append(String.valueOf(c).repeat(3));
        return sb.toString();
    }

    public static String tripleDecode(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i += 3) {
            //Don't need to worry about running off end b/c must be multiple of 3
            if (input.charAt(i) == input.charAt(i + 1) || input.charAt(i) == input.charAt(i + 2))
                sb.append(input.charAt(i));
            else
                sb.append(input.charAt(i + 1));
        }
        return sb.toString();
    }

    public static void bitError(String fileIn, String fileOut) {
        File toRead = new File(fileIn);
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileOut)) {
            byte[] bytes = Files.readAllBytes(toRead.toPath());
            for (int index = 0; index < bytes.length; ++index)
                bytes[index] = modifyByOneBit(bytes[index]);
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Returns some random character *except* not
    private static char randomChar(char not) {
        char temp = not;
        while (temp == not) {
            Random r = new Random();
            int selection = r.nextInt(63);
            if (selection < 26)
                temp = (char) ('a' + selection);
            else if (selection < 52)
                temp = (char) ('A' + (selection - 26));
            else if (selection == 52)
                temp = ' ';
            else
                temp = (char) ('0' + (selection - 53));
        }
        return temp;
    }

    private static char modifyByOneBit(char c) {
        Random r = new Random();
        return (char) (c ^ (1 << r.nextInt(8)));
    }

    private static byte modifyByOneBit(byte c) {
        Random r = new Random();
        return (byte) (c ^ (1 << r.nextInt(8)));
    }
}
