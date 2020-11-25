package maze;

import java.util.HashSet;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.stream.*;

public class Maze {
    private boolean[][] maze;
    private static Random r = new Random();

    public Maze(int rows, int columns) {
        generateMazePrim(rows, columns);
    }

    public void generateMazePrim(int rows, int columns) {
        if (rows < 3)
            throw new IllegalArgumentException("Must have at least three rows!");
        if (columns < 3)
            throw new IllegalArgumentException("Must have at least three columns!");

        final int r = rows ;
        final int c = columns;
        Coordinate[][] grid = new Coordinate[r][c];
        for (int i = 0; i < r; ++i)
            for (int j = 0 ; j < c; ++j)
                grid[i][j] = new Coordinate(i, j);
        PriorityQueue<Coordinate> wallQueue = new PriorityQueue<>();



        Coordinate first = grid[this.r.nextInt(r - 2) + 1][this.r.nextInt(c - 2) + 1];
        first.isWall = false;
        first.visited = true;

        for (int i = Math.max(first.x - 1, 0); i < Math.min(first.x + 2, r); ++i)
            for (int j = Math.max(first.y - 1, 0); j < Math.min(first.y + 2, c); ++j)
                if (grid[i][j].isWall && (i == first.x || j == first.y)) {
                    grid[i][j].visited = true;
                    grid[i][j].visitedBy = first;
                    wallQueue.add(grid[i][j]);
                }

        boolean entranceFormed = false;
        boolean exitFormed = false;

        while (!wallQueue.isEmpty()) {
            /*
            //Testing
            System.out.println();
            printCoordinateGrid(grid);
            System.out.println();
             */

            Coordinate current = wallQueue.poll();
            //Special rules: if first to one of the sides, note and prohibit future expansion
            if (current.y == 0) {
                if (!entranceFormed) {
                    entranceFormed = true;
                    current.isWall = false;
                }
                continue;
            }
            if (current.y == c - 1) {
                if (!exitFormed) {
                    exitFormed = true;
                    current.isWall = false;
                }
                continue;
            }

            Coordinate visitedBy = current.visitedBy;
            int x;
            int y;
            if (current.x == visitedBy.x) {
                x = current.x;
                y = current.y + (current.y - visitedBy.y);
            } else { //must be same y
                y = current.y;
                x = current.x + (current.x - visitedBy.x);
            }
            //Validate coordinate
            if (x < 0 || y < 0 || x >= r || y >= c)
                continue;

            //Special rule: ignore wall cells on top and bottom
            if (x == 0 || x == r - 1)
                continue;

            //Special rule: only one entrance/exit on left/right respectively
            if ((y == 0 && entranceFormed) || (y == c - 1 && exitFormed))
                continue;

            //Valid coordinate
            //Check if cell visited
            if (!grid[x][y].visited) {
                //if grid[x][y] is either first entrance or exit, note it
                if (y == 0)
                    entranceFormed = true;
                else if (y == c - 1)
                    exitFormed = true;
                grid[x][y].visited = true;
                grid[x][y].isWall = false;
                current.isWall = false;
                //Add relevant walls
                for (int i = Math.max(grid[x][y].x - 1, 0); i < Math.min(grid[x][y].x + 2, r); ++i)
                    for (int j = Math.max(grid[x][y].y - 1, 0); j < Math.min(grid[x][y].y + 2, c); ++j)
                        if ((i == grid[x][y].x || j == grid[x][y].y) && grid[i][j].isWall && !grid[i][j].visited) {
                            grid[i][j].visited = true;
                            grid[i][j].visitedBy = grid[x][y];
                            wallQueue.add(grid[i][j]);
                        }
                for (int i = Math.max(current.x - 1, 0); i < Math.min(current.x + 2, r); ++i)
                    for (int j = Math.max(current.y - 1, 0); j < Math.min(current.y + 2, c); ++j)
                        if ((i == current.x || j == current.y) && grid[i][j].isWall && !grid[i][j].visited) {
                            grid[i][j].visited = true;
                            grid[i][j].visitedBy = current;
                            wallQueue.add(grid[i][j]);
                        }
            }

            //Translate grid to maze
            maze = new boolean[rows][columns];
            for (int i = 0; i < rows; ++i)
                for (int j = 0; j < columns; ++j)
                    maze[i][j] = !grid[i][j].isWall;
        }

        //Inner grid done at this point, so add outer walls and punch access holes

    }

    public void generateMazeFromBooleanArray(boolean[][] grid) {
        maze = grid;
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

    public void solveMaze() {
        Coordinate[] openings = findOpenings();
        Coordinate[][] grid = new Coordinate[numRows()][numCols()];
        for (int i = 0; i < numRows(); ++i)
            for (int j = 0; j < numCols(); ++j) {
                grid[i][j] = new Coordinate(i, j);
                grid[i][j].visited = false;
                grid[i][j].isWall = !maze[i][j];
                grid[i][j].visitedBy = null;
            }

        ArrayDeque<Coordinate> queue = new ArrayDeque<>();
        HashSet<Coordinate> path = new HashSet<>();
        queue.offerLast(openings[0]);

        SearchLoop:
        while (true) {
            Coordinate current = queue.pollFirst();
            Coordinate[] neighbors = findNeighbors(grid, current.x, current.y);
            for (Coordinate n : neighbors) {
                if (n.equals(openings[1])) {
                    //Backtrace
                    Coordinate previous = current;
                    path.add(n);
                    do {
                        path.add(previous);
                        previous = previous.visitedBy;
                    } while (!previous.equals(openings[0]));
                    path.add(previous);
                    break SearchLoop;
                } else if (!n.isWall && !n.visited) {
                    n.visited = true;
                    n.visitedBy = current;
                    queue.offerLast(n);
                }
            }
        }

        //Print
        for (Coordinate[] arr: grid) {
            for (Coordinate c : arr)
                if (path.contains(c))
                    System.out.print("//");
                else
                    System.out.print(maze[c.x][c.y] ? path() : wall());
            System.out.println();
        }
    }

    private Coordinate[] findOpenings() {
        Coordinate opening1 = null;
        Coordinate opening2 = null;

        //Check vertical sides
        for (int i = 0; i < numRows(); ++i) {
            if (maze[i][0])
                if (opening1 == null)
                    opening1 = new Coordinate(i, 0);
                else {
                    opening2 = new Coordinate(i, 0);
                    break;
                }
            if (maze[i][numCols() - 1])
                if (opening1 == null)
                    opening1 = new Coordinate(i, numCols() - 1);
                else {
                    opening2 = new Coordinate(i, numCols() - 1);
                    break;
                }
        }

        //check top and bottom
        if (opening1 == null || opening2 == null) {

            for (int i = 0; i < numCols(); ++i) {
                if (maze[0][i])
                    if (opening1 == null)
                        opening1 = new Coordinate(0, i);
                    else {
                        opening2 = new Coordinate(0, i);
                        break;
                    }
                if (maze[numRows() - 1][i])
                    if (opening1 == null)
                        opening1 = new Coordinate(numRows() - 1, i);
                    else {
                        opening2 = new Coordinate(numRows() - 1, i);
                        break;
                    }
            }
        }

        return new Coordinate[]{opening1, opening2};
    }

    private Coordinate[] findNeighbors(Coordinate[][] grid, int x, int y) {
        if (grid == null)
            throw new IllegalArgumentException("Bad coordinate passed!");

        ArrayList<Coordinate> list = new ArrayList<>();

        //North
        if (x > 0)
            list.add(grid[x - 1][y]);
        //East
        if (y < grid[0].length - 1)
            list.add(grid[x][y + 1]);
        //South
        if (x < grid.length)
            list.add(grid[x + 1][y]);
        //West
        if (y > 0)
            list.add(grid[x][y - 1]);

        return list.toArray(new Coordinate[list.size()]);
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

    private static void printCoordinateGrid(Coordinate[][] temp) {
        for (int i = 0; i < temp.length; ++i) {
            for (int j = 0; j < temp[0].length; ++j)
                System.out.print(temp[i][j].isWall ? wall() : path());
            System.out.println();
        }
    }

    private class Coordinate implements Comparable<Coordinate> {
        private int x;
        private int y;
        private boolean visited;
        private boolean isWall;
        private int priority;
        private Coordinate visitedBy;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
            visited = false;
            isWall = true;
            priority = r.nextInt(11);
            visitedBy = null;
        }

        @Override
        public int hashCode() {
            //Cantor's enumeration:
             return (x + y) * (x + y + 1) / 2 + x;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            else if (o.getClass() != this.getClass())
                return false;
            else {
                Coordinate other = (Coordinate) o;
                return this.x == other.x && this.y == other.y;
            }
        }

        @Override
        public String toString() {
            return x + " - " + y;
        }

        @Override
        public int compareTo(Coordinate o) {
            return o.priority - this.priority;
        }
    }
}
