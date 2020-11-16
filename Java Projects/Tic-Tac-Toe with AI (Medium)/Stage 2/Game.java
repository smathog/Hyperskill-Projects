package tictactoe;

import java.util.Scanner;

public class Game {
    private final Board b;
    private final boolean playComputer;
    private final Computer computer;

    public Game() {
        Scanner scanner = new Scanner(System.in);
        b = new Board(scanner, false);
        playComputer = true;
        computer = new EasyComputer();
    }

    public void play() {
        b.printBoard();
        while (true) {
            try {
                if (playComputer)
                    System.out.println("Making move level \"" + computer.level() + "\"");
                if (b.makeMove(b.getxToMove() || !playComputer ? b.getCoordsFromHuman() : computer.computeMove()))
                    break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
