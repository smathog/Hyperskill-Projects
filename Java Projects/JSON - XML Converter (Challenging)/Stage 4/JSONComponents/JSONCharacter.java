package converter.JSONComponents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum JSONCharacter implements JSONComponent{
    JSON_COMMA(','),
    JSON_COLON(':'),
    JSON_BRACKET_LEFT('['),
    JSON_BRACKET_RIGHT(']'),
    JSON_BRACE_LEFT('{'),
    JSON_BRACE_RIGHT('}'),
    JSON_QUOTE('"');

    private static final Map<Character, JSONCharacter> charToJSONMap;

    static {
        charToJSONMap = Arrays.stream(JSONCharacter.values())
                .collect(Collectors.toMap(JSONCharacter::getField, Function.identity(), (e, r) -> e, HashMap::new));
    }

    private final char field;

    JSONCharacter(char field) {
        this.field = field;
    }

    public char getField() {
        return this.field;
    }

    public static Optional<JSONCharacter> charLookup(char c) {
        return charToJSONMap.containsKey(c) ? Optional.of(charToJSONMap.get(c)) : Optional.empty();
    }

    @Override
    public String toString() {
        return JSONRepresentation();
    }

    @Override
    public String JSONRepresentation() {
        return Character.toString(this.field);
    }
}
