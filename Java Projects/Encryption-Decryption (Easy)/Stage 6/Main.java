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
        String alg = null;
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
            } else if ("-alg".equals(args[i])) {
                alg = args[i + 1];
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
        if (alg == null) {
            alg = "shift";
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
        State localState;
        if ("dec".equals(mode)) {
            localState = State.DECRYPT;
        } else {
            localState = State.ENCRYPT;
        }
        String output;
        Algorithm algorithm;
        if ("shift".equals(alg)) {
            algorithm = new ShiftAlgorithm(source, key, localState);
        } else {
            algorithm = new UnicodeAlgorithm(source, key, localState);
        }
        output = algorithm.executeAlgorithm();


        //Output
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
}

enum State {
    ENCRYPT,
    DECRYPT
}

abstract class Algorithm {
    protected String data;
    protected int shift;
    protected State status;

    public Algorithm(String data, int shift, State status) {
        this.data = data;
        this.shift = shift;
        this.status = status;
    }

    public abstract String decrypt();
    public abstract String encrypt();

    public String executeAlgorithm() {
        if (status == State.DECRYPT) {
            return decrypt();
        } else if (status == State.ENCRYPT) {
            return encrypt();
        } else {
            return null;
        }
    }
}

class UnicodeAlgorithm extends Algorithm {
    public UnicodeAlgorithm(String data, int shift, State status) {
        super(data, shift, status);
    }

    @Override
    public String decrypt() {
        char[] builder = new char[data.length()];
        for (int i = 0; i < data.length(); i++) {
            builder[i] = ((char) (data.charAt(i) - shift));
        }
        return new String(builder);
    }

    @Override
    public String encrypt() {
        char[] builder = new char[data.length()];
        for (int i = 0; i < data.length(); i++) {
            builder[i] = ((char) (data.charAt(i) + shift));
        }
        return new String(builder);
    }
}

class ShiftAlgorithm extends Algorithm {
    public ShiftAlgorithm(String data, int shift, State status) {
        super(data, shift, status);
    }

    @Override
    public String decrypt() {
        char[] builder = new char[data.length()];
        for (int i = 0; i < data.length(); i++) {
            char currentChar = data.charAt(i);
            if ('A' <= currentChar && currentChar <= 'Z') {
                builder[i] = (char) (Math.floorMod(currentChar - 'A' - shift, 26) + 'A');
            } else if ('a' <= currentChar && currentChar <= 'z') {
                builder[i] = (char) (Math.floorMod(currentChar - 'a' - shift, 26) + 'a');
            } else {
                builder[i] = currentChar;
            }
        }
        return new String(builder);
    }

    @Override
    public String encrypt() {
        char[] builder = new char[data.length()];
        for (int i = 0; i < data.length(); i++) {
            char currentChar = data.charAt(i);
            if ('A' <= currentChar && currentChar <= 'Z') {
                builder[i] = (char) (((currentChar - 'A' + shift) % 26) + 'A');
            } else if ('a' <= currentChar && currentChar <= 'z') {
                builder[i] = (char) (((currentChar - 'a' + shift) % 26) + 'a');
            } else {
                builder[i] = currentChar;
            }
        }
        return new String(builder);
    }
}

