package bullscows;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static int code = 9315;
    private static ArrayList<Integer> codeList = intToArrayList(code);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        grader(input);
        System.out.println(" The secret code is " + code);
    }

    public static boolean grader(int guess) {
        ArrayList<Integer> guessList = intToArrayList(guess);
        int numCows = 0;
        int numBulls = 0;
        for (int c : guessList) {
            if (!codeList.contains(c))
                continue;
            else {
                if (codeList.indexOf(c) == guessList.indexOf(c))
                    ++numBulls;
                else
                    ++numCows;
            }
        }
        if (numCows == 0 && numBulls == 0) {
            System.out.print("Grade: None.");
        } else if (numCows > 0 && numBulls == 0) {
            System.out.print("Grade: " + numCows + " cow(s).");
        } else if (numCows == 0 && numBulls > 0) {
            System.out.print("Grade: " + numBulls + " bull(s).");
        } else {
            System.out.print("Grade: " + numBulls + " bull(s) "
            + "and " + numCows + " cow(s).");
        }
        if (numBulls == 4)
            return true;
        else
            return false;
    }

    public static ArrayList<Integer> intToArrayList(int input) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int divisor = 1000; divisor >= 10; divisor /= 10) {
            temp.add(input / divisor);
            input %= divisor;
        }
        temp.add(input % 10);
        return temp;
    }
}
