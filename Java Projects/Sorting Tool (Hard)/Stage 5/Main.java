package sorting;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner;

    public static void main(final String[] args) {
        //Setup scanner
        scanner = new Scanner(System.in);

        //Parse command line args
        parseCommands(args);
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

        //Report number
        System.out.println(totalNumber(list, datatype));

        //Sort
        if (!"line".equals(datatype) && "natural".equals(sortType)) {
            System.out.print("Sorted data: ");
        } else if ("line".equals(datatype) && "natural".equals(sortType)) {
            System.out.println("Sorted data: ");
        }
        if ("natural".equals(sortType))
            sortNatural(list, datatype);
        else
            sortByCount(list, datatype);
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
            if (s.charAt(0) == '-' && !"-sortingType".equals(s) && !"-dataType".equals(s))
                System.out.println("\"" + s + "\" is not a valid parameter. It will be skipped.");
        }
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

    private static void sortNatural(ArrayList<Object> list, String dataType) {
        if ("long".equals(dataType))
            list.stream()
                    .mapToLong(i -> (Long) i).sorted()
                    .forEach(d -> System.out.print(d + " "));
        else if ("line".equals(dataType))
            list.stream()
                    .sorted((s1, s2) -> ((String) s1).compareTo((String) s2))
                    .forEach(System.out::println);
        else
            list.stream()
                    .sorted((s1, s2) -> ((String) s1).compareTo((String) s2))
                    .forEach(s -> System.out.print(s + " "));

    }

    private static void sortByCount(ArrayList<Object> list, String dataType) {
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

        HashMap<Object, Long> countMap = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.counting()));
        countMap.entrySet().stream()
                .sorted(customComparator)
                .forEach(o -> System.out.println(o.getKey() + ": " + o.getValue() + " time(s), " + (100 * o.getValue() / list.size()) + "%"));
    }
}