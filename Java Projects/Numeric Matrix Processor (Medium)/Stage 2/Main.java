package processor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        intMatrix a = intMatrix.getMatrix(scanner);
        intMatrix.scalarMultiply(Integer.parseInt(scanner.nextLine()), a).printMatrix();
    }
}
