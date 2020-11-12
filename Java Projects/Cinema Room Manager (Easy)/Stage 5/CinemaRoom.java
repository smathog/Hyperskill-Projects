package cinema;

public class CinemaRoom {
    private char[][] seats;
    private int numPurchasedTickets;
    private int currentIncome;
    private int totalIncome;

    public CinemaRoom(int rows, int width) {
        seats = new char[rows][width];
        numPurchasedTickets = 0;
        currentIncome = 0;
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < width; ++j)
                seats[i][j] = 'S';
        if (rows * width <= 60)
            totalIncome = rows * width * 10;
        else
            totalIncome = ((rows / 2) * 10 + (rows - rows / 2) * 8) * width;
    }

    public void printRoom() {
        System.out.println("Cinema:");

        //Print top row
        System.out.print("  ");
        for (int i = 1; i <= seats[0].length; ++i)
            System.out.print(i + " ");
        System.out.println();

        //Print seats
        for (int row = 1; row <= seats.length; ++row) {
            System.out.print(row + " ");
            for (int col = 1; col <= seats[row - 1].length; ++col)
                System.out.print(seats[row - 1][col - 1] + " ");
            System.out.println();
        }
    }

    public String buySeat(int row, int column) {
        //Check coordinates are valid
        if (row < 1 || row > seats.length || column < 1 || column > seats[0].length) {
            System.out.println("Wrong input!");
            return null;
        }

        //Check seat is available
        if (seats[row - 1][column - 1] != 'S') {
            System.out.println("That ticket has already been purchased!");
            return null;
        }

        ++numPurchasedTickets;
        seats[row - 1][column - 1] = 'B';
        if (seats.length * seats[0].length <= 60 || row <= seats.length / 2) {
            currentIncome += 10;
            return "$10";
        } else {
            currentIncome += 8;
            return "$8";
        }
    }

    public void printStatistics() {
        System.out.println("Number of purchased tickets: " + numPurchasedTickets);
        System.out.println("Percentage: "
                + String.format("%.2f", (double) numPurchasedTickets / (seats.length * seats[0].length) *  100)
                + "%");
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
    }
}
