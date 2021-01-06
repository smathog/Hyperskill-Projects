package converter.XMLParser;

import converter.Pair;
import converter.XMLComponents.XMLAttribute;
import converter.XMLComponents.XMLContent;
import converter.XMLComponents.XMLContentString;
import converter.XMLComponents.XMLSyntax;
import converter.XMLComponents.XMLTag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLParser {
    private static final HashSet<Character> whiteSpace = Stream.of(' ', '\t', '\b', '\n', '\r')
            .collect(Collectors.toCollection(HashSet::new));

    public static Pair<? extends XMLContent, Integer> parse(String XML, int currentPos) {
        //Handle possible invalid input
        if (XML.isEmpty() || XML.isBlank())
            throw new IllegalArgumentException("Invalid XML passed: " + XML);
        else if (currentPos >= XML.length())
            throw new IllegalArgumentException("Invalid currentPos: " + currentPos + " but XML length is: " + XML.length());

        var current = XMLSyntax.getByField(XML.charAt(currentPos));
        var next = XMLSyntax.getByField(XML.charAt(currentPos + 1));
        if (current.isPresent() && current.get() == XMLSyntax.XML_LEFT_ANGLE_BRACKET
                && !(next.isPresent() && next.get() == XMLSyntax.XML_FORWARD_SLASH))
            return parseTag(XML, currentPos);
        else {
            StringBuilder content = new StringBuilder();
            //append until encountering start closing tag
            while (!(current.isPresent() && current.get() == XMLSyntax.XML_LEFT_ANGLE_BRACKET
                && next.isPresent() && next.get() == XMLSyntax.XML_FORWARD_SLASH)) {
                content.append(XML.charAt(currentPos));
                ++currentPos;
                current = XMLSyntax.getByField(XML.charAt(currentPos));
                next = XMLSyntax.getByField(XML.charAt(currentPos + 1));
            }
            //get to end of closing tag
            while (!(current.isPresent() && current.get() == XMLSyntax.XML_RIGHT_ANGLE_BRACKET)) {
                ++currentPos;
                current = XMLSyntax.getByField(XML.charAt(currentPos));
            }
            return new Pair<>(new XMLContentString(content.toString()), ++currentPos);
        }
    }

    //Invoke upon encountering '<'
    public static Pair<XMLTag, Integer> parseTag(String XML, int currentPos) {
        //Handle possible invalid input
        if (XML.isEmpty() || XML.isBlank())
            throw new IllegalArgumentException("Invalid XML passed: " + XML);
        else if (currentPos >= XML.length())
            throw new IllegalArgumentException("Invalid currentPos: " + currentPos + " but XML length is: " + XML.length());
        else if (XMLSyntax.getByField(XML.charAt(currentPos)).get() != XMLSyntax.XML_LEFT_ANGLE_BRACKET)
            throw new IllegalArgumentException("charAt currentPos of XML is: " + XML.charAt(currentPos) + " but parsed tag must" +
                    " begin with '<'!");
        ++currentPos;

        XMLTag tag = new XMLTag();
        //Discard any whitespace up to first string, which should be element
        currentPos = discardWhitespace(XML, currentPos);
        StringBuilder element = new StringBuilder();
        while (!whiteSpace.contains(XML.charAt(currentPos)) && XMLSyntax.getByField(XML.charAt(currentPos)).isEmpty()) {
            element.append(XML.charAt(currentPos));
            ++currentPos;
        }
        tag.setElement(element.toString());
        currentPos = discardWhitespace(XML, currentPos);

        //need to parse attributes
        if (XMLSyntax.getByField(XML.charAt(currentPos)).isEmpty()) {
            var current = XMLSyntax.getByField(XML.charAt(currentPos));
            while (current.isEmpty()
                    || (current.get() != XMLSyntax.XML_RIGHT_ANGLE_BRACKET
                    && current.get() != XMLSyntax.XML_FORWARD_SLASH)) {
                XMLAttribute attr = new XMLAttribute();
                StringBuilder attribute = new StringBuilder();
                while (!(whiteSpace.contains(XML.charAt(currentPos)) || XML.charAt(currentPos) == '=')) {
                    attribute.append(XML.charAt(currentPos));
                    ++currentPos;
                }
                attr.setAttribute(attribute.toString());
                currentPos = discardWhitespace(XML, currentPos);
                if (XML.charAt(currentPos) != '=')
                    throw new IllegalArgumentException("Expected '=' between attribute and value, found " + XML.charAt(currentPos));
                ++currentPos;
                currentPos = discardWhitespace(XML, currentPos);
                if (XML.charAt(currentPos) != '"')
                    throw new IllegalArgumentException("Expected '\"' to surround attribute value, found " + XML.charAt(currentPos));
                StringBuilder value = new StringBuilder();
                ++currentPos;
                while (XML.charAt(currentPos) != '"') {
                    value.append(XML.charAt(currentPos));
                    ++currentPos;
                }
                ++currentPos;
                currentPos = discardWhitespace(XML, currentPos);
                attr.setValue(value.toString());
                if (tag.hasAttributes())
                    tag.getAttributes().add(attr);
                else
                    tag.setAttributes(Stream.of(attr).collect(Collectors.toCollection(ArrayList::new)));
                current = XMLSyntax.getByField(XML.charAt(currentPos));
            }
        }

        if (XMLSyntax.getByField(XML.charAt(currentPos)).get() == XMLSyntax.XML_FORWARD_SLASH
                && XMLSyntax.getByField(XML.charAt(currentPos + 1)).isPresent()
                && XMLSyntax.getByField(XML.charAt(currentPos + 1)).get() == XMLSyntax.XML_RIGHT_ANGLE_BRACKET) //no content
            return new Pair<>(tag, currentPos + 2);
        else if (XMLSyntax.getByField(XML.charAt(currentPos)).get() == XMLSyntax.XML_RIGHT_ANGLE_BRACKET) { //content to parse
            var content = parse(XML, ++currentPos);
            tag.setContent(content.getFirst());
            return new Pair<>(tag, content.getSecond());
        } else
            throw new IllegalArgumentException("Expected tag to close!");
    }

    private static int discardWhitespace(String XML, int currentPos) {
        while (whiteSpace.contains(XML.charAt(currentPos)))
            ++currentPos;
        return currentPos;
    }
}
