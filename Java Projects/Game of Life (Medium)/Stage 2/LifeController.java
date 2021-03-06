package life;

import java.util.Scanner;
import java.util.Arrays;

public class LifeController {
    private LifeModel model;

    public LifeController() {
        Scanner scanner = new Scanner(System.in);
        int[] args = Arrays.stream(scanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        model = new LifeModel(args[0], args[1], args[2]);
        model.seedRandom();
        while (model.getCurrentGeneration() < model.getTargetGeneration())
            model.nextGeneration();
    }

    public void display() {
        model.getBoard().print();
    }
}
