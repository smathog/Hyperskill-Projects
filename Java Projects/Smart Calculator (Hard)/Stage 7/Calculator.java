package calculator;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Calculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern validVariablePattern = Pattern.compile("[a-zA-Z]+");
    private static final Pattern validOperationspattern = Pattern.compile("-|\\+|=|\\*|\\/");
    private static final Pattern parenthesesPattern = Pattern.compile("\\(|\\)");
    private static final Pattern invalidOperatorPattern = Pattern.compile("\\*{2,}|\\/{2,}");
    private static final Pattern commandPattern = Pattern.compile("/[a-zA-Z]+");

    private final HashMap<String, Integer> variables;

    public Calculator() {
        variables = new HashMap<>();
    }

    public void execute() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (isCommand(input)) {
                try {
                    if (parseCommand(input))
                        break;
                    else
                        continue;
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
            if (input.isEmpty())
                continue;
            if (invalidOperatorPattern.matcher(input).find()) {
                System.out.println("Invalid expression");
                continue;
            }
            String expression = processString(input);

            //TEST CODE
            //System.out.println(expression);

            String[] args = expression.split(" ");
            ArrayList<String> argList = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));

            //TEST CODE
            /*
            System.out.println("TEST CODE");
            ArrayList<String> postfix = toPostfix(argList);
            System.out.println(postfix);
            Integer result = parsePostix(postfix);
            System.out.println(result);
            */

            operate(argList);
        }
    }

    private static boolean isValidVariableName(String name) {
        return validVariablePattern.matcher(name).matches();
    }

    private boolean isDefinedVariable(String name) {
        return variables.containsKey(name);
    }

    private static boolean isOperator(String op) {
        return validOperationspattern.matcher(op).matches();
    }

    private static boolean isParentheses(String str) {
        return parenthesesPattern.matcher(str).matches();
    }

    private static boolean isCommand(String str) {
        return commandPattern.matcher(str).matches();
    }

    private static boolean parseCommand(String command) throws IllegalArgumentException {
        if ("/help".equals(command)) {
            System.out.println("This is a calculator.");
            return false;
        }
        else if ("/exit".equals(command)) {
            System.out.println("Bye!");
            return true;
        } else {
            System.out.println("Unknown command");
            throw new IllegalArgumentException("Unknown command");
        }
    }

    private String processString(String line) {
        line = line.replaceAll("\\++", " + ");
        line = line.replaceAll("(--){1,}-", "-");
        line = line.replaceAll("(--)+", " + ");
        line = line.replaceAll("-", " - ");
        line = line.replaceAll("=+", " = ");
        line = line.replaceAll("\\(", " ( ");
        line = line.replaceAll("\\)", " ) ");
        line = line.replaceAll("\\*", " * ");
        line = line.replaceAll("\\/", " / ");
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

    private void operate(ArrayList<String> args) {
        //First, parse args and see if it is a simple expression
        boolean containsAssignment = false;

        for (String s : args)
            if ("=".equals(s)) {
                containsAssignment = true;
                break;
            }

        if (containsAssignment) {
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
                    ArrayList<String> postfix = toPostfix(new ArrayList<>(args.subList(i + 1, args.size())));
                    if (postfix == null)
                        return;
                    Integer rhs = parsePostix(postfix);
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
            ArrayList<String> postfix = toPostfix(args);
            if (postfix == null)
                return;
            Integer result = parsePostix(postfix);
            if (result != null)
                System.out.println(result);
        }
    }

    //Returns true if operator1 has lower precedence than operator2, false otherwise
    private boolean operatorPrecedes(String operator1, String operator2) {
        if (!validOperationspattern.matcher(operator1).matches())
            throw new IllegalArgumentException("Invalid operator passed!");
        if (!validOperationspattern.matcher(operator2).matches())
            throw new IllegalArgumentException("Invalid operator passed!");

        if ("*".equals(operator2) || "/".equals(operator2)) {
            if ("+".equals(operator1) || "-".equals(operator1))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    //NOTE: List *must* not contain assignments or this won't work
    private ArrayList<String> toPostfix(ArrayList<String> list) {
        ArrayList<String> result = new ArrayList<>();
        ArrayDeque<String>  operatorStack = new ArrayDeque<>();
        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            //If operand
            if (isInteger(s))
                result.add(s);
            else if (!isOperator(s) && !isParentheses(s)) {
                if (isValidVariableName(s) && isDefinedVariable(s))
                    result.add(s);
                else if (!isValidVariableName(s)) {
                    System.out.println("Invalid identifier");
                    return null;
                } else { //unknown variable
                    System.out.println("Unknown variable");
                    return null;
                }
            }

            //If operator
            if (isOperator(s)) {
                //SPECIAL HANDLING FOR UNARY + and -
                if (i == 0 || isOperator(list.get(i - 1)) || "(".equals(list.get(i - 1))) {
                    //Can just append immediately since highest precedence
                    if (i == list.size() - 1 || !(isInteger(list.get(i + 1)) || isDefinedVariable(list.get(i + 1)))) {
                        System.out.println("Invalid expression");
                        return null;
                    }
                    result.add(list.get(i + 1));
                    if ("+".equals(s))
                        result.add("@");
                    else if ("-".equals(s))
                        result.add("#");
                    ++i;
                    continue;
                }


                if (operatorStack.isEmpty() || "(".equals(operatorStack.peekFirst())) {
                    operatorStack.offerFirst(s);
                } else {
                    if (operatorPrecedes(operatorStack.peekFirst(), s))
                        operatorStack.offerFirst(s);
                    else {
                        while (!operatorStack.isEmpty() && !(operatorStack.peekFirst().equals("(") || operatorPrecedes(operatorStack.peekFirst(), s)))
                            result.add(operatorStack.pollFirst());
                        operatorStack.offerFirst(s);
                    }
                }
            }

            //If parentheses
            if (isParentheses(s)) {
                if ("(".equals(s))
                    operatorStack.offerFirst(s);
                else {
                    while (!operatorStack.isEmpty() && !operatorStack.peekFirst().equals("("))
                        result.add(operatorStack.pollFirst());
                    if (operatorStack.isEmpty()) {
                        System.out.println("Invalid expression");
                        return null;
                    } else if (operatorStack.peekFirst().equals("("))
                        operatorStack.pollFirst();
                }
            }
        }

        //Add remaining stack elements to result, if any left paren, syntax error
        while (!operatorStack.isEmpty()) {
            String s = operatorStack.pollFirst();
            if ("(".equals(s)) {
                System.out.println("Invalid expression");
                return null;
            } else
                result.add(s);
        }

        return result;
    }

    private Integer parsePostix(ArrayList<String> postfix) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (String s : postfix) {
            //unary operators first
            if ("#".equals(s) || "@".equals(s)) {
                Integer top = stack.pollFirst();
                switch (s) {
                    case "@":
                        stack.offerFirst(top);
                        break;
                    case "#":
                        stack.offerFirst(-1 * top);
                        break;
                }
            } else if (isOperator(s)) {
                Integer second = stack.pollFirst();
                Integer first = stack.pollFirst();
                switch (s) {
                    case "*":
                        stack.offerFirst(first * second);
                        break;
                    case "/":
                        if (second == 0) {
                            System.out.println("Invalid expression");
                            return null;
                        }
                        stack.offerFirst(first / second);
                        break;
                    case "+":
                        stack.offerFirst(first + second);
                        break;
                    case "-":
                        stack.offerFirst(first - second);
                        break;
                }
            } else {
               if (isInteger(s))
                   stack.offerFirst(Integer.parseInt(s));
               else if (isValidVariableName(s)) {
                   if (isDefinedVariable(s))
                       stack.offerFirst(variables.get(s));
                   else {
                       System.out.println("Undefined variable!");
                       return null;
                   }
               } else {
                   System.out.println("Invalid idetnfier");
                   return null;
               }
            }
        }

        return stack.pollFirst();
    }
}
