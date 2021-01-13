package converter.JSONParser;

import converter.JSONComponents.JSONArray;
import converter.JSONComponents.JSONCharacter;
import converter.JSONComponents.JSONComponent;
import converter.JSONComponents.JSONObject;
import converter.JSONComponents.JSONString;
import converter.Pair;

import java.util.List;

public class JSONParser {
    public static Pair<? extends JSONComponent, Integer> parse(List<JSONComponent> tokens, int currentIndex) {
        JSONComponent currentToken = tokens.get(currentIndex);
        if (currentToken.getClass() == JSONCharacter.class) {
            if (currentToken == JSONCharacter.JSON_BRACKET_LEFT)
                return parseArray(tokens, ++currentIndex);
            else if (currentToken == JSONCharacter.JSON_BRACE_LEFT)
                return parseObject(tokens, ++currentIndex);
        }
        return new Pair<>(currentToken, ++currentIndex);
    }

    public static Pair<JSONArray, Integer> parseArray(List<JSONComponent> tokens, int currentIndex) {
        JSONArray ja = new JSONArray();
        if (tokens == null || tokens.isEmpty())
            throw new IllegalArgumentException("List tokens is invalid!");
        if (currentIndex >= tokens.size())
            throw new IllegalArgumentException("Expected terminating ']'");
        if (tokens.get(currentIndex) == JSONCharacter.JSON_BRACKET_RIGHT)
            return new Pair<>(ja, ++currentIndex);
        while (true) {
            var pair = parse(tokens, currentIndex);
            ja.add(pair.getFirst());
            currentIndex = pair.getSecond();
            JSONComponent currentToken = tokens.get(currentIndex);
            if (currentToken.getClass() == JSONCharacter.class) {
                if (currentToken == JSONCharacter.JSON_BRACKET_RIGHT)
                    return new Pair<>(ja, ++currentIndex);
                else if (currentToken != JSONCharacter.JSON_COMMA)
                    throw new IllegalArgumentException("Expected comma character after object in JSONArray!");
                else
                    ++currentIndex;
            } else
                throw new IllegalArgumentException("Expected JSONCharacter ',' or ']' after array entry");
        }

    }

    public static Pair<JSONObject, Integer> parseObject(List<JSONComponent> tokens, int currentIndex) {
        JSONObject obj = new JSONObject();
        JSONComponent currentToken = tokens.get(currentIndex);
        if (currentToken.getClass() == JSONCharacter.class && currentToken == JSONCharacter.JSON_BRACE_RIGHT)
            return new Pair<>(obj, ++currentIndex);
        while (true) {
            var key = tokens.get(currentIndex);
            if (key.getClass() == JSONString.class)
                ++currentIndex;
            else
                throw new IllegalArgumentException("Expected String key in JSONObject, found " + key.getClass());
            if (tokens.get(currentIndex).getClass() != JSONCharacter.class || tokens.get(currentIndex) != JSONCharacter.JSON_COLON)
                throw new IllegalArgumentException("Expected colon after key in JSONObject, found " + tokens.get(currentIndex).getClass());
            var value = parse(tokens, ++currentIndex);
            obj.add((JSONString) key, value.getFirst());
            currentIndex = value.getSecond();
            currentToken = tokens.get(currentIndex);
            if (currentToken.getClass() == JSONCharacter.class && currentToken == JSONCharacter.JSON_BRACE_RIGHT)
                return new Pair<>(obj, ++currentIndex);
            else if (currentToken.getClass() != JSONCharacter.class || currentToken != JSONCharacter.JSON_COMMA)
                throw new IllegalArgumentException("Expected comma after pair in JSONObject, found " + currentToken.getClass() + ": " + currentToken.toString());
            else
                ++currentIndex;
        }
    }
}
