package tictactoe;

import java.util.stream.IntStream;

public class MediumComputer extends EasyComputer {
    public MediumComputer(Board b) {
        super(b);
    }

    @Override
    public int[] computeMove() {
        Board b = getBoard();
        char myChar = b.getxToMove() ? 'X' : 'O';

        //See if winning move available
        for (int i = 1; i <= 3; ++i)
            for (int j = 1; j <= 3; ++j)
                if (b.charAt(i, j) == ' ') {
                    //Free space, see if winning move
                    final int x = i;
                    final int y = j;
                    if (//Check row
                    IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s, y)).filter(c -> c == myChar).count() == 2
                    ||
                    //Check Column
                    IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(x, s)).filter(c -> c == myChar).count() == 2
                    ||
                    //Check if diagonal
                    (x + y == 4 && IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s,4 - s)).filter(c -> c == myChar).count() == 2)
                    ||
                    (x == y && IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s, s)).filter(c -> c == myChar).count() == 2)
                    )
                        return new int[]{i, j};
                }

        //See if blocking move available
        char theirChar = b.getxToMove() ? 'O' : 'X';
        for (int i = 1; i <= 3; ++i)
            for (int j = 1; j <= 3; ++j)
                if (b.charAt(i, j) == ' ') {
                    //Free space, see if blocking move
                    final int x = i;
                    final int y = j;
                    if (    //Check row
                    IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s, y)).filter(c -> c == theirChar).count() == 2
                    ||
                            //Check Column
                    IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(x, s)).filter(c -> c == theirChar).count() == 2
                    ||
                            //Check if diagonal
                    (x + y == 4 && IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s,4 - s)).filter(c -> c == theirChar).count() == 2)
                    ||
                    (x == y && IntStream.rangeClosed(1, 3).mapToObj(s -> b.charAt(s, s)).filter(c -> c == theirChar).count() == 2)
                    )
                        return new int[]{i, j};
                }

        //Previous cases failed, just make a random mood
        //AKA default to EasyComputer
        return super.computeMove();
    }

    @Override
    public String level() {
        return "medium";
    }
}
