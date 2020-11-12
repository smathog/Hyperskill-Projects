package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        int rows = Integer.parseInt(scanner.nextLine());
        System.out.println("enter the number of seats in each row: ");
        int width = Integer.parseInt(scanner.nextLine());
        System.out.println();
        printCinema(rows, width, -1, -1);
        System.out.println();
        System.out.println("Enter a row number: ");
        int rowNum = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter a seat number in that row: ");
        int seatNum = Integer.parseInt(scanner.nextLine());
        System.out.println();
        System.out.println("Ticket price:  $" + ticketPrice(rows, width, rowNum));
        System.out.println();
        printCinema(rows, width, rowNum, seatNum);

    }

    private static void printCinema(int rows, int width, int rowNum, int seatNum) {
        System.out.println("Cinema:");

        //Print column indices
        System.out.print("  ");
        for (int i = 1; i <= width; ++i)
            System.out.print(i + " ");
        System.out.println();

        //Print each row
        for (int i  = 1; i <= rows; ++i) {
            System.out.print(i + " ");
            for (int j = 1; j <= width; ++j)
                if (i == rowNum && j == seatNum)
                    System.out.print("B ");
                else
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

    private static int ticketPrice(int rows, int width, int rowNum) {
        if (rows * width <= 60)
            return 10;
        else
            if (rowNum <= rows / 2)
                return 10;
            else
                return 8;
    }
}