package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextEditor extends JFrame {
    public TextEditor() {
        //Main text field
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setSize(250, 250);
        JScrollPane scrollWrapper = new JScrollPane(textArea);
        scrollWrapper.setName("ScrollPane");
        scrollWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollWrapper, BorderLayout.CENTER);

        //Top information text field
        JTextField textField = new JTextField(30);
        textField.setName("FilenameField");

        //Action listeners
        ActionListener saveAction = actionEvent -> {
            try ( FileWriter fw = new FileWriter(textField.getText());
                  BufferedWriter bw = new BufferedWriter(fw)) {
                System.out.println(textArea.getText());
                bw.write(textArea.getText());
            } catch (IOException e) {
                System.out.println("Error writing to file!");
                System.out.println(e.getMessage());
            }
        };

        ActionListener loadAction = actionEvent -> {
            try {
                textArea.setText(Files.readString(Path.of(textField.getText())));
            } catch (IOException e) {
                System.out.println("Error loading file!");
                System.out.println(e.getMessage());
                textArea.setText("");
            }
        };

        ActionListener exitAction = actionEvent -> System.exit(0);

        //Top information panel buttons
        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.addActionListener(saveAction);
        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.addActionListener(loadAction);
        JPanel topPanel = new JPanel();
        topPanel.add(textField);
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        this.add(topPanel, BorderLayout.NORTH);

        //MenuBar settings
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        menuBar.add(fileMenu);
        JMenuItem load = new JMenuItem("Load");
        load.setName("MenuLoad");
        load.addActionListener(loadAction);
        JMenuItem save = new JMenuItem("Save");
        save.setName("MenuSave");
        save.addActionListener(saveAction);
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("MenuExit");
        exit.addActionListener(exitAction);
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        this.setJMenuBar(menuBar);

        //JFrame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setVisible(true);
        setTitle("Java Swing Text Editor");
    }
}
