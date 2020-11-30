package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? ");
        int num = Integer.parseInt(scanner.nextLine());
        Board b = new Board(9, 9, num, null);
        b.playBoard(scanner);
    }
}
