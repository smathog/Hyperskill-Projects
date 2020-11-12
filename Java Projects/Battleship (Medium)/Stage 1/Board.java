package battleship;

import java.net.Inet4Address;
import java.util.Scanner;

public class Board {
    private char[][] grid;
    private Scanner scanner;

    public Board(Scanner scanner) {
        this.scanner = scanner;
        grid = new char[10][10];
        for (int row = 0; row < 10; ++row)
            for (int col = 0; col < 10; ++col)
                grid[row][col] = '~';
    }

    public void setUpBoard() {
        printBoard();
        for (ShipType st : ShipType.values()) {
            placeShip(st);
            printBoard();
        }
    }

    private void placeShip(ShipType st) {
        System.out.println();
        System.out.print("Enter the coordinates of the ");
        switch (st) {
            case Carrier:
                System.out.println("Aircraft Carrier (5 cells): ");
                break;
            case Battleship:
                System.out.println("Battleship (4 cells): ");
                break;
            case Submarine:
                System.out.println("Submarine (3 cells): ");
                break;
            case Cruiser:
                System.out.println("Cruiser (3 cells): ");
                break;
            case Destroyer:
                System.out.println("Destroyer (2 cells): ");
                break;
        }
        while (true) {
            System.out.println();
            String[] coords = scanner.nextLine().split(" ");
            System.out.println();
            int x1 = coords[0].charAt(0) - 'A';
            int y1 = Integer.parseInt(coords[0].substring(1)) - 1;
            int x2 = coords[1].charAt(0) - 'A';
            int y2 = Integer.parseInt(coords[1].substring(1)) - 1;
            if (!validatePlacement(st, x1, y1, x2, y2))
                continue;
            else {
                if (x1 == x2) { //Horizontal orientation
                    for (int i = y1; ; i += (y2 - y1)/Math.abs(y2 - y1)) {
                        grid[x1][i] = 'O';
                        if (i == y2)
                            break;
                    }
                } else { //Vertical orientation
                    for (int i = x1; ; i += (x2 - x1)/Math.abs(x2 - x1)) {
                        grid[i][y1] = 'O';
                        if (i == x2)
                            break;
                    }
                }
                break;
            }
        }
    }

    public void printBoard() {
        //Print first row
        System.out.print("  ");
        for (int i = 1; i <= 10; ++i)
            System.out.print(i + " ");
        System.out.println();

        //Print grid with row markers
        for (int i = 0; i < 10; ++i) {
            System.out.print((char)('A' + i ) + " ");
            for (int j = 0; j < 10; ++j)
                System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }

    private boolean validatePlacement(ShipType st, int x1, int y1, int x2, int y2) {
        //Check coordinates are within the board
        int[] arr = {x1, y1, x2, y2};
        for (int i : arr)
            if (i < 0 || i >= 10) {
                System.out.println("Error: coordinate not on board! Try again: ");
                return false;
            }

        //Check coordinates are not diagonal
        if (x1 != x2 && y1 != y2) {
            System.out.println("Error: Wrong ship location! Try again: ");
            return false;
        }

        //Check length is correct
        int length;
        if (x1 == x2)
            length = Math.abs(y1 - y2) + 1;
        else
            length = Math.abs(x1 - x2) + 1;
        switch (st) {
            case Carrier:
                if (length != 5) {
                    System.out.println("Error! Wrong length of the Aircraft Carrier! Try again: ");
                    return false;
                }
                break;
            case Battleship:
                if (length != 4) {
                    System.out.println("Error! Wrong length of the Battleship! Try again: ");
                    return false;
                }
                break;
            case Submarine:
                if (length != 3) {
                    System.out.println("Error! Wrong length of the Submarine! Try again: ");
                    return false;
                }
                break;
            case Cruiser:
                if (length != 3) {
                    System.out.println("Error! Wrong length of the Cruiser! Try again: ");
                    return false;
                }
                break;
            case Destroyer:
                if (length != 2) {
                    System.out.println("Error! Wrong lnegth of the Destroyer! Try again: ");
                    return false;
                }
                break;
        }

        //Check that ship is not too close to another ship
        if (x1 == x2) { //Horizontal orientation
            for (int i = y1; i != y2; i += (y2 - y1)/Math.abs(y2 - y1))
                if (!surroundingsClear(x1, i)) {
                    System.out.println("Error! You placed it too close to another ship. Try again: ");
                    return false;
                }
        } else { //Vertical orientation
            for (int i = x1; i != x2; i += (x2 - x1)/Math.abs(x2 - x1))
                if (!surroundingsClear(i, y1)) {
                    System.out.println("Error! You placed it too close to another ship. Try again: ");
                    return false;
                }
        }

        //All checks passed
        return true;
    }

    private boolean surroundingsClear(int x1, int y1) {
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (grid
                        [Math.max(0, Math.min(i + x1, 9))]
                        [Math.max(0, Math.min(j + y1, 9))]
                    == 'O')
                    return false;
            }
        }
        return true;
    }

    public enum ShipType {
        Carrier, Battleship, Submarine, Cruiser, Destroyer
    }
}
