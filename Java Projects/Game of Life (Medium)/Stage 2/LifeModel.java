package life;

import java.util.Random;

public class LifeModel {
    private charMatrix board;
    private Random r;
    private int currentGeneration;
    private int targetGeneration;

    public LifeModel(int size, int seed, int generations) {
        board = new charMatrix(size, size);
        r = new Random(seed);
        targetGeneration = generations + 1;
        currentGeneration = 1;
    }

    public void seedRandom() {
        for (int i = 0; i < board.numRows(); ++i)
            for (int j = 0; j < board.numCols(); ++j)
                    board.setCharAt(i, j, r.nextBoolean() ? 'O' : ' ');
    }

    public void nextGeneration() {
        charMatrix next = new charMatrix(board.numRows(), board.numCols());
        /*
        System.out.println("Debugging printout: generation " + currentGeneration);
        board.print();
        System.out.println("----");
        */
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
}
