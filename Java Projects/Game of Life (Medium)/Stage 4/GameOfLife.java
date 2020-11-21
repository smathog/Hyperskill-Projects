package life;

import javax.swing.*;
import java.awt.*;

public class GameOfLife extends JFrame {
    private JLabel generationLabel;
    private JLabel aliveLabel;
    private JPanel[][] grid;

    public GameOfLife() {
        super("Game Of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Handle components
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setName("InfoPanel");
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 10));
        add(infoPanel, BorderLayout.NORTH);

        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Generation: #" + "0");
        generationLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 5));
        infoPanel.add(generationLabel, BorderLayout.NORTH);

        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: 0");
        aliveLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
        infoPanel.add(aliveLabel, BorderLayout.SOUTH);

        grid = null;

        setVisible(true);
    }

    public void update(LifeModel model) {
        //Needs to be done ever time update called, so leave outside if statement
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
                    if (model.isAlive(r, c))
                        grid[r][c].setBackground(Color.BLACK);
                    else
                        grid[r][c].setBackground(null);

        }

    }
}
