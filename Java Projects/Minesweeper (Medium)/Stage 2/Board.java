package minesweeper;

import java.util.Random;

public class Board {
    private final Random r;
    private final char[][] grid;
    private final int nRows;
    private final int nCols;
    private final Integer numMines;

    public Board(int nRows, int nCols, Integer numMines, Integer seed) {
        if (seed == null) {
            r = new Random();
        } else {
            r = new Random(seed);
        }
        grid = new char[nRows][nCols];
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
                        grid[i][j] = 'X';
                    else
                        grid[i][j] = '.';
        } else {
            for (int i = 0; i < nRows; ++i)
                for (int j = 0; j < nCols; ++j)
                    grid[i][j] = '.';
            int counter = 0;
            while (counter < numMines) {
                int row = r.nextInt(nRows);
                int col = r.nextInt(nCols);
                if (grid[row][col] != 'X') {
                    grid[row][col] = 'X';
                    ++counter;
                }
            }
        }
    }

    public void printBoard() {
        for (char[] row : grid) {
            for (char c : row)
                System.out.print(c);
            System.out.println("");
        }
    }
}
