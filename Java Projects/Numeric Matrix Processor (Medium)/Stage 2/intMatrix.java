package processor;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class intMatrix {
    private final int[][] matrix;

    public intMatrix(int rows, int columns) {
        matrix = new int[rows][columns];
    }

    public static intMatrix getMatrix(Scanner scanner) {
        int[] dims = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        intMatrix temp = new intMatrix(dims[0], dims[1]);
        for (int i = 0; i < dims[0]; ++i)
            temp.matrix[i] = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        return temp;
    }

    public static intMatrix add(intMatrix a, intMatrix b) throws IllegalArgumentException {
        //Handle possible invalid input
        if (a.matrix.length != b.matrix.length || a.matrix[0].length != b.matrix[0].length)
            throw new IllegalArgumentException("Matrices not the same size!");

        //Return new sum matrix
        intMatrix temp = new intMatrix(a.matrix.length, a.matrix[0].length);
        for (int i = 0; i < a.matrix.length; ++i) {
            int row = i;
            temp.matrix[i] = IntStream.range(0, a.matrix[0].length)
                    .map(j -> a.matrix[row][j] + b.matrix[row][j])
                    .toArray();
        }
        return temp;
    }

    public static intMatrix scalarMultiply(int scalar, intMatrix a) {
        intMatrix temp = new intMatrix(a.matrix.length, a.matrix[0].length);
        for (int i = 0; i < a.matrix.length; ++i)
            temp.matrix[i] = Arrays.stream(a.matrix[i]).map(j -> scalar * j).toArray();
        return temp;
    }

    public void printMatrix() {
        for (int[] r : matrix) {
            for (int c : r)
                System.out.print(c + " ");
            System.out.println();
        }
    }
}
