package editor;

import javax.swing.*;

public class TextEditor extends JFrame {
    public TextEditor() {
        JComponent textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setSize(250, 250);
        this.add(textArea);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setVisible(true);
        setTitle("Java Swing Text Editor");
        setLayout(null);
    }
}
