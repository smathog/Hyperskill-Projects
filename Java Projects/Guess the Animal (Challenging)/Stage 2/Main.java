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

        System.out.println("Enter the first animal: ");
        Animal first = new Animal(scanner.nextLine());
        System.out.println("Enter the second animal: ");
        Animal second = new Animal(scanner.nextLine().toLowerCase());
        String question = parseResponse(scanner, first, second);
        System.out.println("I have learned the following facts about animals: ");
        System.out.println("- The " + first + " " + first.getAbility() + ".");
        System.out.println("- The " + second + " " + second.getAbility() + ".");
        System.out.println("I can distinguish these animals by asking the question: ");
        System.out.println("- " + question);

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

    private static String parseResponse(Scanner scanner, Animal first, Animal second) {
        System.out.println("Specify a fact that distinguishes " + first.getArticle() + " " + first + " from " + second.getArticle() + " " + second + ".");
        System.out.println("The sentence should be of the format: 'It can/has/is ...'.");
        System.out.println();
        Pattern pattern = Pattern.compile("It ((can)|(has)|(is)) .*", Pattern.CASE_INSENSITIVE);
        String input = scanner.nextLine();
        if (!pattern.matcher(input).matches()) {
            System.out.println("The examples of a statement: ");
            System.out.println("- It can fly.");
            System.out.println("- It has horn.");
            System.out.println("- It is a mammal.");
            parseResponse(scanner, first, second);
        } else {
            Pattern mPattern = Pattern.compile("(can)|(has)|(is)", Pattern.CASE_INSENSITIVE);
            Matcher mMatcher = mPattern.matcher(input);
            mMatcher.find();
            String modalVerb = input.substring(mMatcher.start(), mMatcher.end()).toLowerCase();
            String ability = input.substring(mMatcher.end()).trim().toLowerCase();
            System.out.println("Is it correct for " + second.getArticle() + " " + second + "?");
            String response = scanner.nextLine();
            while (!responseMap.containsKey(response.toLowerCase())) {
                System.out.println(clarificationList.get(new Random().nextInt(clarificationList.size())));
                response = scanner.nextLine();
            }
            if ("Yes".equalsIgnoreCase(responseMap.get(response))) {
                second.setAbility(modalVerb + " " + ability);
                first.setAbility(modals.get(modalVerb)[1] + " " + ability);
            } else {
                first.setAbility(modalVerb + " " + ability);
                second.setAbility(modals.get(modalVerb)[1] + " " + ability);
            }
            return modals.get(modalVerb)[2] + " " + ability + "?";
        }
        throw new IllegalArgumentException("This statement shouldn't be reached and is here to make the compiler be quiet");
    }
}
