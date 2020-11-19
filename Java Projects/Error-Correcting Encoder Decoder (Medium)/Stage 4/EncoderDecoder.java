package correcter;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void encode(String fileFrom, String fileTo) {
        System.out.println(fileFrom);
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            byte[] bytes = Files.readAllBytes(Paths.get(fileFrom));
            textView(bytes);
            hexView(bytes);
            binView(bytes);
            bytes = encode(bytes);
            System.out.println();
            System.out.println(fileTo);
            fileOutputStream.write(bytes);
            expandView(bytes);
            binView(bytes);
            hexView(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static byte[] encode(byte[] bytes) {
        //First step: build a list to represent a queue of all bits in bytes
        ArrayList<Boolean> bitList = new ArrayList<>();
        for (byte b : bytes)
            for (int i = 7; i >= 0; --i) {
                if (((b >>> i) & 1) == 1)
                    bitList.add(true);
                else
                    bitList.add(false);
            }

        //At this point, bitList holds a representation of all bits
        ArrayList<Byte> byteList = new ArrayList<>();
        for (int i = 0; i < bitList.size(); i += 3) {
            StringBuilder encodedByte = new StringBuilder();
            int sum = 0;
            for (int j = i; j - i < 3; ++j) {
                if (j < bitList.size() && bitList.get(j)) {
                    ++sum;
                    encodedByte.append("11");
                } else
                    encodedByte.append("00");
            }
            if (sum % 2 == 0)
                encodedByte.append("00");
            else
                encodedByte.append("11");
            byteList.add((byte) Integer.parseInt(encodedByte.toString(), 2));
        }
        byte[] temp = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); ++i)
            temp[i] = byteList.get(i);
        return temp;
    }

    public static void send(String fileFrom, String fileTo) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            byte[] bytes = Files.readAllBytes(Paths.get(fileFrom));
            System.out.println(fileFrom);
            hexView(bytes);
            binView(bytes);
            System.out.println();
            for (int i = 0; i < bytes.length; ++i)
                bytes[i] = modifyByOneBit(bytes[i]);
            fileOutputStream.write(bytes);
            System.out.println(fileTo);
            binView(bytes);
            hexView(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void decode(String fileFrom, String fileTo) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileTo)) {
            byte[] bytes = Files.readAllBytes(Paths.get(fileFrom));
            System.out.println(fileFrom);
            hexView(bytes);
            binView(bytes);
            System.out.println();

            //Decode bits
            ArrayList<Boolean> bits = new ArrayList<>();
            for (byte b : bytes) {
                boolean[] byteBits = new boolean[8];
                for (int i = 0; i < 8; ++i)
                    byteBits[i] = ((b >>> 7 - i) & 1) == 1;
                int modifiedBit = 0;
                ArrayList<Boolean> unModifiedBits = new ArrayList<>();
                for (int i = 0; i < 4; ++i)
                    if (byteBits[i * 2] != byteBits[i * 2 + 1])
                        modifiedBit = i;
                    else
                        unModifiedBits.add(byteBits[i * 2]);
                if (modifiedBit == 3)
                    bits.addAll(unModifiedBits);
                else {
                    boolean modBit;
                    if (byteBits[7] == true) {
                        //Either one or three set bits
                        if (unModifiedBits.get(0) == unModifiedBits.get(1))
                            modBit = true;
                        else
                            modBit = false;
                    } else {
                        //Zero or two set bits
                        if (unModifiedBits.get(0) == unModifiedBits.get(1))
                            modBit = false;
                        else
                            modBit = true;
                    }
                    for (int i = 0; i < 3; ++i) {
                        if (i == modifiedBit)
                            bits.add(modBit);
                        else
                            bits.add(byteBits[i * 2]);
                    }
                }
            }

            //Assemble decoded bytes
            byte[] decodedBytes = new byte[bits.size() / 8];
            int index = 0;
            for (int i = 0; i < bits.size(); i += 8) {
                StringBuilder sb = new StringBuilder();
                for (int j = i; j - i < 8 && j < bits.size(); ++j)
                    sb.append(bits.get(j) ? '1' : '0');
                if (index < decodedBytes.length) {
                    decodedBytes[index] = (byte) Integer.parseInt(sb.toString(), 2);
                    ++index;
                }
            }

            //Write out
            fileOutputStream.write(decodedBytes);
            System.out.println(fileTo);
            binView(decodedBytes);
            hexView(decodedBytes);
            textView(decodedBytes);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void textView(byte[] bytes) {
        System.out.println("text view: " + new String(bytes));
    }

    public static void hexView(byte[] bytes) {
        System.out.print("hex view: ");
        for (byte b : bytes) {
            System.out.print(String.format("%02x ", b));
        }
        System.out.println();
    }

    public static void binView(byte[] bytes) {
        System.out.print("bin view: ");
        for (byte b : bytes)
            //From stackoverflow
            System.out.print(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1) + " ");
        System.out.println();
    }

    public static void expandView(byte[] bytes) {
        System.out.print("expand: ");
        for (byte b : bytes)
            //From stackoverflow
            System.out.print(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1).substring(0, 6) + ".. ");
        System.out.println();
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

    private static byte modifyByOneBit(byte c) {
        Random r = new Random();
        return (byte) (c ^ (1 << r.nextInt(8)));
    }
}
