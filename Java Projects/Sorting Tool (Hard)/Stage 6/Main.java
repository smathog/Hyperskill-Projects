package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner;
    private static PrintWriter pw;

    public static void main(final String[] args) {
        //Check args correctness
        parseCommands(args);


        //Check if doing file or standard I/O
        String[] out = checkIOType(args);
        String inputFile = out[0];
        String outputFile = out[1];

        //Setup scanner and printer
        if (inputFile == null)
            scanner = new Scanner(System.in);
        else {
            try {
                scanner = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        if (outputFile == null) {
            pw = null;
        } else {
            try {
                pw = new PrintWriter(outputFile);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        //Parse command line args
        String datatype;
        String sortType;
        try {
            datatype = getDataType(args);
            sortType = getSortType(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        ArrayList<Object> list = new ArrayList<>();

        //Load based on sort type
        loadObjectList(list, datatype);

        //Report
        report(list, datatype, sortType, pw);

        //Close
        if (pw != null)
            pw.close();
        if (scanner != null)
            scanner.close();
    }

    private static void process(ArrayList<Object> list, String type) {
        System.out.println("Total " + (type.equals("line") ? "lines" : "numbers")
                + ": " + list.size() + ".");
        System.out.print("The " + (type.equals("line") ? "longest line" : "greatest number")
                + ": ");
        Object maxInstance;
        int numTimes;
        maxInstance = Collections.max(list, list.get(0) instanceof Long ?
                (o1, o2) -> (int) ((Long) o1 - (Long) o2)
                :
                //(o1, o2) -> ((String) o1).length() - ((String) o2).length()
                Comparator.comparingInt(o -> ((String) o).length())
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

    private static String getDataType(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if ("-dataType".equals(args[i]) && i < args.length - 1) {
                if ("long".equals(args[i + 1]))
                    return "long";
                else if ("word".equals(args[i + 1]))
                    return "word";
                else if ("line".equals(args[i + 1]))
                    return "line";
                else
                    throw new IllegalArgumentException("No data type defined!");
            } else if ("-dataType".equals(args[i]) && i == args.length - 1)
                throw new IllegalArgumentException("No data type defined!");
        }
        return "word";
    }

    private static String getSortType(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if ("-sortingType".equals(args[i]) && i < args.length - 1)
                if ("natural".equals(args[i + 1]))
                    return "natural";
                else if ("byCount".equals(args[i + 1]))
                    return "byCount";
                else
                    throw new IllegalArgumentException("No sorting type defined!");
            else if ("-sortingType".equals(args[i]) && i == args.length - 1)
                throw new IllegalArgumentException("No sorting type defined!");
        }
        return "natural";
    }

    private static void parseCommands(String[] args) {
        for (String s: args) {
            if (s.charAt(0) == '-' && !"-sortingType".equals(s) && !"-dataType".equals(s)
            && !"-inputFile".equals(s) && !"-otputFile".equals(s))
                System.out.println("\"" + s + "\" is not a valid parameter. It will be skipped.");
        }
    }

    private static String[] checkIOType(String[] args) {
        String inputFile = null;
        String outputFile = null;
        for (int i = 0; i < args.length; ++i) {
            if ("-inputFile".equals(args[i]) && i < args.length - 1)
                inputFile = args[i + 1];
            else if ("-outputFile".equals(args[i]) && i < args.length - 1)
                outputFile = args[i + 1];
        }
        return new String[]{inputFile, outputFile};
    }

    private static void loadObjectList(ArrayList<Object> list, String dataType) {
        if ("long".equals(dataType))
            while (scanner.hasNextLong()) {
                String temp = null;
                try {
                    temp = scanner.next();
                    list.add(Long.parseLong(temp));
                } catch (NumberFormatException e) {
                    System.out.println("\"" + temp + "\" is not a long. It will be skipped.");
                }
            }
        else if ("line".equals(dataType))
            while (scanner.hasNextLine())
                list.add(scanner.nextLine());
        else
            while (scanner.hasNext())
                list.add(scanner.next());
    }

    private static String totalNumber(ArrayList<Object> list, String dataType) {
        if ("long".equals(dataType))
            return "Total numbers: " + list.size() + ".";
        else if ("line".equals(dataType))
            return "Total lines: " + list.size() + ".";
        else
            return "Total words: " + list.size() + ".";

    }

    private static void sortNatural(ArrayList<Object> list, String dataType, PrintWriter pw) {
        if ("long".equals(dataType)) {
            LongConsumer lc = i -> {
                if (pw == null)
                    System.out.print(i + " ");
                else
                    pw.print(i + " ");
            };
            list.stream()
                    .mapToLong(i -> (Long) i).sorted()
                    .forEach(lc);
        } else {
            Consumer<Object> oc = i -> {
                if (pw == null)
                    System.out.print("line".equals(dataType) ? i.toString() + '\n' : i.toString() + ' ');
                else
                    pw.print("line".equals(dataType) ? i.toString() + '\n' : i.toString() + ' ');
            };
            list.stream().sorted(Comparator.comparing(Object::toString)).forEach(oc);
        }
    }

    private static void sortByCount(ArrayList<Object> list, String dataType, PrintWriter pw) {
        Comparator<Map.Entry<Object, Long>> customComparator = (o1, o2) -> {
            int val = (int) (o1.getValue() - o2.getValue());
            if (val == 0) {
                if ("long".equals(dataType))
                    return (int) (((Long) o1.getKey()) - ((Long) o2.getKey()));
                else
                    return ((String) o1.getKey()).compareTo((String) o2.getKey());
            } else
                return val;
        };

        Consumer<Map.Entry<Object, Long>> customConsumer = e -> {
            if (pw == null)
                System.out.println(e.getKey() + ": " + e.getValue() + " time(s), "
                        + ((int) (100 * (double) e.getValue() / list.size())) + "%");
            else
                pw.println(e.getKey() + ": " + e.getValue() + " time(s), "
                        + ((int) (100 * (double) e.getValue() / list.size())) + "%");
        };

        HashMap<Object, Long> countMap = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.counting()));
        countMap.entrySet().stream()
                .sorted(customComparator)
                .forEach(customConsumer);
    }

    private static void report(ArrayList<Object> list, String dataType, String sortType, PrintWriter pw) {
        //Report number
        if (pw == null)
            System.out.println(totalNumber(list, dataType));
        else
            pw.println(totalNumber(list ,dataType));

        //Sort
        if ("natural".equals(sortType)) {
            if (pw == null)
                System.out.print("Sorted data: " + ("line".equals(dataType) ? '\n' : ""));
            else
                pw.print("Sorted data: " + ("line".equals(dataType) ? '\n' : ""));
        }

        if ("natural".equals(sortType))
            sortNatural(list, dataType, pw);
        else
            sortByCount(list, dataType, pw);
    }
}