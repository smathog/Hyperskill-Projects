package minesweeper;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private final Random r;
    private final int[][] grid;
    private final boolean[][] explored;
    private final int nRows;
    private final int nCols;
    private final Integer numMines;
    private int numSafe;
    private int numExplored;

    public Board(int nRows, int nCols, Integer numMines, Integer seed) {
        if (seed == null) {
            r = new Random();
        } else {
            r = new Random(seed);
        }
        grid = new int[nRows][nCols];
        explored = new boolean[nRows][nCols];
        this.nRows = nRows;
        this.nCols = nCols;
        this.numMines = numMines;
        this.numExplored = 0;
        generateGrid(null);
    }

    private void generateGrid(Integer numMines) {
        if (numMines == null) {
            for (int i = 0; i < nRows; ++i)
                for (int j = 0; j < nCols; ++j)
                        grid[i][j] = 0;
        } else {
            int counter = 0;
            while (counter < numMines) {
                int row = r.nextInt(nRows);
                int col = r.nextInt(nCols);
                if (grid[row][col] != -1 && grid[row][col] != -10 && grid[row][col] != -3) {
                    if (grid[row][col] == -2)
                        grid[row][col] = -3;
                    else
                        grid[row][col] = -1;
                    ++counter;
                }
            }
        }
        for (int i = 0; i < nRows; ++i)
            for (int j = 0; j < nCols; ++j)
                if (grid[i][j] != -1 && grid[i][j] != -3) {
                    int numNeighboringMines = getNumberNeighboringMines(i, j);
                    if (numNeighboringMines == 0) {
                        if (grid[i][j] == -10)
                            grid[i][j] = -4;
                        else if (grid[i][j] == -2)
                            grid[i][j] = -2;
                        else
                            grid[i][j] = 0;
                    } else {
                        if (grid[i][j] == -10)
                            grid[i][j] = numNeighboringMines;
                        else if (grid[i][j] == -2)
                            grid[i][j] = numNeighboringMines + 20;
                        else
                            grid[i][j] = numNeighboringMines + 10;
                    }
                }

    }

    public void playBoard(Scanner scanner) {
        int numMarkers = 0;
        int numMinesMarked = 0;
        boolean firstFree = false;
        printPlayerVisibleBoard(false);
        while (true) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            int c = scanner.nextInt() - 1;
            int r = scanner.nextInt() - 1;
            String command = scanner.nextLine().trim();
            if ("free".equals(command)) {
                //first time free is used:
                if (!firstFree) {
                    firstFree = true;
                    grid[r][c] = -10;
                    generateGrid(numMines);
                }
                if (!exploreFrom(r, c)) {
                    printPlayerVisibleBoard(true);
                    System.out.println("You stepped on a mine and failed!");
                    break;
                }
                if (numExplored == numSafe) {
                    printPlayerVisibleBoard(false);
                    System.out.println("Congratulations! You found all the mines!");
                    break;
                }
            } else { //"mine".equals(command)
                if (grid[r][c] == 0) {
                    grid[r][c] = -2;
                    ++numMarkers;
                } else if (grid[r][c] == -1) {
                    grid[r][c] = -3;
                    ++numMinesMarked;
                    ++numMarkers;
                } else if (grid[r][c] < 20 && grid[r][c] > 10) {
                    grid[r][c] += 10;
                    ++numMarkers;
                } else if (grid[r][c] == -2) {
                    grid[r][c] = 0;
                    --numMarkers;
                } else if (grid[r][c] == -3) {
                    grid[r][c] = -1;
                    --numMarkers;
                    --numMinesMarked;
                } else if (grid[r][c] > 20) {
                    grid[r][c] -= 10;
                    --numMarkers;
                }

                if (numMarkers == numMinesMarked && numMinesMarked == numMines) {
                    printPlayerVisibleBoard(false);
                    System.out.println("Congratulations! You found all the mines!");
                    break;
                }
            }
            System.out.println();
            printPlayerVisibleBoard(false);
            //printBoard();
        }
    }

    private boolean exploreFrom(int row, int col) {
        //If stepped on a mine, just end; game over
        if (grid[row][col] == -1 || grid[row][col] == -3)
            return false;
        //Explore using BFS
        explored[row][col] = true;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.offerLast(new int[]{row, col});
        while (!queue.isEmpty()) {
            int[] xy = queue.pollFirst();
            ++numExplored;
            int r = xy[0];
            int c = xy[1];
            //First, load queue with eligible neighbors for exploration
            if (!(grid[r][c] > 0)) {
                for (int i = Math.max(r - 1, 0); i < Math.min(nRows, r + 2); ++i)
                    for (int j = Math.max(c - 1, 0); j < Math.min(nCols, c + 2); ++j)
                        if (!explored[i][j] && !(r == i && c == j) && !(grid[i][j] == -1 || grid[i][j] == -3)) {
                            explored[i][j] = true;
                            queue.offerLast(new int[]{i, j});
                        }
            }
            //Now, process grid[r][c]
            if (grid[r][c] > 20)
                grid[r][c] -= 20;
            else if (grid[r][c] > 10)
                grid[r][c] -= 10;
            else if (grid[r][c] == 0 || grid[r][c] == -2)
                grid[r][c] = -4;
        }
        return true;
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
                System.out.print(c);
            }
            System.out.println("");
        }
    }

    private void printPlayerVisibleBoard(boolean revealMines) {
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
                if (grid[i][j] == -1 || grid[i][j] == 0 || (grid[i][j] < 20 && grid[i][j] > 10)) {
                    if (revealMines && grid[i][j] == -1)
                        System.out.print('X');
                    else
                        System.out.print(".");
                    //-2 for empty marked, -3 for mine marked
                } else if (grid[i][j] == -2 || grid[i][j] == -3 || grid[i][j] > 20) {
                    if (revealMines && grid[i][j] == -3)
                        System.out.print('X');
                    else
                        System.out.print("*");
                } else if (grid[i][j] == -4)
                    System.out.print('/');
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

//Number codes
//n > 0: corresponds to an explored cell with n neighboring mines
// 0: corresponds to an unexplored, unmarked cell with no neighboring mines
//-1: corresponds to an unexplored, unmarked cell containing a mine
//-2: corresponds to an unexplored, marked cell with no neighboring mines
//-3: corresponds to an unexplored, marked cell containing a mine
//-4: corresponds to an explored cell without a neighboring mine
//-10: corresponds to the first used explore, special value
//n + 10 > 0: corresponds to an unexplored cell with n neighboring mines
//n + 20 > 0: corresponds to an unexplored, marked cell with n neighboring mines
