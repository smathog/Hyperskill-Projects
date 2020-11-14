package processor;

import java.util.Scanner;
import java.util.Arrays;

public class Processor {
    private Scanner scanner;

    public Processor() {
        scanner = new Scanner(System.in);
        Matrix.setScanner(scanner);
    }

    public void execute() {
        executionLoop:
        while (true) {
            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix by a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            String command = scanner.nextLine();
            try {
                switch (command) {
                    case "1":
                        addMatrices();
                        break;
                    case "2":
                        scalarMultiplication();
                        break;
                    case "3":
                        matrixMultiplication();
                        break;
                    case "0":
                        break executionLoop;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("The operation cannot be performed.");
            }
            System.out.println();
        }
    }

    private void addMatrices() throws IllegalArgumentException {
        System.out.print("Enter size of first matrix: ");
        int[] dims = getDimensions();
        System.out.println("Enter first matrix: ");
        Matrix a = Matrix.getMatrix(dims[0], dims[1]);
        System.out.print("Enter size of second matrix: ");
        dims = getDimensions();
        System.out.println("Enter second matrix: ");
        Matrix b = Matrix.getMatrix(dims[0], dims[1]);
        System.out.println("The result is: ");
        Matrix.add(a, b).printMatrix();
    }

    private void scalarMultiplication() {
        System.out.print("Enter size of matrix: ");
        int[] dims = getDimensions();
        System.out.println("Enter matrix: ");
        Matrix a = Matrix.getMatrix(dims[0], dims[1]);
        System.out.print("Enter constant: ");
        double constant = Double.parseDouble(scanner.next());
        System.out.println("The result is: ");
        Matrix.scalarMultiply(constant, a).printMatrix();
    }

    private void matrixMultiplication() throws IllegalArgumentException {
        System.out.print("Enter size of first matrix: ");
        int[] dims = getDimensions();
        System.out.println("Enter first matrix: ");
        Matrix a = Matrix.getMatrix(dims[0], dims[1]);
        System.out.print("Enter size of second matrix: ");
        dims = getDimensions();
        System.out.println("Enter second matrix: ");
        Matrix b = Matrix.getMatrix(dims[0], dims[1]);
        System.out.println("The result is: ");
        Matrix.multiply(a, b).printMatrix();
    }

    private int[] getDimensions() {
        return Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
    }
}