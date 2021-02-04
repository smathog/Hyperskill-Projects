package animals;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Wrapper w = Wrapper.getInstance();
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

        greet();
        Scanner scanner = new Scanner(System.in);

        BST bst;
        try {
            bst = new BST();
            bst.loadTree(w.fileName + fileType.name().toLowerCase(), fileType);
        } catch (IOException e) {
            //System.out.println("EXCEPTION: " + e.getMessage());
            //System.out.println();
            System.out.println(w.appResource.getString("animal.wantLearn"));
            System.out.println(w.appResource.getString("animal.askFavorite"));
            Animal favorite = LanguageRules.getAnimal(scanner);
            bst = new BST(favorite);
        }

        System.out.println();
        printToUpper(w.appResource.getString("welcome"));
        while(menu(bst, scanner, fileType));

        //System.out.println("fileName: " + w.fileName);
        bst.saveTree(w.fileName + fileType.name().toLowerCase(), fileType);
        System.out.println();
        printToUpper(getRandomElement(w.goodbyeList));
    }



    private static void greet() {
        Wrapper w = Wrapper.getInstance();
        LocalDateTime currentTime = LocalDateTime.now();
        if (5 <= currentTime.getHour() && currentTime.getHour() <= 12)
            printToUpper(w.appResource.getString("greeting.morning"));
        else if (currentTime.getHour() <= 18)
            printToUpper(w.appResource.getString("greeting.afternoon"));
        else
            printToUpper(w.appResource.getString("greeting.evening"));
    }

    private static boolean menu(BST bst, Scanner scanner, BST.FileType fileType) {
        Wrapper w = Wrapper.getInstance();
        System.out.println();
        printToUpper(w.appResource.getString("menu.property.title"));
        System.out.println();
        System.out.println("1. " + w.appResource.getString("menu.entry.play"));
        System.out.println("2. " + w.appResource.getString("menu.entry.list"));
        System.out.println("3. " + w.appResource.getString("menu.entry.search"));
        System.out.println("4. " + w.appResource.getString("menu.entry.statistics"));
        System.out.println("5. " + w.appResource.getString("menu.entry.print"));
        System.out.println("0. " + w.appResource.getString("menu.property.exit"));
        int response = Integer.parseInt(scanner.nextLine());
        switch (response) {
            case 1:
                playGame(bst, scanner, fileType);
                return true;
            case 2:
                printToUpper(w.appResource.getString("tree.list.animals"));
                bst.getOrderedAnimalSet().stream().map(a -> "- " + a).forEachOrdered(System.out::println);
                return true;
            case 3: {
                printToUpper(w.appResource.getString("animal.prompt"));
                Animal animal = LanguageRules.getAnimal(scanner);
                if (bst.hasAnimal(animal)) {
                    printToUpper(((Function<Animal, String>) w.appResource.getObject("tree.search.facts")).apply(animal));
                    bst.listFacts(animal).stream().map(s -> "- " + Character.toUpperCase(s.charAt(0)) + s.substring(1)).forEachOrdered(System.out::println);
                } else
                    printToUpper(((Function<Animal, String>) w.appResource.getObject("tree.search.noFacts")).apply(animal));
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
                printToUpper(w.appResource.getString("menu.property.error"));
                return menu(bst, scanner, fileType);
        }
    }

    private static void playGame(BST bst, Scanner scanner, BST.FileType fileType) {
        Wrapper w = Wrapper.getInstance();
        while (true) {
            playRound(bst, scanner);
            System.out.println();
            printToUpper(getRandomElement(w.againList));
            if (!validateResponse(scanner))
                break;
        }
    }

    private static void playRound(BST bst, Scanner scanner) {
        Wrapper w = Wrapper.getInstance();
        ((Runnable) w.appResource.getObject("game.entry")).run();
        scanner.nextLine();
        BST.Node activeNode = bst.getRoot();
        while (true) {
            if (activeNode.isAnimal()) {
                BST.AnimalNode guessNode = (BST.AnimalNode) activeNode;
                Animal guess = guessNode.getValue();
                printToUpper(guess.getName().replaceAll(w.appResource.getString("guessAnimal.1.pattern"), w.appResource.getString("guessAnimal.1.replace")));
                if (validateResponse(scanner))
                    printToUpper(w.appResource.getString("game.win"));
                else {
                    printToUpper(w.appResource.getString("game.giveUp"));
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
        Wrapper w = Wrapper.getInstance();
        Animal first = firstNode.getValue();
        printToUpper((((BiFunction<Animal, Animal, String>) w.appResource.getObject("statement.prompt")).apply(first, second)));
        Pattern pattern = Pattern.compile(w.appResource.getString("statement.1.pattern"), Pattern.CASE_INSENSITIVE);
        String input = scanner.nextLine().toLowerCase();
        if (!pattern.matcher(input).matches()) {
            printToUpper(w.appResource.getString("statement.error"));
            parseResponse(scanner, firstNode, second, bst);
        } else {
            printToUpper(((Function<Animal, String>) w.appResource.getObject("game.isCorrect")).apply(second));
            String question;
            //input at this point is already the relevant positive statement after trimming
            String positiveStatement = input.replaceAll(w.appResource.getString("statement.1.pattern"), w.appResource.getString("statement.1.replace"));
            String negativeStatement;
            if (input.matches(w.appResource.getString("question.1.pattern"))) {
                question = input.replaceAll(w.appResource.getString("question.1.pattern"), w.appResource.getString("question.1.replace"));
                negativeStatement = input.replaceAll(w.appResource.getString("negative.1.pattern"), w.appResource.getString("negative.1.replace"));
            } else if (input.matches(w.appResource.getString("question.2.pattern"))) {
                question = input.replaceAll(w.appResource.getString("question.2.pattern"), w.appResource.getString("question.2.replace"));
                if (input.matches(w.appResource.getString("negative.2.pattern")))
                    negativeStatement = input.replaceAll(w.appResource.getString("negative.2.pattern"), w.appResource.getString("negative.2.replace"));
                else if (input.matches(w.appResource.getString("negative.3.pattern")))
                    negativeStatement = input.replaceAll(w.appResource.getString("negative.3.pattern"), w.appResource.getString("negative.3.replace"));
                else
                    throw new IllegalArgumentException("ERROR: failed to find valid match for negativeStatement!");
            } else
                throw new IllegalArgumentException("Error: failed to match any pattern for valid response!");

            BiFunction<Animal, String, String> animalStatement = (a, statement) -> {
                String animalName = a.getName().replaceAll(w.appResource.getString("animalName.1.pattern"), w.appResource.getString("animalName.1.replace"));
                return String.format(statement.replaceAll(w.appResource.getString("animalFact.1.pattern"), w.appResource.getString("animalFact.1.replace")), animalName);
            };

            if (validateResponse(scanner)) {
                printToUpper(w.appResource.getString("game.learned"));
                bst.insertQuestion(firstNode, question, positiveStatement, negativeStatement, second, false);
                printToUpper(animalStatement.apply(second, positiveStatement));
                printToUpper(animalStatement.apply(first, negativeStatement));
            } else {
                printToUpper(w.appResource.getString("game.learned"));
                bst.insertQuestion(firstNode, question, positiveStatement, negativeStatement, second, true);
                printToUpper(animalStatement.apply(first, positiveStatement));
                printToUpper(animalStatement.apply(second, negativeStatement));
            }
            printToUpper(w.appResource.getString("game.distinguish"));
            printToUpper(question);
            printToUpper(getRandomElement(w.niceList));
        }
    }

    private static boolean validateResponse(Scanner scanner) {
        Wrapper w = Wrapper.getInstance();
        String response = stripPunctuation(scanner.nextLine());
        var b = LanguageRules.validateAnswer(response);
        while(b.isEmpty()) {
            printToUpper(getRandomElement(w.clarificationList));
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
        Wrapper w = Wrapper.getInstance();
        return list.get(w.r.nextInt(list.size()));
    }

    public static void printToUpper(String str) {
        System.out.println(Character.toUpperCase(str.charAt(0)) + str.substring(1));
    }

    public static class Wrapper {
        protected final ResourceBundle appResource = getBundle();
        protected final String fileName = appResource.getString("fileName");
        protected final ArrayList<String> clarificationList = Arrays.stream(appResource.getStringArray("ask.again"))
                .collect(Collectors.toCollection(ArrayList::new));
        protected final ArrayList<String> goodbyeList = Arrays.stream(appResource.getStringArray("farewell"))
                .collect(Collectors.toCollection(ArrayList::new));
        protected final ArrayList<String> niceList = Arrays.stream(appResource.getStringArray("animal.nice"))
                .collect(Collectors.toCollection(ArrayList::new));
        protected final ArrayList<String> againList = Arrays.stream(appResource.getStringArray("game.again"))
                .collect(Collectors.toCollection(ArrayList::new));
        protected final Random r = new Random();

        public static Wrapper getInstance() {
            return new Wrapper();
        }

        private Wrapper() {
        };

        //Not an ideal way of handling this...but I found it was the only way for the tests
        //To correctly get the right resource bundle, as opposed to using an exterior Java
        //class as a resource bundle (worked correctly on my computer but the tests couldn't
        //find the bundles when implemented that way!
        private ResourceBundle getBundle() {
            ResourceBundle messagesBundle = ResourceBundle.getBundle("messages");
            ResourceBundle patternsBundle = ResourceBundle.getBundle("patterns");
            if (messagesBundle.getString("fileName").equalsIgnoreCase("animals."))
                return new ListResourceBundle() {
                    private List<Object[]> messagesList() {
                        return Arrays.asList(
                                new Object[][]{
                                        {"fileName", messagesBundle.getString("fileName")},
                                        {"welcome", messagesBundle.getString("welcome")},
                                        {"greeting.morning", messagesBundle.getString("greeting.morning")},
                                        {"greeting.afternoon", messagesBundle.getString("greeting.afternoon")},
                                        {"greeting.evening", messagesBundle.getString("greeting.evening")},
                                        {"farewell", messagesBundle.getString("farewell").split("\\f")},
                                        {"ask.again", messagesBundle.getString("ask.again").split("\\f")},
                                        {"animal.wantLearn", messagesBundle.getString("animal.wantLearn")},
                                        {"animal.askFavorite", messagesBundle.getString("animal.askFavorite")},
                                        {"animal.nice", messagesBundle.getString("animal.nice").split("\\f")},
                                        {"animal.prompt", messagesBundle.getString("animal.prompt")},
                                        {"animal.error", messagesBundle.getString("animal.error")},
                                        {"statement.prompt", (BiFunction<Animal, Animal, String>) (a1, a2) -> MessageFormat.format(messagesBundle.getString("statement.prompt"),
                                                a1.getName(),
                                                a2.getName())},
                                        {"statement.error", messagesBundle.getString("statement.error")},
                                        {"game.entry", (Runnable) () -> {
                                            System.out.println(messagesBundle.getString("game.letsPlay"));
                                            System.out.println(messagesBundle.getString("game.think"));
                                            System.out.println(messagesBundle.getString("game.enter"));
                                        }},
                                        {"game.win", messagesBundle.getString("game.win")},
                                        {"game.giveUp", messagesBundle.getString("game.giveUp")},
                                        {"game.isCorrect", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("game.isCorrect"), a.getName())},
                                        {"game.learned", messagesBundle.getString("game.learned")},
                                        {"game.distinguish", messagesBundle.getString("game.distinguish")},
                                        {"game.again", messagesBundle.getString("game.again").split("\\f")},
                                        {"menu.property.title", messagesBundle.getString("menu.property.title")},
                                        {"menu.property.exit", messagesBundle.getString("menu.property.exit")},
                                        {"menu.property.error", messagesBundle.getString("menu.property.error")},
                                        {"menu.entry.play", messagesBundle.getString("menu.entry.play")},
                                        {"menu.entry.list", messagesBundle.getString("menu.entry.list")},
                                        {"menu.entry.search", messagesBundle.getString("menu.entry.search")},
                                        {"menu.entry.statistics", messagesBundle.getString("menu.entry.statistics")},
                                        {"menu.entry.print", messagesBundle.getString("menu.entry.print")},
                                        {"tree.list.animals", messagesBundle.getString("tree.list.animals")},
                                        {"tree.search.facts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.facts"), a.getNameWithoutArticle())},
                                        {"tree.search.noFacts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.noFacts"), a.getNameWithoutArticle())},
                                        {"tree.stats.title", messagesBundle.getString("tree.stats.title")},
                                        {"tree.stats.root", (Function<String, String>) s -> MessageFormat.format(messagesBundle.getString("tree.stats.root"), s)},
                                        {"tree.stats.nodes", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.nodes"), i)},
                                        {"tree.stats.animals", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.animals"), i)},
                                        {"tree.stats.statements", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.statements"), i)},
                                        {"tree.stats.height", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.height"), i)},
                                        {"tree.stats.minimum", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.minimum"), i)},
                                        {"tree.stats.average", (Function<Double, String>) d -> MessageFormat.format(messagesBundle.getString("tree.stats.average"), d)}
                                }
                        );
                    }

                    private List<Object[]> patternList() {
                        return patternsBundle.keySet().stream()
                                .map(s -> new Object[]{s, patternsBundle.getString(s)})
                                .collect(Collectors.toList());
                    }

                    @Override
                    protected Object[][] getContents() {
                        return Stream.of(messagesList(), patternList())
                                .flatMap(l -> l.stream())
                                .collect(Collectors.toList())
                                .toArray(new Object[][]{});
                    }
                };
            else if (messagesBundle.getString("fileName").equalsIgnoreCase("animals_eo."))
                return new ListResourceBundle() {
                    private List<Object[]> messagesList() {
                        return Arrays.asList(
                                new Object[][]{
                                        {"fileName", messagesBundle.getString("fileName")},
                                        {"welcome", messagesBundle.getString("welcome")},
                                        {"greeting.morning", messagesBundle.getString("greeting.morning")},
                                        {"greeting.afternoon", messagesBundle.getString("greeting.afternoon")},
                                        {"greeting.evening", messagesBundle.getString("greeting.evening")},
                                        {"farewell", messagesBundle.getString("farewell").split("\\f")},
                                        {"ask.again", messagesBundle.getString("ask.again").split("\\f")},
                                        {"animal.wantLearn", messagesBundle.getString("animal.wantLearn")},
                                        {"animal.askFavorite", messagesBundle.getString("animal.askFavorite")},
                                        {"animal.nice", messagesBundle.getString("animal.nice").split("\\f")},
                                        {"animal.prompt", messagesBundle.getString("animal.prompt")},
                                        {"animal.error", messagesBundle.getString("animal.error")},
                                        {"statement.prompt", (BiFunction<Animal, Animal, String>) (a1, a2) -> MessageFormat.format(messagesBundle.getString("statement.prompt"),
                                                a1.getName(),
                                                a2.getName())},
                                        {"statement.error", messagesBundle.getString("statement.error")},
                                        {"game.entry", (Runnable) () -> {
                                            System.out.println(messagesBundle.getString("game.letsPlay"));
                                            System.out.println(messagesBundle.getString("game.think"));
                                            System.out.println(messagesBundle.getString("game.enter"));
                                            System.out.println();
                                        }},
                                        {"game.win", messagesBundle.getString("game.win")},
                                        {"game.giveUp", messagesBundle.getString("game.giveUp")},
                                        {"game.isCorrect", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("game.isCorrect"), a.getName())},
                                        {"game.learned", messagesBundle.getString("game.learned")},
                                        {"game.distinguish", messagesBundle.getString("game.distinguish")},
                                        {"game.again", messagesBundle.getString("game.again").split("\\f")},
                                        {"menu.property.title", messagesBundle.getString("menu.property.title")},
                                        {"menu.property.exit", messagesBundle.getString("menu.property.exit")},
                                        {"menu.property.error", messagesBundle.getString("menu.property.error")},
                                        {"menu.entry.play", messagesBundle.getString("menu.entry.play")},
                                        {"menu.entry.list", messagesBundle.getString("menu.entry.list")},
                                        {"menu.entry.search", messagesBundle.getString("menu.entry.search")},
                                        {"menu.entry.statistics", messagesBundle.getString("menu.entry.statistics")},
                                        {"menu.entry.print", messagesBundle.getString("menu.entry.print")},
                                        {"tree.list.animals", messagesBundle.getString("tree.list.animals")},
                                        {"tree.search.facts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.facts"), a.getName())},
                                        {"tree.search.noFacts", (Function<Animal, String>) a -> MessageFormat.format(messagesBundle.getString("tree.search.noFacts"), a.getName())},
                                        {"tree.stats.title", messagesBundle.getString("tree.stats.title")},
                                        {"tree.stats.root", (Function<String, String>) s -> MessageFormat.format(messagesBundle.getString("tree.stats.root"), s)},
                                        {"tree.stats.nodes", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.nodes"), i)},
                                        {"tree.stats.animals", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.animals"), i)},
                                        {"tree.stats.statements", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.statements"), i)},
                                        {"tree.stats.height", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.height"), i)},
                                        {"tree.stats.minimum", (Function<Integer, String>) i -> MessageFormat.format(messagesBundle.getString("tree.stats.minimum"), i)},
                                        {"tree.stats.average", (Function<Double, String>) d -> MessageFormat.format(messagesBundle.getString("tree.stats.average"), d)}
                                }
                        );
                    }

                    private List<Object[]> patternList() {
                        return patternsBundle.keySet().stream()
                                .map(s -> new Object[]{s, patternsBundle.getString(s)})
                                .collect(Collectors.toList());
                    }

                    @Override
                    protected Object[][] getContents() {
                        return Stream.of(messagesList(), patternList())
                                .flatMap(l -> l.stream())
                                .collect(Collectors.toList())
                                .toArray(new Object[][]{});
                    }
                };
            else
                throw new IllegalStateException("Program cannot be invoked unless in en or eo locale");
        }
    }
}
