package life;

import java.util.Random;

public class LifeModel {
    private charMatrix board;
    private Random r;

    public LifeModel(int size, int seed) {
        board = new charMatrix(size, size);
        r = new Random(seed);
    }

    public void seedRandom() {
        for (int i = 0; i < board.numRows(); ++i)
            for (int j = 0; j < board.numCols(); ++j)
                    board.setCharAt(i, j, r.nextBoolean() ? 'O' : ' ');
    }

    public charMatrix getBoard() {
        return board;
    }
}
