package animals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
        System.out.println("Enter an animal: ");
        String input = scanner.nextLine();
        String article, animal;
        if (input.matches("[Aa]n?\\s[\\-a-zA-Z]+")) {
            String[] split = input.split("\\s");
            article = split[0].toLowerCase();
            animal = split[1].toLowerCase();
        } else {
            if (anSet.contains(input.charAt(0)))
                article = "an";
            else
                article = "a";
            animal = input.toLowerCase();
        }
        System.out.println("Is it " + article + " " + animal + "?");
        parseResponse(scanner);
        System.out.println();
        System.out.println(goodbyeList.get(new Random().nextInt(goodbyeList.size())));
    }

    private static void greet() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (5 <= currentTime.getHour() && currentTime.getHour() <= 12)
            System.out.println("Good morning");
        else if (currentTime.getHour() <= 18)
            System.out.println("Good afternoon");
        else
            System.out.println("Good evening");
    }

    private static void parseResponse(Scanner scanner) {
        String response = scanner.nextLine().strip();
        if (response.endsWith("!") || response.endsWith("."))
            response = response.substring(0, response.length() - 1);
        response = response.toLowerCase();
        if (responseMap.containsKey(response))
            System.out.println("You answered: " + responseMap.get(response));
        else {
            System.out.println(clarificationList.get(new Random().nextInt(clarificationList.size())));
            parseResponse(scanner);
        }
    }
}
