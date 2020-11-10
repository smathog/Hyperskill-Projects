package bullscows;

import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;

public class Main {
    private static int code = 9315;
    private static ArrayList<Integer> codeList = intToArrayList(code);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        int num = generatePsuedoRandomNumber(input);
        if (num == -1)
            System.out.println("Error: cannot have that many unique digits!");
        else
            System.out.println("The random secret number is: " + num);
    }

    public static int generatePsuedoRandomNumber(int length) {
        if (length > 10)
            return -1;
        long psuedoRandomNumber = Math.abs(System.nanoTime());
        HashSet<Integer> uniqueNums = new HashSet<>();
        int first = Math.abs(((int) psuedoRandomNumber % 9) + 1);
        int num = first;
        uniqueNums.add(first);
        while (uniqueNums.size() < length) {
            if (psuedoRandomNumber == 0)
                psuedoRandomNumber = Math.abs(System.nanoTime());
            int temp = Math.abs((int) psuedoRandomNumber % 10);
            if (!uniqueNums.contains(temp)) {
                num = num * 10 + temp;
                uniqueNums.add(temp);
            }
            psuedoRandomNumber /= 10;
        }
        return num;
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
