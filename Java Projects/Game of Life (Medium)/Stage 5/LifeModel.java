package life;

import java.util.Random;

public class LifeModel {
    private charMatrix board;
    private final Random r;
    private int currentGeneration;
    private final int targetGeneration;
    private final int size;

    public LifeModel(int size, int seed, int generations) {
        board = new charMatrix(size, size);
        if (seed != -1)
            r = new Random(seed);
        else
            r = new Random();
        targetGeneration = generations + 1;
        currentGeneration = 1;
        this.size = size;
    }

    public void seedRandom() {
        for (int i = 0; i < board.numRows(); ++i)
            for (int j = 0; j < board.numCols(); ++j)
                    board.setCharAt(i, j, r.nextBoolean() ? 'O' : ' ');
    }

    public void nextGeneration() {
        charMatrix next = new charMatrix(board.numRows(), board.numCols());

        for (int i = 0; i < board.numRows(); ++i) {
            for (int j = 0; j < board.numRows(); ++j) {
                //Count number of alive neighbors
                int counter1 = 0;
                int numAlive = 0;
                int x = Math.floorMod(i - 1, board.numRows());
                int y = Math.floorMod(j - 1, board.numCols());
                while (counter1 < 3) {
                    int counter2 = 0;
                    while(counter2 < 3) {
                        if (board.getCharAt(x, y) == 'O' && !(x == i && y == j))
                            ++numAlive;
                        ++counter2;
                        y = Math.floorMod(++y, board.numCols());
                    }
                    ++counter1;
                    x = Math.floorMod(++x, board.numRows());
                    y = Math.floorMod(j - 1, board.numCols());
                }
                if ((board.getCharAt(i, j) == 'O' && numAlive == 2) || numAlive == 3)
                    next.setCharAt(i, j, 'O');
                else
                    next.setCharAt(i, j, ' ');
            }
        }
        board = next;
        ++currentGeneration;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public int getTargetGeneration() {
        return targetGeneration;
    }

    public charMatrix getBoard() {
        return board;
    }

    public int numAlive() {
        int sum = 0;
        for (int r = 0; r < board.numRows(); ++r)
            for (int c = 0; c < board.numCols(); ++c)
                sum += board.getCharAt(r, c) == 'O' ? 1 : 0;
        return sum;
    }

    public boolean isAlive(int row, int column) {
        return board.getCharAt(row, column) == 'O';
    }

    public int getSize() {
        return size;
    }
}
