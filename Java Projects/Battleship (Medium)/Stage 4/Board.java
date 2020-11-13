package battleship;

import java.util.Scanner;
import java.util.ArrayList;

public class Board {
    private final Object[][] grid;
    private final Character[][] displayGrid;
    private int numShips;
    private int numSunk;
    private final Scanner scanner;

    public Board(Scanner scanner) {
        this.scanner = scanner;
        numShips = 0;
        numSunk = 0;
        grid = new Object[10][10];
        displayGrid = new Character[10][10];
        for (int row = 0; row < 10; ++row)
            for (int col = 0; col < 10; ++col) {
                grid[row][col] = '~';
                displayGrid[row][col] = '~';
            }
        setUpBoard();
    }

    public void Play() {
        System.out.println();
        System.out.println("The game starts!");
        outerLoop:
        while (true) {
            System.out.println();
            printBoard(displayGrid);
            System.out.println();
            System.out.println("Take a shot!");
            while (true) {
                System.out.println();
                String in = scanner.nextLine();
                int x = in.charAt(0) - 'A';
                int y = Integer.parseInt(in.substring(1)) - 1;
                if (!validateShot(x, y)) {
                    System.out.println();
                    System.out.println("Error! You entered the wrong coordinates. Try again: ");
                    continue;
                }
                Ship result = fireShot(x, y);
                System.out.println();
                printBoard(displayGrid);
                System.out.println();
                if (result != null) {
                    if (result.isAlive())
                        System.out.println("You hit a ship! Try again: ");
                    else {
                        if (numSunk == numShips) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            break outerLoop;
                        } else
                            System.out.println("You sank a ship! Specify a new target: ");
                    }
                } else
                    System.out.println("You missed! Try again: ");
            }
        }
        System.out.println();
        printBoard(grid);
    }

    private boolean validateShot(int x, int y) {
        return x >= 0 && y >= 0 && x < 10 && y < 10;
    }

    private Ship fireShot(int x, int y) {
        //Mark shot
        if (grid[x][y].getClass() != Ship.class) {
            grid[x][y] = 'M';
            displayGrid[x][y] = 'M';
            return null;
        } else {
            Ship hitShip = ((Ship) grid[x][y]);
            //Only if ship not already hit here
            if (!displayGrid[x][y].toString().equals("X")) {
                hitShip.getHit();
                System.out.println("Ship Alive?: " + hitShip.isAlive());
                displayGrid[x][y] = 'X';
                if (!hitShip.isAlive())
                    ++numSunk;
            }
            return hitShip;
        }
    }

    public void setUpBoard() {
        printBoard(grid);
        for (Ship.ShipType st : Ship.ShipType.values()) {
            placeShip(st);
            printBoard(grid);
        }
    }

    private void placeShip(Ship.ShipType st) {
        System.out.println();
        System.out.print("Enter the coordinates of the "
            + st.getName() + " (" + st.getlength() + " cells): ");
        while (true) {
            System.out.println();
            String[] coords = scanner.nextLine().split(" ");
            System.out.println();
            int x1 = coords[0].charAt(0) - 'A';
            int y1 = Integer.parseInt(coords[0].substring(1)) - 1;
            int x2 = coords[1].charAt(0) - 'A';
            int y2 = Integer.parseInt(coords[1].substring(1)) - 1;
            if (validatePlacement(st, x1, y1, x2, y2)) {
                Ship ship = new Ship(st);
                if (x1 == x2) { //Horizontal orientation
                    for (int i = y1; ; i += (y2 - y1)/Math.abs(y2 - y1)) {
                        grid[x1][i] = ship;
                        if (i == y2)
                            break;
                    }
                } else { //Vertical orientation
                    for (int i = x1; ; i += (x2 - x1)/Math.abs(x2 - x1)) {
                        grid[i][y1] = ship;
                        if (i == x2)
                            break;
                    }
                }
                ++numShips;
                break;
            }
        }
    }

    public void printBoard(Object[][] grid) {
        //Print first row
        System.out.print("  ");
        for (int i = 1; i <= 10; ++i)
            System.out.print(i + " ");
        System.out.println();

        //Print grid with row markers
        for (int i = 0; i < 10; ++i) {
            System.out.print((char)('A' + i ) + " ");
            for (int j = 0; j < 10; ++j)
                if (grid[i][j].getClass() == Ship.class)
                    System.out.print("O ");
                else
                    System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }

    private boolean validatePlacement(Ship.ShipType st, int x1, int y1, int x2, int y2) {
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
        if (length != st.getlength()) {
            System.out.println("Error! Wrong length of the " +
                    st.getName() + "! Try again: ");
            return false;
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
                    .getClass() == Ship.class)
                    return false;
            }
        }
        return true;
    }
}
