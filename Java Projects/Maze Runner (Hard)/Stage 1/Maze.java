package maze;

import java.util.Random;

public class Maze {
    private boolean[][] maze;

    public Maze(int rows, int columns) {
        maze = new boolean[rows][columns];
        maze[1][0] = true;
        maze[8][9] = true;
        for (int i = 1; i < 9; ++i)
            for (int c = 1; c < 9; ++c)
                maze[i][c] = true;
    }

    public int numRows() {
        return maze.length;
    }

    public int numCols() {
        return maze[0].length;
    }

    public boolean pathAt(int row, int col) {
        return maze[row][col];
    }

    public static void printMaze(Maze m) {
        for (int row = 0; row < m.numRows(); ++row) {
            for (int col = 0; col < m.numCols(); ++col)
                System.out.print(m.pathAt(row, col) ? path() : wall());
            System.out.println();
        }
    }

    private static String wall() {
        return "\u2588\u2588";
    }

    private static String path() {
        return "  ";
    }
}
