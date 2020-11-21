package life;

import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

public class LifeController {
    private LifeModel model;
    private GameOfLife game;

    public LifeController() {
        Scanner scanner = new Scanner(System.in);
        game = new GameOfLife();
        int size = Integer.parseInt(scanner.nextLine());
        model = new LifeModel(size, -1, 99);
        model.seedRandom();
    }

    public void execute() {
        while (model.getCurrentGeneration() < model.getTargetGeneration()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    clearScreen();
                    display();
                    game.update(model);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    model.nextGeneration();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        //Display final execution
        clearScreen();
        display();
        game.update(model);
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
