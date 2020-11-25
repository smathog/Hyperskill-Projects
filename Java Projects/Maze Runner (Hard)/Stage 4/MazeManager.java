package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;


public class MazeManager {
    private static Scanner scanner = new Scanner(System.in);
    private Maze m;

    public MazeManager() {
        m = null;
    }

    public void execute() {
        executionLoop:
        while (true) {
            System.out.println("=== Menu ===");
            System.out.println("1. Generate a new maze");
            System.out.println("2. Load a maze");
            if (m != null) {
                System.out.println("3. Save the maze");
                System.out.println("4. Display the maze");
                System.out.println("5. Find the escape");
            }
            System.out.println("0. Exit");
            String command = scanner.nextLine();
            if (m == null && ("3".equals(command) || "4".equals(command)))
                command = "-1";
            switch (command) {
                case "1":
                    generateMaze();
                    break;
                case "2":
                    loadMazeFromFile();
                    break;
                case "3":
                    saveMazeToFile();
                    break;
                case "4":
                    Maze.printMaze(m);
                    break;
                case "5":
                    m.solveMaze();
                    break;
                case "0":
                    System.out.println("Bye!");
                    break executionLoop;
                default:
                    System.out.println("Incorrect option. Please try again.");
                    break;
            }
            System.out.println();
        }
    }

    private void generateMaze() {
        System.out.println("Enter the size of a new maze: ");
        int size = Integer.parseInt(scanner.nextLine());
        m = new Maze(size, size);
        Maze.printMaze(m);
    }

    private void loadMazeFromFile() {
        String loadFile = scanner.nextLine();
        try (Scanner fileScanner = new Scanner(new File(loadFile))) {
            int[] dims = Arrays.stream(fileScanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            boolean[][] grid = new boolean[dims[0]][dims[1]];
            for (int i = 0; i < dims[0]; ++i)
                for (int j = 0; j< dims[1]; ++j)
                    grid[i][j] = fileScanner.nextBoolean();
            if (m == null)
                m = new Maze(3, 3);
            m.generateMazeFromBooleanArray(grid);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveMazeToFile() {
        String saveFile = scanner.nextLine();
        try (PrintWriter pw = new PrintWriter(saveFile)) {
            pw.println(m.numRows() + " " + m.numCols());
            for (int i = 0; i < m.numRows(); ++i) {
                for (int j = 0; j < m.numCols(); ++j)
                    pw.print(m.pathAt(i, j) + " ");
                pw.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
