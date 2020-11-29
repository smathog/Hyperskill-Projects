package solver;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

//Test class just see if matrix works as intended
public class MatrixTester {
    public static void Test() {
        Scanner scanner = new Scanner(System.in);
        int numRows = Integer.parseInt(scanner.nextLine());
        ArrayList<ArrayList<Double>> input = new ArrayList<>();
        for (int i = 0; i < numRows; ++i)
            input.add(Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        Matrix m = new Matrix(input);
        System.out.println("Raw matrix: ");
        m.printMatrix();
        System.out.println("Reduced Row Echelon: ");
        m.toReducedRowEchelonForm();
        m.printMatrix();
    }
}
