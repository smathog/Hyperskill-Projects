package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        int rows = Integer.parseInt(scanner.nextLine());
        System.out.println("enter the number of seats in each row: ");
        int width = Integer.parseInt(scanner.nextLine());
        System.out.println("Total income: ");
        System.out.println("$" + calculateProfit(rows, width));
    }

    private static void printCinema(int rows, int width) {
        System.out.println("Cinema:");

        //Print column indices
        System.out.print("  ");
        for (int i = 1; i <= width; ++i)
            System.out.print(i + " ");
        System.out.println();

        //Print each row
        for (int i  = 1; i <= rows; ++i) {
            System.out.print(i + " ");
            for (int j = 0; j < width; ++j)
                System.out.print("S ");
            System.out.println();
        }
    }

    private static int calculateProfit(int rows, int width) {
        if (rows * width <= 60)
            return rows * width * 10;
        else
            return ((rows / 2) * 10 + (rows - rows / 2) * 8) * width;
    }
}