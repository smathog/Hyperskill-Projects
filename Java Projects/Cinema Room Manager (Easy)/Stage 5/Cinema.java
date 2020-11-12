package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        int rows = Integer.parseInt(scanner.nextLine());
        System.out.println("enter the number of seats in each row: ");
        int width = Integer.parseInt(scanner.nextLine());
        CinemaRoom cr = new CinemaRoom(rows, width);
        commandLoop:
        while (true) {
            System.out.println();
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            String command = scanner.nextLine();
            System.out.println();
            switch(command) {
                case "1":
                    cr.printRoom();
                    break;
                case "2": {
                    ticketLoop:
                    while (true) {
                        System.out.println("Enter a row number: ");
                        int row = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter a seat number in that row: ");
                        int col = Integer.parseInt(scanner.nextLine());
                        String price = cr.buySeat(row, col);
                        if (price != null) {
                            System.out.println("Ticket price: " + price);
                            break ticketLoop;
                        }
                    }
                    break;
                }
                case "3":
                    cr.printStatistics();
                    break;
                case "0":
                    break commandLoop;
                default:
                    break;
            }
        }


    }
}