package tictactoe;

import java.util.Random;

public class EasyComputer extends Computer {
    private final Board b;

    public EasyComputer(Board b) {
        this.b = b;
    }

    @Override
    public int[] computeMove() {
        //Return once looking at non-occupied space
        int[] coords = new int[2];
        do {
            Random r = new Random();
            coords[0] = r.nextInt(3) + 1;
            coords[1] = r.nextInt(3) + 1;
        } while (b.charAt(coords[0], coords[1]) != ' ');
        return coords;
    }

    @Override
    public String level() {
        return "easy";
    }

    protected Board getBoard() {
        return b;
    }
}
