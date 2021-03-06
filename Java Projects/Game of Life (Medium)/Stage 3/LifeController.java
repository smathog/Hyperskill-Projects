package life;

import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

public class LifeController {
    private LifeModel model;

    public LifeController() {
        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        model = new LifeModel(size, -1, 11);
        model.seedRandom();
    }

    public void execute() {
        while (model.getCurrentGeneration() < model.getTargetGeneration()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    clearScreen();
                    display();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    model.nextGeneration();
                }
            });
            thread.run();
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        //Display final execution
        clearScreen();
        display();
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
