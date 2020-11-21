package sorting;

import java.util.*;

public class Main {
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Long> longList = new ArrayList<>();
        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            longList.add(number);
        }
        System.out.println("Total numbers: " + longList.size() + ".");
        System.out.println("The largest number: " +
                Collections.max(longList)
                + " (" + Collections.frequency(longList, Collections.max(longList))
                + " time(s)).");
    }
}
