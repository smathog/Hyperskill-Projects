package readability;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] inputs = scanner.nextLine().split("(\\.|!|\\?)");
        int wordCount = 0;
        for (String s : inputs) {
            String[] temp = s.trim().split(" +");
            wordCount += temp.length;
        }
        double average = ((double) wordCount) / ((double) inputs.length);
        if (average > (double) 10) {
            System.out.println("HARD");
        } else {
            System.out.println("EASY");
        }
    }
}
