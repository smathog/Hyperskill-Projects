package converter.XMLComponents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum XMLSyntax implements XMLComponent {
    XML_EQUALS('='),
    XML_QUOTE('"'),
    XML_LEFT_ANGLE_BRACKET('<'),
    XML_RIGHT_ANGLE_BRACKET('>'),
    XML_FORWARD_SLASH('/');

    private static HashMap<Character, XMLSyntax> charMapping = Arrays.stream(XMLSyntax.values())
            .collect(Collectors.toMap(XMLSyntax::getField, Function.identity(), (e, r) -> e, HashMap::new));

    private char field;

    XMLSyntax(char field) {
        this.field = field;
    }

    public char getField() {
        return field;
    }

    public static Optional<XMLSyntax> getByField(char field) {
        return Optional.ofNullable(charMapping.get(field));
    }

}
