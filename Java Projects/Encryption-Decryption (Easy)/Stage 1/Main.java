package encryptdecrypt;

public class Main {
    public static void main(String[] args) {
        String message = "we found a treasure!";
        char[] messageArray = message.toCharArray();
        for (char c: messageArray) {
            if (Character.isAlphabetic(c)) {
                c = (char) ('z' - (c - 'a'));
            }
            System.out.print(c);
        }
    }
}

