package animals;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageRules {
    private static ResourceBundle appResource = ResourceBundle.getBundle("App");

    public static Optional<Boolean> validateAnswer(String answer) {
        Pattern positive = Pattern.compile(appResource.getString("positiveAnswer.isCorrect"), Pattern.CASE_INSENSITIVE);
        Pattern negative = Pattern.compile(appResource.getString("negativeAnswer.isCorrect"), Pattern.CASE_INSENSITIVE);
        Matcher mPositive = positive.matcher(answer);
        Matcher mNegative = negative.matcher(answer);
        boolean p = mPositive.matches();
        boolean n = mNegative.matches();
        if (!(p || n))
            return Optional.empty();
        else if (p)
            return Optional.of(true);
        else
            return Optional.of(false);
    }

    public static Animal getAnimal(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher isCorrect = Pattern.compile(appResource.getString("animal.isCorrect"), Pattern.CASE_INSENSITIVE).matcher(input);
        if (isCorrect.matches()) {
            String p1 = appResource.getString("animal.1.pattern");
            String p2 = appResource.getString("animal.2.pattern");
            String p3 = appResource.getString("animal.3.pattern");
            Matcher m1 = Pattern.compile(p1, Pattern.CASE_INSENSITIVE).matcher(input);
            Matcher m2 = Pattern.compile(p2, Pattern.CASE_INSENSITIVE).matcher(input);
            Matcher m3 = Pattern.compile(p3, Pattern.CASE_INSENSITIVE).matcher(input);
            if (m1.matches())
                return new Animal(input.replaceAll("(?i)" + p1, appResource.getString("animal.1.replace")));
            else if (m2.matches())
                return new Animal(input.replaceAll("(?i)" + p2, appResource.getString("animal.2.replace")));
            else if (m3.matches())
                return new Animal(input.replaceAll("(?i)" + p3, appResource.getString("animal.3.replace")));
            else
                throw new IllegalArgumentException("Debug statement - shouldn't happen");
        } else {
            System.out.println(appResource.getString("animal.error"));
            return getAnimal(scanner);
        }
    }
}
