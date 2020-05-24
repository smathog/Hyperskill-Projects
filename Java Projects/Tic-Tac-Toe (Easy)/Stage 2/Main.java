package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter cells: ");
        String input = scanner.nextLine();
        initBoard(input);
        printBoard();
    }
    
    private static void initBoard(String input) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = input.charAt((i * 3) + j);
            }
        }
    }
    
    private static void printBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.print('-');
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.print('|' + " ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print('|' + " ");
            System.out.println();
        }
        for (int i = 0; i < 9; i++) {
            System.out.print('-');
        }
        System.out.println();
    }
    
    private static Scanner scanner = new Scanner(System.in);
    private static char[][] board = new char[3][3];
}
