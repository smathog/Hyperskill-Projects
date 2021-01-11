package converter.JSONParser;

import converter.JSONComponents.JSONBoolean;
import converter.JSONComponents.JSONCharacter;
import converter.JSONComponents.JSONComponent;
import converter.JSONComponents.JSONFloat;
import converter.JSONComponents.JSONInteger;
import converter.JSONComponents.JSONNull;
import converter.JSONComponents.JSONNumber;
import converter.JSONComponents.JSONString;
import converter.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JSONLexer {
    private static HashSet<Character> JSONWhitespace;
    private static HashSet<Character> JSONSyntax;
    private static HashSet<Character> JSONNumberCharacters;

    static {
        JSONWhitespace = Stream.of(' ', '\t', '\b', '\n', '\r')
                .collect(Collectors.toCollection(HashSet::new));
        JSONSyntax = Arrays.stream(JSONCharacter.values())
                .map(JSONCharacter::getField)
                .collect(Collectors.toCollection(HashSet::new));
        JSONNumberCharacters = Stream.concat(
                Stream.of('-', 'e', '.'),
                IntStream.range(0, 10)
                .mapToObj(i -> Character.valueOf((char) ('0' + i))))
                .collect(Collectors.toCollection(HashSet::new));

    }

    public static Optional<Pair<JSONString, String>> lexString(String input) {
        StringBuilder sb = new StringBuilder();
        if (JSONCharacter.JSON_QUOTE.getField() != input.charAt(0))
            return Optional.empty();
        input = input.substring(1);
        input.chars()
                .takeWhile(c -> JSONCharacter.JSON_QUOTE.getField() != (char) c)
                .forEachOrdered(c -> sb.append((char) c));
        if (sb.length() == input.length())
            throw new IllegalArgumentException("Failed to reach terminal \" at end of input String");
        else
            return Optional.of(new Pair<>(new JSONString(sb.toString()), input.substring(sb.length() + 1)));
    }

    public static Optional<Pair<JSONNumber, String>> lexNumber(String input) {
        StringBuilder sb = new StringBuilder();
        input.chars().takeWhile(c -> JSONNumberCharacters.contains((char) c))
                .forEachOrdered(c -> sb.append((char) c));
        input = input.substring(sb.length());
        if (sb.length() == 0)
            return Optional.empty();
        else {
            JSONNumber jn;
            if (sb.toString().contains("."))
                jn = new JSONFloat(Double.parseDouble(sb.toString()));
            else
                jn = new JSONInteger(Integer.parseInt(sb.toString()));
            return Optional.of(new Pair<>(jn, input));
        }
    }

    public static Optional<Pair<JSONBoolean, String>> lexBoolean(String input) {
        if (input.startsWith("true"))
            return Optional.of(new Pair<>(new JSONBoolean(true), input.substring(4)));
        else if (input.startsWith("false"))
            return Optional.of(new Pair<>(new JSONBoolean(false), input.substring(5)));
        else
            return Optional.empty();
    }

    public static Optional<String> lexNull(String input) {
        if (input.startsWith("null"))
            return Optional.of(input.substring(4));
        else
            return Optional.empty();
    }

    public static List<JSONComponent> lex(String input) {
        List<JSONComponent> tokens = new ArrayList<>();
        while (!input.isEmpty()) {
            var jsonString = lexString(input);
            if (jsonString.isPresent()) {
                tokens.add(jsonString.get().getFirst());
                input = jsonString.get().getSecond();
                continue;
            }
            var jsonNumber = lexNumber(input);
            if (jsonNumber.isPresent()) {
                tokens.add(jsonNumber.get().getFirst());
                input = jsonNumber.get().getSecond();
                continue;
            }
            var jsonBoolean = lexBoolean(input);
            if (jsonBoolean.isPresent()) {
                tokens.add(jsonBoolean.get().getFirst());
                input = jsonBoolean.get().getSecond();
                continue;
            }
            var jsonNull = lexNull(input);
            if (jsonNull.isPresent()) {
                tokens.add(new JSONNull());
                input = jsonNull.get();
                continue;
            }
            char leadChar = input.charAt(0);
            input = input.substring(1);
            if (JSONWhitespace.contains(leadChar))
                continue;
            else if (JSONSyntax.contains(leadChar)) {
                tokens.add(JSONCharacter.charLookup(leadChar).get());
            } else {
                throw new IllegalArgumentException("Unexpected character: " + leadChar);
            }
        }
        return tokens;
    }
}
