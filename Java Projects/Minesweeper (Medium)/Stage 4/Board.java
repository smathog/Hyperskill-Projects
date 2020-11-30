package minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private final Random r;
    private final int[][] grid;
    private final int nRows;
    private final int nCols;
    private final Integer numMines;

    public Board(int nRows, int nCols, Integer numMines, Integer seed) {
        if (seed == null) {
            r = new Random();
        } else {
            r = new Random(seed);
        }
        grid = new int[nRows][nCols];
        this.nRows = nRows;
        this.nCols = nCols;
        this.numMines = numMines;
        generateGrid(numMines);
    }

    private void generateGrid(Integer numMines) {
        if (numMines == null) {
            for (int i = 0; i < nRows; ++i)
                for (int j = 0; j < nCols; ++j)
                    if (r.nextInt(4) == 0)
                        grid[i][j] = -1;
                    else
                        grid[i][j] = 0;
        } else {
            for (int i = 0; i < nRows; ++i)
                for (int j = 0; j < nCols; ++j)
                    grid[i][j] = '.';
            int counter = 0;
            while (counter < numMines) {
                int row = r.nextInt(nRows);
                int col = r.nextInt(nCols);
                if (grid[row][col] != -1) {
                    grid[row][col] = -1;
                    ++counter;
                }
            }
        }
        for (int i = 0; i < nRows; ++i)
            for (int j = 0; j < nCols; ++j)
                if (grid[i][j] != -1)
                    grid[i][j] = getNumberNeighboringMines(i, j);

    }

    public void playBoard(Scanner scanner) {
        int numMarkers = 0;
        int numMinesMarked = 0;
        printPlayerVisibleBoard();
        while (true) {
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            int[] coords = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            //Shift to board coords
            int c = --coords[0];
            int r = --coords[1];
            if (grid[r][c] > 0) {
                System.out.println("There is a number here!");
                continue;
            } else {
                if (grid[r][c] == 0) {
                    grid[r][c] = -2;
                    ++numMarkers;
                } else if (grid[r][c] == -1) {
                    grid[r][c] = -3;
                    ++numMarkers;
                    ++numMinesMarked;
                } else if (grid[r][c] == -2) {
                    grid[r][c] = 0;
                    --numMarkers;
                } else {//if (grid[r][c] == -3)
                    grid[r][c] = -1;
                    --numMarkers;
                    --numMinesMarked;
                }
            }
            System.out.println();
            printPlayerVisibleBoard();
            if (numMarkers == numMinesMarked && numMinesMarked == numMines) {
                System.out.println("Congratulations! You found all the mines!");
                break;
            }
        }
    }

    private int getNumberNeighboringMines(int row, int col) {
        if (row < 0 || row >= nRows || col < 0 || col >= nCols )
            throw new IllegalArgumentException("Bad input: " + row + " " + col);
        int count = 0;
        for (int i = Math.max(0, row - 1); i < Math.min(nRows, row + 2); ++i)
            for (int j = Math.max(0, col - 1); j < Math.min(nCols, col + 2); ++j)
                if (!(i == row && j == col) && grid[i][j] == -1)
                    ++count;
        return count;
    }

    private void printBoard() {
        for (int[] row : grid) {
            for (int c : row) {
                if (c == -1)
                    System.out.print('X');
                else if (c == 0)
                    System.out.print('.');
                else
                    System.out.print(c);
            }
            System.out.println("");
        }
    }

    private void printPlayerVisibleBoard() {
        //Top column markers
        System.out.print(" |");
        for (int i = 0; i < nCols; ++i)
            System.out.print(i + 1);
        System.out.println("|");
        //Separator
        System.out.print("-|");
        for (int i = 0; i < nCols; ++i)
            System.out.print("-");
        System.out.println("|");
        //Grid
        for (int i = 0; i < nRows; ++i) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < nCols; ++j) {
                if (grid[i][j] == -1 || grid[i][j] == 0)
                    System.out.print(".");
                //-2 for empty marked, -3 for mine marked
                else if (grid[i][j] == -2 || grid[i][j] == -3)
                    System.out.print("*");
                else
                    System.out.print(grid[i][j]);
            }
            System.out.println("|");
        }
        //Separator
        System.out.print("-|");
        for (int i = 0; i < nCols; ++i)
            System.out.print("-");
        System.out.println("|");
    }
}
