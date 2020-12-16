package banking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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

    public void addIncome(Card card, int amount) {
        int newBalance = card.getBalance() + amount;
        card.setBalance(amount);
        String sql = "UPDATE card SET balance = ? WHERE number = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newBalance);
            ps.setString(2, card.getCardNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding income to database");
            System.out.println(e.getMessage());
        }
    }

    public void doTransfer(Card card, Scanner scanner) {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String cardNumber = scanner.nextLine();
        if (!cardList.containsKey(cardNumber)) {
            if (Integer.parseInt(cardNumber.substring(cardNumber.length() - 1)) != Card.generateChecksum(cardNumber.substring(0, cardNumber.length() - 1)))
                System.out.println("Probably you made a mistake in the card number. Please try again!");
            else
                System.out.println("Such a card does not exist.");
            return;
        } else if (cardNumber.equals(card.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amount = Integer.parseInt(scanner.nextLine());
        if (amount > card.getBalance()) {
            System.out.println("Not enough money!");
        } else {
            String sql1 = "UPDATE card SET balance = balance - ? WHERE number = ?;";
            String sql2 = "UPDATE card SET balance = balance + ? WHERE number = ?;";
            try (PreparedStatement ps1 = connection.prepareStatement(sql1);
                 PreparedStatement ps2 = connection.prepareStatement(sql2)) {
                connection.setSavepoint();
                connection.setAutoCommit(false);
                ps1.setInt(1, amount);
                ps1.setString(2, card.getCardNumber());
                ps1.executeUpdate();
                ps2.setInt(1, amount);
                ps2.setString(2, cardNumber);
                ps2.executeUpdate();
                connection.setAutoCommit(true);
                cardList.get(cardNumber).setBalance(cardList.get(cardNumber).getBalance() + amount);
                cardList.get(card.getCardNumber()).setBalance(card.getBalance() - amount);
                System.out.println("Success!");
            } catch (SQLException e) {
                System.out.println("Error in transferring funds!");
                System.out.println(e.getMessage());
                try {
                    connection.rollback();
                } catch (SQLException e2) {
                    System.out.println("Error in rollback:");
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public void closeAccount(Card card) {
        cardList.remove(card.getCardNumber());
        String sql = "DELETE FROM card WHERE number = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, card.getCardNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error closing account!");
            System.out.println(e.getMessage());
        }
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
