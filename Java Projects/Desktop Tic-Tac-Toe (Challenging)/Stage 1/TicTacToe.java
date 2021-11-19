package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicTacToe extends JFrame {
    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setSize(550, 550);

        //Add buttons
        for (String num : List.of("3", "2", "1")) {
            for (String alpha : List.of("A", "B", "C")) {
                JButton button = new JButton(alpha + num);
                button.setName("Button" + alpha + num);
                add(button);
            }
        }

        //Add layout
        setLayout(new GridLayout(3, 3));
        setVisible(true);
    }
}