package bullscows;

import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;

public class Main {
    private static int code;
    private static ArrayList<Integer> codeList;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        game();
    }

    public static void game() {
        System.out.println("Please, enter the secret code's length: ");
        int length = Integer.parseInt(scanner.nextLine());
        int num = generatePsuedoRandomNumber(length);
        if (num == -1) {
            System.out.println("Error: cannot generate that many unique digits!");
            return;
        } else {
            code = num;
            codeList = intToArrayList(code, length);
        }
        System.out.println("Okay, let's start a game!");
        int turn = 1;
        while (true) {
            System.out.println("Turn " + turn + ": ");
            int guess = Integer.parseInt(scanner.nextLine());
            if (grader(guess, length))
                break;
        }
        System.out.println("Congratulations! You guessed the secret code.");
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

    public static boolean grader(int guess, int length) {
        ArrayList<Integer> guessList = intToArrayList(guess, length);

        int numCows = 0;
        int numBulls = 0;

        for (int index = 0; index < length; ++index)
            if (!codeList.contains(guessList.get(index)))
                continue;
            else
                if (codeList.get(index).intValue() == guessList.get(index).intValue())
                    ++numBulls;
                else
                    ++numCows;

        if (numCows == 0 && numBulls == 0) {
            System.out.println("Grade: None.");
        } else if (numCows > 0 && numBulls == 0) {
            System.out.println("Grade: " + numCows + " cow(s).");
        } else if (numCows == 0 && numBulls > 0) {
            System.out.println("Grade: " + numBulls + " bull(s).");
        } else {
            System.out.println("Grade: " + numBulls + " bull(s) "
            + "and " + numCows + " cow(s).");
        }

        if (numBulls == length)
            return true;
        else
            return false;
    }

    public static ArrayList<Integer> intToArrayList(int input, int length) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int divisor = (int) Math.pow(10, length - 1); divisor >= 10; divisor /= 10) {
            temp.add(input / divisor);
            input %= divisor;
        }
        temp.add(input % 10);
        return temp;
    }
}
