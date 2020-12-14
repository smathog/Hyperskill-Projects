package banking;

import java.util.HashMap;

public class Backend {
    public static Backend instance;

    private HashMap<String, Card> cardList;

    public static Backend getInstance() {
        if (instance == null)
            instance = new Backend();
        return instance;
    }

    public Card generateCard() {
        Card card;
        do {
            card = new Card();
        } while (!isUniqueNumber(card.getCardNumber()));
        addCard(card);
        return card;
    }

    public Card getCard(String cardNumber) {
        return cardList.getOrDefault(cardNumber, null);
    }

    private Backend() {
        cardList = new HashMap<>();
    }

    private void addCard(Card card) {
        cardList.putIfAbsent(card.getCardNumber(), card);
    }

    private boolean isUniqueNumber(String cardNumber) {
        return !cardList.containsKey(cardNumber);
    }
}
