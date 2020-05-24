package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter cells: ");
        String input = scanner.nextLine();
        initBoard(input);
        printBoard();
        evaluateBoard();
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

    private static void evaluateBoard() {
        //Determine key numbers
        int numO = 0;
        int numX = 0;
        int numSpace = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'X') {
                    numX++;
                } else if (board[i][j] == 'O') {
                    numO++;
                } else if (board[i][j] == '_') {
                    numSpace++;
                }
            }
        }
        int num3X = 0;
        int num3O = 0;
        for (int i = 0; i < board.length; i++) {
            if (checkRow(i, 'X')) {
                num3X++;
            } else if (checkRow(i, 'O')) {
                num3O++;
            }
        }
        for (int i = 0; i < board.length; i++) {
            if (checkColumn(i, 'X')) {
                num3X++;
            } else if (checkColumn(i, 'O')) {
                num3O++;
            }
        }
        if (checkDiagonal1('X')) {
            num3X++;
        } else if (checkDiagonal1('O')) {
            num3O++;
        }
        if (checkDiagonal2('X')) {
            num3X++;
        } else if (checkDiagonal2('O')) {
            num3O++;
        }

        //State Evaluation
        if ((num3O > 0 && num3X > 0 ) || Math.abs(numO - numX) >= 2) {
            System.out.println("Impossible");
        } else {
            if (num3O == 0 && num3X == 0 && numSpace != 0) {
                System.out.println("Game not finished");
            } else if (num3O == 0 && num3X == 0 && numSpace == 0) {
                System.out.println("Draw");
            } else if (num3O > 0) {
                System.out.println("O wins");
            } else if (num3X > 0) {
                System.out.println("X wins");
            }
        }
    }

    private static boolean checkRow(int row, char c) {
        return (board[row][0] == c && board[row][1] == c && board[row][2] == c);
    }

    private static boolean checkColumn(int column, char c) {
        return (board[0][column] == c && board[1][column] == c && board[2][column] == c);
    }

    private static boolean checkDiagonal1(char c) {
        return (board[0][0] == c && board[1][1] == c && board[2][2] == c);
    }

    private static boolean checkDiagonal2(char c) {
        return (board[2][0] == c && board[1][1] == c && board[0][2] == c);
    }
    
    private static Scanner scanner = new Scanner(System.in);
    private static char[][] board = new char[3][3];
}
