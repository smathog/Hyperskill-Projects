package life;


public class charMatrix {
    private char[][] grid;

    public charMatrix(int row, int column) {
        grid = new char[row][column];
    }

    public void setCharAt(int row, int column, char ch) throws IllegalArgumentException {
        if (row < 0 || row >= grid.length || column < 0 || column > grid[0].length)
            throw new IllegalArgumentException("Invalid coordinates passed");
        grid[row][column] = ch;
    }

    public char getCharAt(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= grid.length || column < 0 || column > grid[0].length)
            throw new IllegalArgumentException("Invalid coordinates passed");
        return grid[row][column];
    }

    public int numRows() {
        return grid.length;
    }

    public int numCols() {
        return grid[0].length;
    }

    public void print() {
        for (char[] row : grid) {
            for (char c : row)
                System.out.print(c);
            System.out.println();
        }
    }
}
