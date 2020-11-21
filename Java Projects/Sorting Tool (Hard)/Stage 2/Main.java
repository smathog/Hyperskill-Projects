package sorting;

import com.sun.security.jgss.GSSUtil;

import java.util.*;

public class Main {

    public static void main(final String[] args) {
        //Setup scanner
        Scanner scanner = new Scanner(System.in);

        //Parse command line args
        String type;
        ArrayList<Object> list = new ArrayList<>();
        if (args.length != 2 || args[1].equals("word")) {
            type = "word";
            while (scanner.hasNext())
                list.add(scanner.next());
        } else if (args[1].equals("line")) {
            type = "line";
            while (scanner.hasNextLine())
                list.add(scanner.nextLine());
        } else if (args[1].equals("long")) {
            type = "long";
            while (scanner.hasNextLong())
                list.add(scanner.nextLong());
        } else
            throw new IllegalArgumentException("Bad command passed!");

        //Process
        process(list, type);
    }

    public static void process(ArrayList<Object> list, String type) {
        System.out.println("Total " + (type.equals("line") ? "lines" : "numbers")
            + ": " + list.size() + ".");
        System.out.print("The " + (type.equals("line") ? "longest line" : "greatest number")
                + ": ");
        Object maxInstance;
        int numTimes;
        maxInstance = Collections.max(list, list.get(0) instanceof Long ?
                (o1, o2) -> (int) ((Long) o1 - (Long) o2)
                :
                (o1, o2) -> ((String) o1).length() - ((String) o2).length()
                );
        numTimes = Collections.frequency(list, maxInstance);
        String percentString = "(" + numTimes + " time(s), " + (numTimes * 100 / list.size()) + "%).";
        if (type.equals("line")) {
            System.out.println();
            System.out.println(maxInstance);
            System.out.println(percentString);
        } else {
            System.out.println(maxInstance + " " + percentString);
        }

    }
}