package banking;

import java.util.Random;
import java.util.stream.IntStream;

public class Card {
    //Class variables
    private static final String MII = "4";
    private static final String IIN = "00000";

    //Instance variables
    private final String accountNumber;
    private final int checkSum;
    private final String pin;
    private int amount;

    public Card() {
        Random r = new Random();
        StringBuilder pb = new StringBuilder();
        for (int i = 0; i < 4; ++i)
            pb.append(r.nextInt(10));
        this.pin = pb.toString();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; ++i)
            sb.append(r.nextInt(10));
        accountNumber = sb.toString();
        checkSum = generateChecksum(MII + IIN + accountNumber);
        amount = 0;
    }

    public Card(String cardNumber, String pin, int amount) {
        this.pin = pin;
        this.amount = amount;
        accountNumber = cardNumber.substring(6, cardNumber.length() - 1);
        checkSum = Integer.parseInt(cardNumber.substring(cardNumber.length() - 1));
    }

    public String getCardNumber() {
        return MII + IIN + accountNumber + checkSum;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return amount;
    }

    public void setBalance(int amount) {
        this.amount = amount;
    }

    public static int generateChecksum(String str) {
        int num = IntStream.range(0, str.length())
                .map(i -> {
                    int c = Integer.parseInt(String.valueOf(str.charAt(i)));
                    if ((i + 1) % 2 == 1)
                        return c * 2;
                    else
                        return c;
                })
                .map(i -> i > 9 ? i - 9 : i)
                .sum() % 10;
        if (num == 0)
            return 0;
        else
            return 10 - num;
    }
}
