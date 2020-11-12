package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board b = new Board(new Scanner(System.in));
        b.setUpBoard();
    }
}
