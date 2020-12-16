package banking;

import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Backend {
    public static Backend instance;

    private HashMap<String, Card> cardList;
    private Connection connection;

    public static Backend getInstance(String fileName) {
        if (instance == null)
            instance = new Backend(fileName);
        return instance;
    }

    public Card generateCard() {
        Card card;
        do {
            card = new Card();
        } while (!isUniqueNumber(card.getCardNumber()));
        addCard(card);
        String sql = "INSERT INTO card "
                + "(number, pin, balance) "
                + "VALUES"
                + "(" + card.getCardNumber()
                + ", " + card.getPin()
                + ", " + card.getBalance()
                + ");" +
                "";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error inserting card into database!");
            System.out.println(e.getMessage());
        }
        return card;
    }

    public Card getCard(String cardNumber) {
        return cardList.getOrDefault(cardNumber, null);
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection!");
            System.out.println(e.getMessage());
        }
    }

    private Backend(String fileName) {
        initDB(fileName);
        cardList = new HashMap<>();
        try (Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT * FROM card;")) {
            while (results.next()) {
                Card card = new Card(results.getString("number"), results.getString("pin"), results.getInt("balance"));
                cardList.put(card.getCardNumber(), card);
            }
        } catch (SQLException e) {
            System.out.println("Error reading from table!");
            System.out.println(e.getMessage());
        }
    }

    private void addCard(Card card) {
        cardList.putIfAbsent(card.getCardNumber(), card);
    }

    private boolean isUniqueNumber(String cardNumber) {
        return !cardList.containsKey(cardNumber);
    }

    private void initDB(String fileName) {
        connection = Connect.connectToDatabase(fileName);
        String sql =  "CREATE TABLE IF NOT EXISTS card (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " number TEXT, \n"
                + " pin TEXT, \n"
                + " balance INTEGER DEFAULT 0"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing table");
            System.out.println(e.getMessage());
        }
    }
}
