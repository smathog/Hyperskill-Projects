package minesweeper;

import java.util.Random;

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

    public void printBoard() {
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
}
