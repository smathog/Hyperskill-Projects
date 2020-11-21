package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOfLife extends JFrame {
    private JLabel generationLabel;
    private JLabel aliveLabel;
    private JPanel[][] grid;
    private Thread executingThread = null;
    private LifeController lc;

    public GameOfLife() {
        super("Game Of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Handle components
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setName("InfoPanel");
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 600, 0));
        add(infoPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setName("ButtonPanel");
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        infoPanel.add(buttonPanel, BorderLayout.NORTH);

        JToggleButton playToggleButton = new JToggleButton();
        playToggleButton.setName("PlayToggleButton");
        playToggleButton.setText("Pause");
        ActionListener toggleListener = e ->{
            if (playToggleButton.isSelected()) {
                if (executingThread != null) {
                    try {
                        lc.pause();
                        executingThread.join();
                    } catch (InterruptedException except) {
                        System.out.println(except.getMessage());
                    }
                }
                playToggleButton.setText("Resume");
            }
            else {
                if (executingThread != null) {
                    executingThread = new Thread(() -> lc.execute());
                    executingThread.start();
                }
                playToggleButton.setText("Pause");
            }
        };
        playToggleButton.addActionListener(toggleListener);
        buttonPanel.add(playToggleButton, BorderLayout.WEST);

        JButton resetButton = new JButton();
        resetButton.setName("ResetButton");
        resetButton.setText("Reset");
        resetButton.addActionListener(e -> {
            //if there is a current executing thread, stop it first
            if (executingThread != null) {
                try {
                    lc.pause();
                    executingThread.join();
                } catch (InterruptedException except) {
                    System.out.println(except.getMessage());
                }
                //if was paused, reset
                if (playToggleButton.isSelected()) {
                    playToggleButton.addActionListener(null);
                    playToggleButton.setSelected(false);
                    playToggleButton.setText("Pause");
                    playToggleButton.addActionListener(toggleListener);
                }
            } else {
                //do nothing; no input yet
                return;
            }
            //Reset the model.
            lc.reset();
            //Restart execution
            executingThread = new Thread(() -> lc.execute());
            executingThread.start();
        });
        buttonPanel.add(resetButton, BorderLayout.EAST);

        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Generation: #" + "0");
        generationLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(generationLabel, BorderLayout.CENTER);

        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: 0");
        aliveLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
        infoPanel.add(aliveLabel, BorderLayout.SOUTH);

        grid = null;

        setVisible(true);
    }

    public void getController(LifeController lc) {
        this.lc = lc;
    }

    public void setExecutingThread(Thread executingThread) {
        this.executingThread = executingThread;
    }

    public void update(LifeModel model) {
        //Needs to be done every time update called, so leave outside if statement
        generationLabel.setText("Generation #" + model.getCurrentGeneration());
        aliveLabel.setText("Alive: " + model.numAlive());

        if (grid == null) {
            grid = new JPanel[model.getSize()][model.getSize()];
            JPanel gridPanel = new JPanel();
            gridPanel.setLayout(new GridLayout(model.getSize(), model.getSize(), -1, -1));
            gridPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            for (int r = 0; r < model.getSize(); ++r) {
                for (int c = 0; c < model.getSize(); ++c) {
                    grid[r][c] = new JPanel();
                    grid[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    if (model.isAlive(r, c))
                        grid[r][c].setBackground(Color.BLACK);
                    gridPanel.add(grid[r][c]);
                }
            }
            add(gridPanel, BorderLayout.CENTER);
        } else {
            for (int r = 0; r < model.getSize(); ++r)
                for (int c = 0; c < model.getSize(); ++c)
                    if (model.isAlive(r, c)) {
                        grid[r][c].setBackground(Color.BLACK);
                    }
                    else
                        grid[r][c].setBackground(null);

        }

    }
}
