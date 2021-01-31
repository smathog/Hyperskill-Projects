package animals;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final HashSet<Character> anSet = Stream.of('a', 'e', 'i', 'o', 'u')
                .flatMap(c -> Stream.of(c, Character.toUpperCase(c)))
                .collect(Collectors.toCollection(HashSet::new));
    private static final HashMap<String, String> responseMap;
    private static final ArrayList<String> clarificationList = Stream.of(
            "I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no.")
            .collect(Collectors.toCollection(ArrayList::new));
    private static final ArrayList<String> goodbyeList = Stream.of(
            "Goodbye!",
            "Bye!",
            "Later!",
            "Ciao!",
            "See ya!")
            .collect(Collectors.toCollection(ArrayList::new));
    private static final HashMap<String, String[]> modals = Stream.of(
            new String[]{"can", "can't", "Can it"},
            new String[]{"has", "doesn't have", "Does it have"},
            new String[]{"is a", "isn't a", "Is it a"},
            new String[]{"is an", "isn't an", "Is it an"},
            new String[]{"is", "isn't", "Is it"})
            .collect(Collectors.toMap(
                    s -> s[0],
                    Function.identity(),
                    (s, v) -> s,
                    HashMap::new
            ));

    static {
        responseMap = new HashMap<>();
        Stream.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct",
                "indeed", "you bet", "exactly", "you said it").forEachOrdered(s -> responseMap.put(s, "Yes"));
        Stream.of("n", "no", "no way", "nah", "nope", "negative", "i don't think so", "yeah no")
                .forEachOrdered(s -> responseMap.put(s, "No"));
    }

    public static void main(String[] args) {
        greet();
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        System.out.println("I want to learn about animals.");
        System.out.println("Which animal do you like most?");
        Animal favorite = new Animal(scanner.nextLine());
        System.out.println("Wonderful! I've learned so much about animals!");
        BST bst = new BST(favorite);
        while (true) {
            playRound(bst, scanner);
            System.out.println();
            System.out.println("Would you like to play again?");
            String response = validateResponse(scanner);
            if ("No".equalsIgnoreCase(responseMap.get(response.toLowerCase())))
                break;
        }


        System.out.println();
        System.out.println(goodbyeList.get(new Random().nextInt(goodbyeList.size())));
    }

    private static void greet() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (5 <= currentTime.getHour() && currentTime.getHour() <= 12)
            System.out.println("Good morning!");
        else if (currentTime.getHour() <= 18)
            System.out.println("Good afternoon!");
        else
            System.out.println("Good evening!");
    }

    private static void playRound(BST bst, Scanner scanner) {
        System.out.println("Let's play a game!");
        System.out.println("You think of an animal, and I guess it.");
        System.out.println("Press enter when you're ready");
        scanner.nextLine();
        BST.Node activeNode = bst.getRoot();
        while (true) {
            if (activeNode.isAnimal()) {
                BST.AnimalNode guessNode = (BST.AnimalNode) activeNode;
                Animal guess = guessNode.getValue();
                System.out.println("Is it " + guess.getArticle() + " " + guess + "?");
                String response = validateResponse(scanner);
                if ("Yes".equalsIgnoreCase(responseMap.get(response.toLowerCase())))
                    System.out.println("WOOHOO I'M CORRECT YAY!");
                else {
                    System.out.println("I give up. What animal do you have in mind?");
                    Animal otherAnimal = new Animal(scanner.nextLine());
                    parseResponse(scanner, guessNode, otherAnimal, bst);
                }
                break;
            } else {
                BST.QuestionNode questionNode = (BST.QuestionNode) activeNode;
                System.out.println(questionNode.getValue());
                String response = validateResponse(scanner);
                if ("Yes".equalsIgnoreCase(responseMap.get(response.toLowerCase())))
                    activeNode = questionNode.getYesNode();
                else
                    activeNode = questionNode.getNoNode();
            }
        }
    }

    private static void parseResponse(Scanner scanner, BST.AnimalNode firstNode, Animal second, BST bst) {
        Animal first = firstNode.getValue();
        System.out.println("Specify a fact that distinguishes " + first.getArticle() + " " + first + " from " + second.getArticle() + " " + second + ".");
        System.out.println("The sentence should satisfy one of the following templates:");
        System.out.println("- It can ...");
        System.out.println("- It has ...");
        System.out.println("- It is a/an ...");
        System.out.println();
        Pattern pattern = Pattern.compile("It ((can)|(has)|(is( an?)?)) .*", Pattern.CASE_INSENSITIVE);
        String input = scanner.nextLine();
        if (!pattern.matcher(input).matches()) {
            System.out.println("The examples of a statement: ");
            System.out.println("- It can fly.");
            System.out.println("- It has horn.");
            System.out.println("- It is a mammal.");
            parseResponse(scanner, firstNode, second, bst);
        } else {
            Pattern mPattern = Pattern.compile("(can)|(has)|(is( an?)?)", Pattern.CASE_INSENSITIVE);
            Matcher mMatcher = mPattern.matcher(input);
            mMatcher.find();
            String modalVerb = input.substring(mMatcher.start(), mMatcher.end()).toLowerCase();
            String ability = input.substring(mMatcher.end()).trim().toLowerCase();
            System.out.println("Is the statement correct for " + second.getArticle() + " " + second + "?");
            String response = validateResponse(scanner);
            String question = modals.get(modalVerb)[2] + " " + ability + "?";
            System.out.println("I have learned the following facts about animals:");
            if ("Yes".equalsIgnoreCase(responseMap.get(response.toLowerCase()))) {
                bst.insertQuestion(firstNode, question, second, false);
                System.out.println("- The " + second + " " + modalVerb + " " + ability + ".");
                System.out.println("- The " + first + " " + modals.get(modalVerb)[1] + " " + ability + ".");
            } else {
                bst.insertQuestion(firstNode, question, second, true);
                System.out.println("- The " + first + " " + modalVerb + " " + ability + ".");
                System.out.println("- The " + second + " " + modals.get(modalVerb)[1] + " " + ability + ".");
            }
            System.out.println("I can distinguish between these animals by asking the question: ");
            System.out.println(question);
            System.out.println("Nice! I've learned so much about animals!");
        }
    }

    private static String validateResponse(Scanner scanner) {
        String response = stripPunctuation(scanner.nextLine());
        while (!responseMap.containsKey(response.toLowerCase())) {
            System.out.println(clarificationList.get(new Random().nextInt(clarificationList.size())));
            response = stripPunctuation(scanner.nextLine());
        }
        return response;
    }

    private static String stripPunctuation(String s) {
        s = s.strip();
        if (s.endsWith(".") || s.endsWith("!"))
            return s.substring(0, s.length() - 1);
        else
            return s;
    }

    public static HashSet<Character> getAnSet() {
        return anSet;
    }
}
