package encryptdecrypt;

public class Main {
    public static void main(String[] args) {
        //Handle command line arguments
        String mode = "enc";
        int key = 0;
        String data = "";
        for (int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i])) {
                if (i + 1 < args.length &&
                        !"-key".equals(args[i + 1]) &&
                        !"-data".equals(args[i + 1])) {
                    if ("dec".equals(args[i + 1])) {
                        mode = "dec";
                    }
                }
            } else if ("-key".equals(args[i])) {
                if (i + 1 < args.length &&
                        !"-mode".equals(args[i + 1]) &&
                        !"-data".equals(args[i + 1])) {
                    key = Integer.parseInt(args[i + 1]);
                }
            } else if ("-data".equals(args[i])) {
                if (i + 1 < args.length &&
                        !"-key".equals(args[i + 1]) &&
                        !"-data".equals(args[i + 1])) {
                    data = args[i + 1];
                }
            }
        }

        //Execute
        if ("enc".equals(mode)) {
            encrypt(data, key);
        } else {
            decrypt(data, key);
        }
    }

    private static void decrypt(String input, int shift) {
        encrypt(input, -1 * shift);
    }

    private static void encrypt(String input, int shift) {
        for (int i = 0; i < input.length(); i++) {
            System.out.print(((char) (input.charAt(i) + shift)));
        }
    }

    private static boolean isEnglishChar(char c) {
        return (('a' <= c && c <= 'z') ||
                ('A' <= c && c <= 'Z'));
    }
    
    private static String engAlphabet = "abcdefghijklmnopqrstuvwxyz";
}

