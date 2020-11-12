package cinema;

public class Cinema {

    public static void main(String[] args) {
        printCinema(7, 8);
    }

    public static void printCinema(int rows, int width) {
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
}