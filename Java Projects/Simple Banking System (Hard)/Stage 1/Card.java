package banking;

import java.util.Random;

public class Card {
    //Class variables
    private static final String MII = "4";
    private static final String IIN = "00000";

    //Instance variables
    public final String cardNumber;
    private final int pin;
    private int amount;

    public Card() {
        Random r = new Random();
        this.pin = r.nextInt(10000);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; ++i)
            sb.append(r.nextInt(10));
        cardNumber = MII + IIN + sb.toString() + "1";
        amount = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public int getBalance() {
        return amount;
    }
}
