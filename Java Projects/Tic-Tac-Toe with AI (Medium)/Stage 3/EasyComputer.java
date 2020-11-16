package tictactoe;

import java.util.Random;

public class EasyComputer implements Computer {
    private Board b;

    public EasyComputer(Board b) {
        this.b = b;
    }

    @Override
    public int[] computeMove() {
        //Return once looking at non-occupied space
        int[] coords = new int[2];
        while (true) {
            Random r = new Random();
            coords[0] = r.nextInt(3) + 1;
            coords[1] = r.nextInt(3) + 1;
            if (b.charAt(coords[0], coords[1]) == ' ')
                break;
        }
        return coords;
    }

    @Override
    public String level() {
        return "easy";
    }
}
