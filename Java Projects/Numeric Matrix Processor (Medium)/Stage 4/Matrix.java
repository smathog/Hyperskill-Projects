package processor;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Matrix {
    private final double[][] matrix;
    private static Scanner scanner;

    public Matrix(int rows, int columns) {
        matrix = new double[rows][columns];
    }

    public static Matrix getMatrix() {
        int[] dims = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        return getMatrix(dims[0], dims[1]);
    }
    
    public static Matrix getMatrix(int rows, int columns) {
        Matrix temp = new Matrix(rows, columns);
        for (int i = 0; i < rows; ++i)
            temp.matrix[i] = Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        return temp;
    }

    public static Matrix add(Matrix a, Matrix b) throws IllegalArgumentException {
        //Handle possible invalid input
        if (a == null
                || b == null
                ||a.matrix.length != b.matrix.length
                || a.matrix[0].length != b.matrix[0].length)
            throw new IllegalArgumentException("Matrices not the same size!");

        //Return new sum matrix
        Matrix temp = new Matrix(a.matrix.length, a.matrix[0].length);
        for (int i = 0; i < a.matrix.length; ++i) {
            final int row = i;
            temp.matrix[i] = IntStream.range(0, a.matrix[0].length)
                    .mapToDouble(j -> a.matrix[row][j] + b.matrix[row][j])
                    .toArray();
        }
        return temp;
    }

    public static Matrix multiply(Matrix a, Matrix b) throws IllegalArgumentException {
        //Handle possible invalid input
        if (a == null
            || b == null
            || a.matrix[0].length != b.matrix.length)
            throw new IllegalArgumentException("Matrices of wrong size for multiplication!");

        //Return new product matrix
        Matrix temp = new Matrix(a.matrix.length, b.matrix[0].length);
        for (int row = 0; row < a.matrix.length; ++row) {
            for (int col = 0; col < b.matrix[0].length; ++col) {
                //Calculate temp.matrix[row][col]
                //Appease the Java lambda gods
                final int r = row;
                final int c = col;
                temp.matrix[row][col] = IntStream.range(0, a.matrix[0].length)
                        .mapToDouble(j -> a.matrix[r][j] * b.matrix[j][c])
                        .sum();
            }
        }
        return temp;
    }

    public static Matrix scalarMultiply(double scalar, Matrix a) {
        Matrix temp = new Matrix(a.matrix.length, a.matrix[0].length);
        for (int i = 0; i < a.matrix.length; ++i)
            temp.matrix[i] = Arrays.stream(a.matrix[i]).map(j -> scalar * j).toArray();
        return temp;
    }

    public static Matrix transposeMainDiagonal(Matrix a) {
        Matrix temp = new Matrix(a.matrix[0].length, a.matrix.length);
        for (int r = 0; r < a.matrix[0].length; ++r) {
            final int row = r;
            temp.matrix[row] = IntStream.range(0, a.matrix.length)
                    .mapToDouble(i -> a.matrix[i][row])
                    .toArray();
        }
        return temp;
    }

    public static Matrix transposeSideDiagonal(Matrix a) {
        Matrix temp = new Matrix(a.matrix[0].length, a.matrix.length);
        for (int r = a.matrix[0].length - 1; r >= 0; --r) {
            final int row = r;
            temp.matrix[a.matrix[0].length - 1 - row] = IntStream.range(0, a.matrix.length)
                    .mapToDouble(i -> a.matrix[a.matrix.length - i - 1][row])
                    .toArray();
        }
        return temp;
    }

    public static Matrix transposeVertical(Matrix a) {
        Matrix temp = new Matrix(a.matrix.length, a.matrix[0].length);
        for (int r = 0; r < a.matrix.length; ++r) {
            final int row = r;
            temp.matrix[row] = IntStream.range(0, a.matrix[0].length)
                    .mapToDouble(i -> a.matrix[row][a.matrix[0].length - 1 - i])
                    .toArray();
        }
        return temp;
    }

    public static Matrix transposeHorizontal(Matrix a) {
        Matrix temp = new Matrix(a.matrix.length, a.matrix[0].length);
        for (int r = 0; r < a.matrix.length; ++r)
            temp.matrix[r] = a.matrix[a.matrix.length - 1 - r].clone();
        return temp;
    }

    public void printMatrix() {
        for (double[] r : matrix) {
            for (double c : r)
                System.out.print(c + " ");
            System.out.println();
        }
    }

    public static void setScanner(Scanner scanner) {
        Matrix.scanner = scanner;
    }
}
