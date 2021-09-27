package numbers;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static final LinkedHashMap<String, Predicate<BigInteger>> propertiesMap = new LinkedHashMap<>();
    private static final HashMap<String, String> incompatibleProperties = new HashMap<>();
    private static final String WELCOME = "Welcome to Amazing Numbers!";
    private static final String INSTRUCTIONS;
    private static final String FIRST_PARAM_ERROR = "The first parameter should be a natural number or zero.";
    private static final String SECOND_PARAM_ERROR = "The second parameter should be a natural number.";
    private static final String AVAILABLE_PROPERTIES;

    // Set up the properties map by associating properties with method references
    static {
        propertiesMap.put("even", Main::isEven);
        propertiesMap.put("odd", Main::isOdd);
        propertiesMap.put("buzz", Main::isBuzz);
        propertiesMap.put("duck", Main::isDuck);
        propertiesMap.put("palindromic", Main::isPalindromic);
        propertiesMap.put("gapful", Main::isGapful);
        propertiesMap.put("spy", Main::isSpy);
        propertiesMap.put("square", Main::isSquare);
        propertiesMap.put("sunny", Main::isSunny);
        propertiesMap.put("jumping", Main::isJumping);
        propertiesMap.put("happy", Main::isHappy);
        propertiesMap.put("sad", Main::isSad);
        AVAILABLE_PROPERTIES = String.format("Available properties: [%s]",
                String.join(",", propertiesMap.keySet()));
    }

    //Set up map of incompatible properties
    static {
        incompatibleProperties.put("even", "ODD");
        incompatibleProperties.put("odd", "EVEN");
        incompatibleProperties.put("duck", "SPY");
        incompatibleProperties.put("spy", "DUCK");
        incompatibleProperties.put("sunny", "SQUARE");
        incompatibleProperties.put("square", "SUNNY");
        incompatibleProperties.put("happy", "SAD");
        incompatibleProperties.put("sad", "HAPPY");
    }

    //Set up instructions String
    static {
        INSTRUCTIONS = "Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.";
    }


    public static void main(String[] args) {
        System.out.println(WELCOME);
        System.out.println();
        System.out.println(INSTRUCTIONS);
        mainMenu();
    }

    public static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.print("Enter a request: ");
            String[] input = scanner.nextLine().split(" ");
            System.out.println();
            //Validate input
            if (!validateInput(input)) {
                continue;
            }

            //Handle case input is single number (possibly exit command)
            BigInteger first = new BigInteger(input[0]);
            if (input.length == 1) {
                if (BigInteger.ZERO.equals(first)) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    valueProperties(first);
                }
            }

            //Handle cases where input is two or more arguments
            if (input.length >= 2) {
                BigInteger second = new BigInteger(input[1]);
                if (input.length == 2) {
                    nextN(first, second);
                } else { //input.length >= 3
                    HashSet<String> targetProperties = new HashSet<>();
                    HashSet<String> excludedProperties = new HashSet<>();
                    for (int i = 2; i < input.length; ++i) {
                        if (input[i].startsWith("-")) {
                            excludedProperties.add(input[i].substring(1));
                        } else {
                            targetProperties.add(input[i]);
                        }
                    }
                    nextNProperties(first, second, targetProperties, excludedProperties);
                }
            }
        }
    }

    private static void valueProperties(BigInteger value) {
        System.out.printf("Properties of %d%n", value);
        propertiesMap.forEach((s, p) -> System.out.printf("%s: %b%n", s, p.test(value)));
    }

    private static void nextN(BigInteger value, BigInteger n) {
        for (BigInteger i = value; !i.equals(value.add(n)); i = i.add(BigInteger.ONE)) {
            final BigInteger finalI = i;
            System.out.printf("%d is %s%n", i,
                    propertiesMap.entrySet().stream()
                            .filter(e -> e.getValue().test(finalI))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.joining(", "))
                    );
        }
    }

    private static void nextNProperties(BigInteger value, BigInteger n, HashSet<String> targetProperties,
                                        HashSet<String> excludedProperties) {
        BigInteger counter = BigInteger.ZERO;
        while (counter.compareTo(n) < 0) {
            final BigInteger localValue = value;
            HashSet<String> properties = propertiesMap.entrySet().stream()
                    .filter(e -> e.getValue().test(localValue))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(HashSet::new));
            if (targetProperties.stream().allMatch(s -> properties.contains(s.toLowerCase()))
                && excludedProperties.stream().noneMatch(s -> properties.contains(s.toLowerCase()))) {
                counter = counter.add(BigInteger.ONE);
                System.out.printf("%d is %s%n", localValue,
                        String.join(", ", properties));
            }
            value = value.add(BigInteger.ONE);
        }
    }

    private static boolean validateInput(String[] input) {
        //Validate first param
        try {
            BigInteger first = new BigInteger(input[0]);
            if (first.compareTo(BigInteger.ZERO) < 0) {
                System.out.println(FIRST_PARAM_ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(FIRST_PARAM_ERROR);
            return false;
        }

        //If present, validate second and beyond params
        if (input.length > 1) {
            try {
                BigInteger second = new BigInteger(input[1]);
                if (second.compareTo(BigInteger.ZERO) < 1) {
                    System.out.println(SECOND_PARAM_ERROR);
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(SECOND_PARAM_ERROR);
                return false;
            }

            if (input.length > 2) {
                HashSet<String> propertiesSet = new HashSet<>();
                HashSet<String> negativePropertiesSet = new HashSet<>();
                HashSet<String> wrongPropertySet = new HashSet<>();
                for (int i = 2; i < input.length; ++i) {
                    String property = input[i];
                    boolean negative = false;
                    if (property.startsWith("-")) {
                        property = property.substring(1);
                        negative = true;
                    }
                    // Check to make sure property actually is known
                    if (propertiesMap.keySet().stream().noneMatch(property::equalsIgnoreCase)) {
                        wrongPropertySet.add(property.toUpperCase());
                    }

                    // Add property to appropriate set
                    if (negative) {
                        negativePropertiesSet.add(input[i]);
                    } else {
                        propertiesSet.add(property);
                    }
                }

                //Handle case where wrong properties are present
                if (!wrongPropertySet.isEmpty()) {
                    if (wrongPropertySet.size() == 1) {
                        System.out.printf("The property [%s] is wrong.%n", wrongPropertySet.stream().findFirst().get());
                    } else {
                        System.out.printf("The properties [%s] are wrong.%n", String.join(", ", wrongPropertySet));
                    }
                    System.out.println(AVAILABLE_PROPERTIES);
                    return false;
                }

                // Handle cases of mutually exclusive properties
                HashSet<String> combinedSet = Stream.concat(propertiesSet.stream(),
                        negativePropertiesSet.stream())
                        .collect(Collectors.toCollection(HashSet::new));
                for (String property: combinedSet) {
                    // Check if a different property contradicts the explored property
                    if (incompatibleProperties.containsKey(property)
                            || incompatibleProperties.containsKey(property.substring(1))) {
                        String target;
                        if (propertiesSet.contains(property)) {
                            target = incompatibleProperties.get(property);
                        } else {
                            target = "-" + incompatibleProperties.get(property.substring(1));
                        }
                        if (combinedSet.stream().anyMatch(target::equalsIgnoreCase)) {
                            System.out.printf("The request contains mutually exclusive properties: [%s, %s]%n",
                                    property.toUpperCase(), target.toUpperCase());
                            System.out.println("There are no numbers with these properties.");
                            return false;
                        }
                    }

                    // Check if the opposite of the property is already specified
                    if ((negativePropertiesSet.contains(("-" + property.toUpperCase()))
                                && propertiesSet.contains(property))
                            || (negativePropertiesSet.contains(property)
                                    && propertiesSet.contains(property.substring(1)))) {
                        String target;
                        if (propertiesSet.contains(property)) {
                            target = "-" + property;
                        } else {
                            target = property.substring(1);
                        }
                        System.out.printf("The request contains mutually exclusive properties: [%s, %s]%n",
                                property.toUpperCase(), target.toUpperCase());
                        System.out.println("There are no numbers with these properties.");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isEven(BigInteger num) {
        return num.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }

    public static boolean isOdd(BigInteger num) {
        return !isEven(num);
    }

    public static boolean isBuzz(BigInteger num) {
        return num.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO)
                || num.mod(BigInteger.TEN).equals(BigInteger.valueOf(7));
    }

    public static boolean isDuck(BigInteger num) {
        return num.toString().chars().anyMatch(c -> (char) c == '0');
    }

    public static boolean isPalindromic(BigInteger num) {
        String digits = num.toString();
        for (int i = 0, j = digits.length() - 1; i < j; ++i, --j) {
            if (digits.charAt(i) != digits.charAt(j)) {
                    return false;
            }
        }
        return true;
    }

    public static boolean isGapful(BigInteger num) {
        if (num.compareTo(new BigInteger("100")) >= 0) {
            BigInteger concat = new BigInteger(String.valueOf(num.toString().charAt(0))
                    + num.toString().charAt(num.toString().length() - 1));
            return num.mod(concat).equals(BigInteger.ZERO);
        } else {
            return false;
        }
    }

    public static boolean isSpy(BigInteger num) {
        String digits = num.toString();
        Supplier<IntStream> supplier = () -> digits.chars().map(Character::getNumericValue);
        int sum = supplier.get().sum();
        int product = supplier.get().reduce((i, j) -> i * j).orElse(0);
        return sum == product;
    }

    public static boolean isSquare(BigInteger num) {
        BigInteger[] squareInfo = num.sqrtAndRemainder();
        return BigInteger.ZERO.equals(squareInfo[1]);
    }

    public static boolean isSunny(BigInteger num) {
        return isSquare(num.add(BigInteger.ONE));
    }

    public static boolean isJumping(BigInteger num) {
        char[] digits = num.toString().toCharArray();
        for (int i = 1; i < digits.length; ++i) {
            if (Math.abs(Character.getNumericValue(digits[i - 1]) - Character.getNumericValue(digits[i])) != 1) {
                return false;
            }
        }
        return true;
    }

    //Caches to speed up happy/sad checks
    private static final HashSet<Long> happyCache = new HashSet<>();
    private static final HashSet<Long> sadCache = new HashSet<>();

    public static boolean isHappy(BigInteger num) {
        HashSet<Long> visited = new HashSet<>();
        long n = num.longValue();
        while (true) {
            //Sequence reaches 1 on this step of loop
            if (n == 1 || happyCache.contains(n)) {
                happyCache.addAll(visited);
                return true;
            }
            //Otherwise, check if in infinite loop and otherwise calculate new value
            if (visited.contains(n) || sadCache.contains(n)) {
                //Hit an infinite loop
                sadCache.addAll(visited);
                return false;
            } else {
                visited.add(n);
                n = Long.toString(n).chars()
                        .map(Character::getNumericValue)
                        .map(i -> i * i)
                        .reduce(Integer::sum).orElseThrow();
            }
        }
    }

    public static boolean isSad(BigInteger num) {
        return !isHappy(num);
    }
}
