package life;

import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

public class LifeController {
    private LifeModel model;
    private GameOfLife game;
    private boolean pauseExecution;

    public LifeController() {
        Scanner scanner = new Scanner(System.in);
        game = new GameOfLife();
        game.getController(this);
        int size = Integer.parseInt(scanner.nextLine());
        model = new LifeModel(size, -1, 99);
        model.seedRandom();
        pauseExecution = false;
    }

    public void execute() {
        pauseExecution = false;
        while (model.getCurrentGeneration() < model.getTargetGeneration() && !pauseExecution) {
            game.setExecutingThread(Thread.currentThread());
            clearScreen();
            display();
            game.update(model);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            model.nextGeneration();
        } if (model.getCurrentGeneration() == model.getTargetGeneration()) {
            //Display final execution
            clearScreen();
            display();
            game.update(model);
        }
    }

    public void pause() {
        pauseExecution = true;
    }

    public void reset() {
        model = new LifeModel(model.getSize(), -1, 99);
        model.seedRandom();
    }

    public void display() {
        System.out.println("Generation #" + model.getCurrentGeneration());
        System.out.println("Alive: " + model.numAlive());
        model.getBoard().print();
    }

    public void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e) {}
    }
}
