package minesweeper;

import java.util.Random;

public class Board {
    private final Random r;
    private final char[][] grid;
    private final int nRows;
    private final int nCols;

    public Board(int nRows, int nCols, Integer seed) {
        if (seed == null) {
            r = new Random();
        } else {
            r = new Random(seed);
        }
        grid = new char[nRows][nCols];
        this.nRows = nRows;
        this.nCols = nCols;
        generateGrid();
    }

    private void generateGrid() {
        for (int i = 0; i < nRows; ++i)
            for (int j = 0; j < nCols; ++j)
                if (r.nextInt(4) == 0)
                    grid[i][j] = 'X';
                else
                    grid[i][j] = '.';
    }

    public void printBoard() {
        for (char[] row : grid) {
            for (char c : row)
                System.out.print(c);
            System.out.println("");
        }
    }
}
