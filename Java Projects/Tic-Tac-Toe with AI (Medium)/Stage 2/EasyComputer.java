package tictactoe;

import java.util.Random;

public class EasyComputer implements Computer {
    public EasyComputer() {}


    @Override
    public int[] computeMove() {
        int[] coords = new int[2];
        Random r = new Random();
        coords[0] = r.nextInt(3) + 1;
        coords[1] = r.nextInt(3) + 1;
        return coords;
    }

    @Override
    public String level() {
        return "easy";
    }
}
