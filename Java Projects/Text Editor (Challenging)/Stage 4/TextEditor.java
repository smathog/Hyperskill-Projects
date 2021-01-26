package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JPanel topPanel;
    private JCheckBox useRegex;
    private JCheckBoxMenuItem regex;
    private JFileChooser jFileChooser;

    private JTextField searchField;
    private java.util.List<MatchIndices> startIndices;
    private int currentIndex;

    //Action listeners
    private final ActionListener saveAction = actionEvent -> {
        if (jFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(jFileChooser.getSelectedFile().getAbsolutePath());
                 BufferedWriter bw = new BufferedWriter(fw)) {
                System.out.println(textArea.getText());
                bw.write(textArea.getText());
            } catch (IOException e) {
                System.out.println("Error writing to file!");
                System.out.println(e.getMessage());
            }
        }
    };

    private final ActionListener loadAction = actionEvent -> {
        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                textArea.setText(Files.readString(Path.of(jFileChooser.getSelectedFile().getAbsolutePath())));
            } catch (IOException e) {
                System.out.println("Error loading file!");
                System.out.println(e.getMessage());
                textArea.setText("");
            }
        }
    };

    private final ActionListener exitAction = actionEvent -> System.exit(0);

    private final ActionListener searchAction = actionEvent -> {
        new SearchWorker().execute();
    };

    private final ActionListener previousFind = actionEvent -> {
        getMatch(false);
    };

    private final ActionListener nextFind = actionEvent -> {
        getMatch(true);
    };

    private final ActionListener setRegex = actionEvent -> {
        if (useRegex.isSelected())
            regex.setState(true);
        else
            regex.setState(false);
    };

    private final ActionListener setRegexMenu = actionEvent -> {
        if (regex.isSelected())
            useRegex.setSelected(true);
        else
            useRegex.setSelected(false);
    };

    public TextEditor() {
        jFileChooser = new JFileChooser();
        jFileChooser.setName("FileChooser");
        this.add(jFileChooser);

        //Set up main text area
        initTextField();

        JPanel topPanel = new JPanel();
        //Set up file control buttons
        initFileButtons();
        //Set up search functionality and buttons
        initSearchButtons();

        //Set up MenuBar
        initMenuBar();

        //JFrame (this) settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setVisible(true);
        setTitle("Java Swing Text Editor");
    }

    private void initTextField() {
        //Main text field
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setSize(250, 250);
        JScrollPane scrollWrapper = new JScrollPane(textArea);
        scrollWrapper.setName("ScrollPane");
        scrollWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollWrapper, BorderLayout.CENTER);
    }

    private void initFileButtons() {
        //Top information panel buttons
        if (topPanel == null)
            topPanel = new JPanel();
        JButton saveButton = new JButton(new ImageIcon("C:\\Users\\Scott\\Desktop\\Text Editor\\Text Editor\\task\\src\\editor\\Resources\\baseline_save_black_18dp.png"));
        saveButton.setName("SaveButton");
        JButton loadButton = new JButton(new ImageIcon("C:\\Users\\Scott\\Desktop\\Text Editor\\Text Editor\\task\\src\\editor\\Resources\\baseline_backup_black_18dp.png"));
        loadButton.setName("OpenButton");
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        this.add(topPanel, BorderLayout.NORTH);
        saveButton.addActionListener(saveAction);
        loadButton.addActionListener(loadAction);
    }

    private void initSearchButtons() {
        if (topPanel == null)
            topPanel = new JPanel();
        searchField = new JTextField(20);
        searchField.setName("SearchField");
        JButton startSearchButton = new JButton(new ImageIcon("C:\\Users\\Scott\\Desktop\\Text Editor\\Text Editor\\task\\src\\editor\\Resources\\baseline_find_in_page_black_18dp.png"));
        startSearchButton.addActionListener(searchAction);
        startSearchButton.setName("StartSearchButton");
        JButton previousButton = new JButton(new ImageIcon("C:\\Users\\Scott\\Desktop\\Text Editor\\Text Editor\\task\\src\\editor\\Resources\\baseline_arrow_back_black_18dp.png"));
        previousButton.addActionListener(previousFind);
        previousButton.setName("PreviousMatchButton");
        JButton nextButton = new JButton(new ImageIcon("C:\\Users\\Scott\\Desktop\\Text Editor\\Text Editor\\task\\src\\editor\\Resources\\baseline_arrow_forward_black_18dp.png"));
        nextButton.addActionListener(nextFind);
        nextButton.setName("NextMatchButton");
        this.useRegex = new JCheckBox("Use Regex");
        this.useRegex.addActionListener(setRegex);
        this.useRegex.setName("UseRegExCheckbox");
        topPanel.add(searchField);
        topPanel.add(startSearchButton);
        topPanel.add(previousButton);
        topPanel.add(nextButton);
        topPanel.add(useRegex);
    }

    private void initMenuBar() {
        //MenuBar settings
        JMenuBar menuBar = new JMenuBar();

        //File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        menuBar.add(fileMenu);
        JMenuItem load = new JMenuItem("Open");
        load.setName("MenuOpen");
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

        //Search menu
        JMenu searchMenu = new JMenu("Search");
        menuBar.add(searchMenu);
        searchMenu.setName("MenuSearch");
        JMenuItem search = new JMenuItem("Start Search");
        search.addActionListener(searchAction);
        search.setName("MenuStartSearch");
        JMenuItem previous = new JMenuItem("Previous Match");
        previous.addActionListener(previousFind);
        previous.setName("MenuPreviousMatch");
        JMenuItem next = new JMenuItem("Next Match");
        next.addActionListener(nextFind);
        next.setName("MenuNextMatch");
        regex = new JCheckBoxMenuItem("Use Regex");
        regex.addActionListener(setRegexMenu);
        regex.setName("MenuUseRegExp");
        searchMenu.add(search);
        searchMenu.add(previous);
        searchMenu.add(next);
        searchMenu.add(regex);

        this.setJMenuBar(menuBar);
    }

    private synchronized java.util.List<MatchIndices> getStartIndices() {
        return startIndices;
    }

    private synchronized void setStartIndices(java.util.List<MatchIndices> startIndices) {
        this.startIndices = startIndices;
        currentIndex = 0;
        displayCurrentMatch();
    }

    private synchronized void displayCurrentMatch() {
        if (getStartIndices() == null || getStartIndices().isEmpty()) {
            //Do nothing
        } else {
            MatchIndices indices = getStartIndices().get(currentIndex);
            textArea.setCaretPosition(indices.getEnd());
            textArea.select(indices.getStart(), indices.getEnd());
            textArea.grabFocus();
        }
    }

    //displays the previous or next match, provided it exists
    private synchronized void getMatch(boolean next) {
        if (startIndices != null && !startIndices.isEmpty()) {
            if (next) {
                currentIndex = Math.floorMod(++currentIndex, startIndices.size());
                displayCurrentMatch();

            } else {
                currentIndex = Math.floorMod(--currentIndex, startIndices.size());
                displayCurrentMatch();
            }
        }
    }

    private class SearchWorker extends SwingWorker<java.util.List<MatchIndices>, Object> {
        @Override
        protected void done() {
            try {
                setStartIndices(get());
            } catch (Exception e) {
                System.out.println("Error in SearchWorker!");
                System.out.println(e.getMessage());
            }
        }

        @Override
        protected java.util.List<MatchIndices> doInBackground() throws Exception {
            boolean useRegex = TextEditor.this.useRegex.isSelected();
            Matcher matcher;
            if (useRegex)
                matcher = Pattern.compile(searchField.getText()).matcher(textArea.getText());
            else
                matcher = Pattern.compile(Pattern.quote(searchField.getText())).matcher(textArea.getText());
            ArrayList<MatchIndices> indexList = new ArrayList<>();
            while (matcher.find())
                indexList.add(new MatchIndices(matcher.start(), matcher.end()));
            return indexList;
        }
    }

    private class MatchIndices {
        private final int start;
        private final int end;

        public MatchIndices(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }
}
