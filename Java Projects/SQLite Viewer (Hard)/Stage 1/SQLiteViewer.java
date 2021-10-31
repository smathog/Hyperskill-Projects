package viewer;

import javax.swing.*;

public class SQLiteViewer extends JFrame {
    public SQLiteViewer() {
        //Basic Frame Setup
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        //Add components
        addFileNameTextField();
        addOpenFileButton();

        //Make everything visible:
        setVisible(true);
    }

    private void addFileNameTextField() {
        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setBounds(0, 0, 600, 30);
        fileNameTextField.setName(textFieldName);
        this.add(fileNameTextField);
    }

    private void addOpenFileButton() {
        JButton openFileButton = new JButton("Open");
        openFileButton.setBounds(600, 0, 100, 30);
        openFileButton.setName(openButtonName);
        this.add(openFileButton);
    }

    private static final String title = "SQLite Viewer";
    private static final String textFieldName = "FileNameTextField";
    private static final String openButtonName = "OpenFileButton";
}
