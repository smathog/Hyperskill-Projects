package calculator;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Calculator {
    private static Scanner scanner = new Scanner(System.in);
    private static Pattern validVariablePattern = Pattern.compile("[a-zA-Z]+");
    private static Pattern validOperationspattern = Pattern.compile("-|\\+|=");
    private static Pattern validExpresionPattern = Pattern.compile(
            "(\\/[a-zA-Z]+)|(([\\+-] )?([a-zA-Z]+|[0-9]+)( [\\+\\-=] ([a-zA-Z]+|[0-9]+))*)"
    );

    private HashMap<String, Integer> variables;

    public Calculator() {
        variables = new HashMap<>();
    }

    public void execute() {
        executionLoop:
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;
            String expression = processString(input);
            if (!validExpresionPattern.matcher(expression).matches()) {
                System.out.println("Invalid expression");
                continue;
            }
            String[] args = expression.split(" ");
            if (args.length == 1) {
                if (args[0] == null || args[0].isEmpty())
                    continue;
                else if (parseSingleArgument(args[0]))
                    break executionLoop;
            } else {
                ArrayList<String> argList = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
                if (stripPrepend(argList))
                    operate(argList);
                else
                    continue;
            }
        }
    }

    private boolean isValidVariableName(String name) {
        return validVariablePattern.matcher(name).matches();
    }

    private boolean parseSingleArgument(String arg) {
        //If command
        if (arg.matches("\\/.*")) {
            if ("/help".equals(arg))
                System.out.println("The program calculates the sum and difference of numbers");
            else if ("/exit".equals(arg)) {
                System.out.println("Bye!");
                return true;
            } else
                System.out.println("Unknown command");
        } else
            processSingleArg(arg);
        return false;
    }

    private void processSingleArg(String arg) {
        if (!isValidVariableName(arg)) {
            //Might be a number
            try {
                System.out.println(Integer.parseInt(arg));
            } catch (NumberFormatException e){
                //If not, treat as bad variable
                System.out.println("Invalid identifier");
            }
        } else if (!variables.containsKey(arg))
            System.out.println("Unknown variable");
        else //both a valid variable name and known
            System.out.println(variables.get(arg));
    }

    private String processString(String line) {
        line = line.replaceAll("\\++", " + ");
        line = line.replaceAll("(--){1,}-", "-");
        line = line.replaceAll("(--)+", " + ");
        line = line.replaceAll("-", " - ");
        line = line.replaceAll("=+", " = ");
        line = line.replaceAll("\\s+", " ");
        line = line.trim();

        return line;
    }

    private boolean isInteger(String temp) {
        try {
            Integer.parseInt(temp);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean stripPrepend(ArrayList<String> args) {
        //Deal with annoying prepend
        if ("+".equals(args.get(0)))
            args.remove(0);
        else if ("-".equals(args.get(0))) {
            args.remove(0);
            String first = args.get(0);
            if (isInteger(first))
                args.set(0, Integer.toString(-1 * Integer.parseInt(first)));
            else { //must be a variable, either way illegal
                System.out.println("Invalid expression");
                return false;
            }
        }
        return true;
    }

    //Parses an expression w/o assignment
    private Integer parseSimple(ArrayList<String> args) {
        Integer current = 0;
        String operation = null;
        for (int i = 0; i < args.size(); ++i) {
            String s = args.get(i);
            //If operation
            if (validOperationspattern.matcher(s).matches()) {
                //Should already be caught before, but just in case...
                if (i == args.size() - 1) {
                    System.out.println("Invalid Expression");
                    return null;
                }

                if (operation == null)
                    operation = s;
                else if ("-".equals(operation) && "+".equals(s))
                    continue;
                else if ("-".equals(operation) && "-".equals(s))
                    operation = "+";
                else if ("+".equals(operation) && "+".equals(s))
                    continue;
                else if ("+".equals(operation) && "-".equals(s))
                    operation = "-";
            }
            //Else, variable or Integer; perform operation if defined
            else {
                Integer newVal = null;
                if (isInteger(s)) {
                    if (i == 0) {
                        current = Integer.parseInt(s);
                        continue;
                    } else
                        newVal = Integer.parseInt(s);
                } else if (validVariablePattern.matcher(s).matches()) {
                    if (variables.containsKey(s)) {
                        if (i == 0) {
                            current = variables.get(s);
                            continue;
                        } else
                            newVal = variables.get(s);
                    } else {
                        System.out.println("Unknown variable");
                        return null;
                    }
                } else {
                    System.out.println("Invalid identifier");
                    return null;
                }
                if (operation != null) {
                    switch (operation) {
                        case "+":
                            current = current + newVal;
                            operation = null;
                            break;
                        case "-":
                            current = current - newVal;
                            operation = null;
                            break;
                    }
                }
            }
        }
        return current;
    }

    private void operate(ArrayList<String> args) {
        //First, parse args and see if it is a simple expression
        boolean containsAssignment = false;
        boolean print = true;

        for (String s : args)
            if ("=".equals(s)) {
                containsAssignment = true;
                break;
            }

        if (containsAssignment) {
            //Don't print when assigning
            print = false;

            //Identify indices of assignment and handle
            for (int i = args.size() - 1; i >= 0; --i) {
                if ("=".equals(args.get(i))) {
                    //Validate legal position for =
                    if (i == args.size() - 1 || i == 0) {
                        System.out.println("Invalid Expression");
                        return;
                    }

                    //Ensure args.get(i - 1) is a legal value for assignment
                    if (isInteger(args.get(i - 1))) {
                        System.out.println("Invalid Assignment");
                        return;
                    } else if (!validVariablePattern.matcher(args.get(i - 1)).matches()) {
                        System.out.println("Invalid identifier");
                        return;
                    }

                    //Parse right hand side
                    Integer rhs = parseSimple(new ArrayList<>(args.subList(i + 1, args.size())));
                    if (rhs == null)
                        return;
                    else {
                        variables.put(args.get(i - 1), rhs);
                        //Just get rid of everything after i
                        args.subList(i, args.size()).clear();
                    }
                }
            }
        } else {
            //Simple expression, so just evaluate immediately
            Integer result = parseSimple(args);
            if (result != null)
                System.out.println(result);
        }
    }
}
