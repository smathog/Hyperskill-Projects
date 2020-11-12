package cinema;

public class CinemaRoom {
    char[][] seats;

    public CinemaRoom(int rows, int width) {
        seats = new char[rows][width];
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < width; ++j)
                seats[i][j] = 'S';
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
        seats[row - 1][column - 1] = 'B';
        if (seats.length * seats[0].length <= 60)
            return "$10";
        else if (row <= seats.length / 2)
            return "$10";
        else
            return "$8";
    }
}
