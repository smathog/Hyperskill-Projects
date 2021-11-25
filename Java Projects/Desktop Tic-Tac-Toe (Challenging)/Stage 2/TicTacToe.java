package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends JFrame {
    private final JLabel statusLabel;
    private final ArrayList<JButton> buttons;

    private GameManager gameManager;

    public TicTacToe() {
        // Create a gameManager
        gameManager = new GameManager();

        // Boilerplate for JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setSize(550, 650);
        setLayout(new BorderLayout());

        // Create panel for the board
        JPanel boardPanel = new JPanel();
        // Add buttons to board panel
        buttons = new ArrayList<>();
        int row = 0;
        for (String num : List.of("3", "2", "1")) {
            int col = 0;
            for (String alpha : List.of("A", "B", "C")) {
                JButton button = new JButton(" ");
                button.setName("Button" + alpha + num);
                button.setFocusPainted(false);
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(new Update(e -> {
                    gameManager.attemptMove(finalRow, finalCol)
                            .ifPresent(c -> button.setText(c.toString()));
                }));
                buttons.add(button);
                boardPanel.add(button);
                col++;
            }
            row++;
        }
        // Add layout to board panel
        boardPanel.setLayout(new GridLayout(3, 3));
        // Add panel to JFrame
        add(boardPanel, BorderLayout.CENTER);
        boardPanel.setPreferredSize(new Dimension(550, 550));

        // Create panel for bottom
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        // Add components
        statusLabel = new JLabel(gameManager.getStatus());
        statusLabel.setName("LabelStatus");
        bottom.add(statusLabel, BorderLayout.WEST);
        statusLabel.setPreferredSize(new Dimension(450, 100));
        JButton reset = new JButton("Reset");
        reset.setFocusPainted(false);
        reset.setName("ButtonReset");
        reset.addActionListener(new Update(e -> {
            gameManager = new GameManager();
            for (var button : buttons) {
                button.setText(" ");
            }
        }));
        bottom.add(reset, BorderLayout.EAST);
        reset.setPreferredSize(new Dimension(100, 100));
        // Add panel to JFrame
        add(bottom, BorderLayout.SOUTH);
        bottom.setPreferredSize(new Dimension(550, 100));

        // Make everything in JFrame visible
        setVisible(true);
    }

    // Customized, "decorated" ActionListener that updates the statusLabel whenever invoked
    private class Update implements ActionListener {
        private final ActionListener listener;

        public Update(ActionListener listener) {
            this.listener = listener;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            listener.actionPerformed(e);
            statusLabel.setText(gameManager.getStatus());
        }
    }
}