package solver;

import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[] eq1 = Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        double[] eq2 = Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        double[] result = Solver.linear22Solver(eq1[0], eq1[1], eq1[2], eq2[0], eq2[1], eq2[2]);
        System.out.println(result[0] + " " + result[1]);
    }
}
