package tictactoe;

import java.util.Scanner;

public class Board {
    private final char[][] grid;
    private int numEmpty;
    private boolean xToMove;
    private final Scanner scanner;

    public Board(Scanner scanner, boolean preload) {
        this.scanner = scanner;
        grid = new char[3][3];
        numEmpty = 0;
        if (preload)
            preloadGrid();
        else {
            for (int i = 0; i < 3; ++i)
                for (int j = 0; j < 3; ++j)
                    grid[i][j] = ' ';
            numEmpty = 9;
            xToMove = true;
        }
    }

    private void preloadGrid() {
        int numX = 0;
        int numY = 0;
        System.out.print("Enter cells: ");
        String boardStatus = scanner.nextLine().trim();
        for (int i = 0; i < boardStatus.length(); ++i)
            if (boardStatus.charAt(i) != '_') {
                grid[i / 3][i % 3] = boardStatus.charAt(i);
                if (boardStatus.charAt(i) == 'X')
                    ++numX;
                else
                    ++numY;
            } else {
                grid[i / 3][i % 3] = ' ';
                ++numEmpty;
            }
        xToMove = numX == numY;
    }

    public void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < 3; ++i) {
            System.out.print("| ");
            for (char c : grid[i])
                System.out.print(c + " ");
            System.out.println("|");
        }
        System.out.println("---------");
    }

    public int[] getCoordsFromHuman() throws IllegalArgumentException {
        System.out.print("Enter the coordinates: ");
        String[] coordsString = scanner.nextLine().split(" ");
        int[] coords = new int[2];
        try {
            coords[0] = Integer.parseInt(coordsString[0]);
            coords[1] = Integer.parseInt(coordsString[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("You should enter numbers!");
        }
        if (coords[0] < 1 || coords[0] > 3 || coords[1] < 1 || coords[1] > 3)
            throw new IllegalArgumentException("Coordinates should be from 1 to 3!");
        return coords;
    }

    //Function trusts that coords is well-formed; only give input from other functions
    public boolean makeMove(int[] coords) throws IllegalArgumentException {
        int[] gridCoords = convertFromBoardCoords(coords[0], coords[1]);
        if (grid[gridCoords[0]][gridCoords[1]] != ' ')
            throw new IllegalArgumentException("This cell is occupied! Choose another one!");
        else {
            grid[gridCoords[0]][gridCoords[1]] = xToMove ? 'X' : 'O';
            xToMove = !xToMove;
            --numEmpty;
            printBoard();
            return evaluateBoard(gridCoords[0], gridCoords[1]);
        }
    }

    //NOTE: takes grid coords, not board coords
    public boolean evaluateBoard(int lastx, int lasty) {
        //Check row and column
        boolean result = (grid[lastx][0] == grid[lastx][1] && grid[lastx][1] == grid[lastx][2])
                || (grid[0][lasty] == grid[1][lasty] && grid[1][lasty] == grid[2][lasty]);
        //Check if diagonals needed
        if (lastx == lasty)
            result = result || (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]);
        if (lastx + lasty == 2)
            result = result || (grid[2][0] == grid[1][1] && grid[1][1] == grid[0][2]);

        //Evaluate
        if (result) {
            System.out.println(grid[lastx][lasty] + " wins");
            return true;
        }
        else {
            if (numEmpty == 0) {
                System.out.println("Draw");
                return true;
            } else {
                //System.out.println("Game not finished");
                return false;
            }
        }
    }

    public boolean getxToMove() {
        return xToMove;
    }

    //Takes board coords
    public char charAt(int x, int y) {
        int[] gridCoords = convertFromBoardCoords(x, y);
        return grid[gridCoords[0]][gridCoords[1]];
    }

    //Returns an int array converting board coords to grid coords
    private int[] convertFromBoardCoords(int x, int y) {
        int[] newCoords = new int[2];
        newCoords[0] = 3 - y;
        newCoords[1] = x - 1;
        return newCoords;
    }
}
