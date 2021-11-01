package viewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLiteViewer extends JFrame {

    private String[] tables;
    private JTextField fileNameTextField;
    private JButton openFileButton;
    private JComboBox<String> comboBox;
    private JTextArea textArea;
    private JButton executeButton;

    public SQLiteViewer() {
        //Basic Frame Setup
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        //Initial table setup
        tables = new String[]{};

        //Add components
        addFileNameTextField();
        addOpenFileButton();
        addTableBox();
        addQueryArea();
        addExecuteButton();

        //Make everything visible:
        setVisible(true);
    }

    private void addFileNameTextField() {
        fileNameTextField = new JTextField();
        fileNameTextField.setBounds(20, 20, 580, 30);
        fileNameTextField.setName(textFieldName);
        this.add(fileNameTextField);
    }

    private void addOpenFileButton() {
        openFileButton = new JButton("Open");
        openFileButton.setBounds(620, 20, 80, 30);
        openFileButton.setName(openButtonName);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = fileNameTextField.getText();
                String sqlURL = "jdbc:sqlite:" + fileName;
                SQLiteDataSource dataSource = new SQLiteDataSource();
                dataSource.setUrl(sqlURL);
                try (Connection connection = dataSource.getConnection()) {
                    System.out.println("Connected to database...");
                    try (Statement statement = connection.createStatement()) {
                        System.out.println("Statement created...");
                        try (ResultSet results = statement.executeQuery(
                                "SELECT name " +
                                "FROM sqlite_master " +
                                "WHERE type ='table' " +
                                "AND name NOT LIKE 'sqlite_%';\n")) {
                            ArrayList<String> tableNames = new ArrayList<>();
                            while (results.next()) {
                                tableNames.add(results.getString(1));
                            }
                            tables = tableNames.toArray(String[]::new);
                            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(tables);
                            comboBox.setModel(model);
                            comboBox.setSelectedIndex(0);
                            System.out.println("Updated model...");
                            System.out.println("Tables: ");
                            System.out.println(Arrays.toString(tables));
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

            }
        });
        this.add(openFileButton);
    }

    private void addTableBox() {
        comboBox = new JComboBox<>(tables);
        comboBox.setBounds(20, 70, 680, 30);
        comboBox.setName(tableName);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String table = (String) comboBox.getSelectedItem();
                String query = String.format("SELECT * FROM %s;", table);
                textArea.setText(query);
                System.out.println("Query: " + query);
            }
        });
        this.add(comboBox);
    }

    private void addQueryArea() {
        textArea = new JTextArea();
        textArea.setBounds(20, 120, 510, 200);
        textArea.setName(queryTextName);
        this.add(textArea);
    }

    private void addExecuteButton() {
        executeButton = new JButton("Execute");
        executeButton.setBounds(550, 120, 150, 30);
        executeButton.setName(executeButtonName);
        this.add(executeButton);
    }

    private static final String title = "SQLite Viewer";
    private static final String textFieldName = "FileNameTextField";
    private static final String openButtonName = "OpenFileButton";
    private static final String tableName = "TablesComboBox";
    private static final String queryTextName = "QueryTextArea";
    private static final String executeButtonName = "ExecuteQueryButton";
}
