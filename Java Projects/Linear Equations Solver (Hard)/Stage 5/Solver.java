package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solver {

    //Solves an equation of the form ax = b
    public static double linearSolver(double a, double b) {
        return b / a;
    }

    //Solves a system of two equations in two variables
    public static double[] linear22Solver(double a, double b, double c, double d, double e, double f) {
        final double y = (f - c * d  / a) / (e - b * d / a);
        final double x = (c - b * y) / a;
        return new double[]{x, y};
    }

    //Solves a system of N variables in N equations
    public static void linearNVarNEqSolver(String fileIn, String fileOut) {
        try (Scanner fileScanner = new Scanner(new File(fileIn));
             PrintWriter pw = new PrintWriter(fileOut)) {
            int num = Integer.parseInt(fileScanner.nextLine().split(" ")[1]);
            Matrix m = new Matrix(IntStream.range(0, num)
                    .mapToObj(i -> Arrays.stream(fileScanner.nextLine().split(" "))
                    .map(Complex::parseComplex)
                    .collect(ArrayList<Complex>::new, ArrayList<Complex>::add, ArrayList<Complex>::addAll))
                    .collect(Collectors.toCollection(ArrayList::new)));
            System.out.println("Input matrix: ");
            m.printMatrix();
            System.out.println("Reduced Row-Echelon Form: ");
            m.toReducedRowEchelonForm();
            m.printMatrix();
            if (m.isSolvable() == null) {
                pw.println("Infinitely many solutions");
            } else if (m.isSolvable()) {
                var copy = m.getCopyOfMatrix();
                System.out.println("Copy: ");
                copy.forEach(a -> {
                    a.forEach(d -> System.out.print(d + " "));
                    System.out.println("");
                });
                for (int i = 0; i < copy.size(); ++i) {
                    final int index = i;
                    boolean zeroRow = IntStream.range(0, m.getnCols() - 1).mapToObj(j -> copy.get(index).get(j)).allMatch(d -> d.equals(Complex.ZERO));
                    if (i != copy.size() - 1 && !zeroRow)
                        pw.println(copy.get(i).get(m.getnCols() - 1));
                    else if (!zeroRow)
                        pw.print(copy.get(i).get(m.getnCols() - 1));
                }
            } else {
                pw.println("No solutions");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
