package animals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final ResourceBundle appResource = ResourceBundle.getBundle("App");
    private static final String fileName = appResource.getString("fileName");
    private static final ArrayList<String> clarificationList = Arrays.stream(appResource.getStringArray("ask.again"))
            .collect(Collectors.toCollection(ArrayList::new));
    private static final ArrayList<String> goodbyeList = Arrays.stream(appResource.getStringArray("farewell"))
            .collect(Collectors.toCollection(ArrayList::new));
    private static final ArrayList<String> niceList = Arrays.stream(appResource.getStringArray("animal.nice"))
            .collect(Collectors.toCollection(ArrayList::new));
    private static final ArrayList<String> againList = Arrays.stream(appResource.getStringArray("game.again"))
            .collect(Collectors.toCollection(ArrayList::new));
    private static final Random r = new Random();

    public static void main(String[] args) {
        BST.FileType fileType;
        if (args.length != 2)
            fileType = BST.FileType.JSON;
        else {
            switch (args[1].toLowerCase()) {
                case "xml":
                    fileType = BST.FileType.XML;
                    break;
                case "yaml":
                    fileType = BST.FileType.YAML;
                    break;
                case "json":
                default:
                    fileType = BST.FileType.JSON;
                    break;
            }
        }

        //greet();
        Scanner scanner = new Scanner(System.in);

        BST bst;
        try {
            bst = new BST();
            bst.loadTree(fileName + fileType.name().toLowerCase(), fileType);
        } catch (IOException e) {
            //System.out.println("EXCEPTION: " + e.getMessage());
            //System.out.println();
            System.out.println(appResource.getString("animal.wantLearn"));
            System.out.println(appResource.getString("animal.askFavorite"));
            Animal favorite = LanguageRules.getAnimal(scanner);
            bst = new BST(favorite);
        }

        System.out.println();
        printToUpper(appResource.getString("welcome"));
        while(menu(bst, scanner, fileType));

        bst.saveTree(fileName + fileType.name().toLowerCase(), fileType);
        System.out.println();
        printToUpper(getRandomElement(goodbyeList));
    }



    private static void greet() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (5 <= currentTime.getHour() && currentTime.getHour() <= 12)
            printToUpper(appResource.getString("greeting.morning"));
        else if (currentTime.getHour() <= 18)
            printToUpper(appResource.getString("greeting.afternoon"));
        else
            printToUpper(appResource.getString("greeting.evening"));
    }

    private static boolean menu(BST bst, Scanner scanner, BST.FileType fileType) {
        System.out.println();
        printToUpper(appResource.getString("menu.property.title"));
        System.out.println();
        System.out.println("1. " + appResource.getString("menu.entry.play"));
        System.out.println("2. " + appResource.getString("menu.entry.list"));
        System.out.println("3. " + appResource.getString("menu.entry.search"));
        System.out.println("4. " + appResource.getString("menu.entry.statistics"));
        System.out.println("5. " + appResource.getString("menu.entry.print"));
        System.out.println("0. " + appResource.getString("menu.property.exit"));
        int response = Integer.parseInt(scanner.nextLine());
        switch (response) {
            case 1:
                playGame(bst, scanner, fileType);
                return true;
            case 2:
                printToUpper(appResource.getString("tree.list.animals"));
                bst.getOrderedAnimalSet().stream().map(a -> "- " + a).forEachOrdered(System.out::println);
                return true;
            case 3: {
                printToUpper(appResource.getString("animal.prompt"));
                Animal animal = LanguageRules.getAnimal(scanner);
                if (bst.hasAnimal(animal)) {
                    printToUpper(((Function<Animal, String>) appResource.getObject("tree.search.facts")).apply(animal));
                    bst.listFacts(animal).stream().map(s -> "- " + Character.toUpperCase(s.charAt(0)) + s.substring(1)).forEachOrdered(System.out::println);
                } else
                    printToUpper(((Function<Animal, String>) appResource.getObject("tree.search.noFacts")).apply(animal));
                return true;
            }
            case 4:
                bst.getStats();
                return true;
            case 5:
                System.out.println(bst);
                return true;
            case 0:
                return false;
            default:
                printToUpper(appResource.getString("menu.property.error"));
                return menu(bst, scanner, fileType);
        }
    }

    private static void playGame(BST bst, Scanner scanner, BST.FileType fileType) {
        while (true) {
            playRound(bst, scanner);
            System.out.println();
            printToUpper(getRandomElement(againList));
            if (!validateResponse(scanner))
                break;
        }
    }

    private static void playRound(BST bst, Scanner scanner) {
        ((Runnable) appResource.getObject("game.entry")).run();
        scanner.nextLine();
        BST.Node activeNode = bst.getRoot();
        while (true) {
            if (activeNode.isAnimal()) {
                BST.AnimalNode guessNode = (BST.AnimalNode) activeNode;
                Animal guess = guessNode.getValue();
                printToUpper(guess.getName().replaceAll(appResource.getString("guessAnimal.1.pattern"), appResource.getString("guessAnimal.1.replace")));
                if (validateResponse(scanner))
                    printToUpper(appResource.getString("game.win"));
                else {
                    printToUpper(appResource.getString("game.giveUp"));
                    Animal otherAnimal = LanguageRules.getAnimal(scanner);
                    parseResponse(scanner, guessNode, otherAnimal, bst);
                }
                break;
            } else {
                BST.QuestionNode questionNode = (BST.QuestionNode) activeNode;
                printToUpper(questionNode.getValue());
                if (validateResponse(scanner))
                    activeNode = questionNode.getYesNode();
                else
                    activeNode = questionNode.getNoNode();
            }
        }
    }

    private static void parseResponse(Scanner scanner, BST.AnimalNode firstNode, Animal second, BST bst) {
        Animal first = firstNode.getValue();
        printToUpper((((BiFunction<Animal, Animal, String>) appResource.getObject("statement.prompt")).apply(first, second)));
        Pattern pattern = Pattern.compile(appResource.getString("statement.1.pattern"), Pattern.CASE_INSENSITIVE);
        String input = scanner.nextLine().toLowerCase();
        if (!pattern.matcher(input).matches()) {
            printToUpper(appResource.getString("statement.error"));
            parseResponse(scanner, firstNode, second, bst);
        } else {
            printToUpper(((Function<Animal, String>) appResource.getObject("game.isCorrect")).apply(second));
            String question;
            //input at this point is already the relevant positive statement after trimming
            String positiveStatement = input.replaceAll(appResource.getString("statement.1.pattern"), appResource.getString("statement.1.replace"));
            String negativeStatement;
            if (input.matches(appResource.getString("question.1.pattern"))) {
                System.out.println("MATCHED question.1.pattern");
                question = input.replaceAll(appResource.getString("question.1.pattern"), appResource.getString("question.1.replace"));
                negativeStatement = input.replaceAll(appResource.getString("negative.1.pattern"), appResource.getString("negative.1.replace"));
            } else if (input.matches(appResource.getString("question.2.pattern"))) {
                System.out.println("MATCHED question.2.pattern");
                question = input.replaceAll(appResource.getString("question.2.pattern"), appResource.getString("question.2.replace"));
                if (input.matches(appResource.getString("negative.2.pattern")))
                    negativeStatement = input.replaceAll(appResource.getString("negative.2.pattern"), appResource.getString("negative.2.replace"));
                else if (input.matches(appResource.getString("negative.3.pattern")))
                    negativeStatement = input.replaceAll(appResource.getString("negative.3.pattern"), appResource.getString("negative.3.replace"));
                else
                    throw new IllegalArgumentException("ERROR: failed to find valid match for negativeStatement!");
            } else
                throw new IllegalArgumentException("Error: failed to match any pattern for valid response!");

            BiFunction<Animal, String, String> animalStatement = (a, statement) -> {
                String animalName = a.getName().replaceAll(appResource.getString("animalName.1.pattern"), appResource.getString("animalName.1.replace"));
                return String.format(statement.replaceAll(appResource.getString("animalFact.1.pattern"), appResource.getString("animalFact.1.replace")), animalName);
            };

            printToUpper(appResource.getString("game.learned"));
            if (validateResponse(scanner)) {
                bst.insertQuestion(firstNode, question, positiveStatement, negativeStatement, second, false);
                printToUpper(animalStatement.apply(second, positiveStatement));
                printToUpper(animalStatement.apply(first, negativeStatement));
            } else {
                bst.insertQuestion(firstNode, question, positiveStatement, negativeStatement, second, true);
                printToUpper(animalStatement.apply(first, positiveStatement));
                printToUpper(animalStatement.apply(second, negativeStatement));
            }
            printToUpper(appResource.getString("game.distinguish"));
            printToUpper(question);
            printToUpper(getRandomElement(niceList));
        }
    }

    private static boolean validateResponse(Scanner scanner) {
        String response = stripPunctuation(scanner.nextLine());
        var b = LanguageRules.validateAnswer(response);
        while(b.isEmpty()) {
            printToUpper(getRandomElement(clarificationList));
            response = stripPunctuation(scanner.nextLine());
            b = LanguageRules.validateAnswer(response);
        }
        return b.get();
    }

    private static String stripPunctuation(String s) {
        s = s.strip();
        if (s.endsWith(".") || s.endsWith("!"))
            return s.substring(0, s.length() - 1);
        else
            return s;
    }

    private static String getRandomElement(List<String> list) {
        return list.get(r.nextInt(list.size()));
    }

    public static void printToUpper(String str) {
        System.out.println(Character.toUpperCase(str.charAt(0)) + str.substring(1));
    }

    public static ResourceBundle getAppResource() {
        return appResource;
    }
}
