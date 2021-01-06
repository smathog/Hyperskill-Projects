package converter;

import converter.JSONComponents.*;
import converter.JSONParser.*;
import converter.XMLComponents.*;
import converter.XMLParser.*;

public class Converter {
    public static String convert(String input) {
        if (input.startsWith("<")) {
            return convertXMLToJSON(XMLParser.parse(input, 0).getFirst()).JSONRepresentation();
        } else if (input.startsWith("{")) {
            return convertJSONToXML(JSONParser.parse(JSONLexer.lex(input), 0).getFirst()).toString();
        } else
            throw new IllegalArgumentException("Input is neither XML nor JSON");
    }

    private static JSONComponent convertXMLToJSON(XMLContent content) {
        if (content.isTag()) {
            XMLTag xml = (XMLTag) content;
            JSONObject json = new JSONObject();
            if (xml.hasAttributes()) {
                JSONObject nested = new JSONObject();
                for (XMLAttribute attr : xml.getAttributes()) {
                    String attribute = "@" + attr.getAttribute();
                    nested.add(new JSONString(attribute), new JSONString(attr.getValue()));
                }
                if (xml.hasContent())
                    nested.add(new JSONString("#" + xml.getElement()), convertXMLToJSON(xml.getContent()));
                else
                    nested.add(new JSONString("#" + xml.getElement()), new JSONNull());
                json.add(new JSONString(xml.getElement()), nested);
            } else {
                if (xml.hasContent())
                    json.add(new JSONString(xml.getElement()), convertXMLToJSON(xml.getContent()));
                else
                    json.add(new JSONString(xml.getElement()), new JSONNull());
            }
            return json;
        } else { //must be content string
            return new JSONString(((XMLContentString) content).getContentString());
        }
    }

    private static XMLContent convertJSONToXML(JSONComponent content) {
        if (content.isJSONPrimitive())
            return new XMLContentString(content.JSONRepresentation());
        else if (content.isJSONArray())
            throw new IllegalArgumentException("Not implemented yet!");
        else if (content.isJSONObject()) {
            XMLTag tag = new XMLTag();
            JSONObject json = (JSONObject) content;
            //at this stage, can only have a single field
            var field = json.getMappings().entrySet().stream().findFirst().get();
            tag.setElement(field.getKey().toString());
            if (field.getValue().isJSONPrimitive() && field.getValue().getClass() != JSONNull.class) {
                tag.setContent(new XMLContentString(field.getValue().toString()));
            } else if (field.getValue().isJSONArray()) {
                tag.setContent(convertJSONToXML(field.getValue()));
            } else if (field.getValue().isJSONObject()) {
                JSONObject nested = (JSONObject) field.getValue();
                for (var entry : nested.getMappings().entrySet()) {
                    if (entry.getKey().toString().startsWith("@"))
                        tag.addAttribute(new XMLAttribute(entry.getKey().toString().substring(1),
                                entry.getValue().toString()));
                    else if (entry.getKey().toString().equals("#" + tag.getElement())
                        && entry.getValue().getClass() != JSONNull.class)
                        tag.setContent(convertJSONToXML(entry.getValue()));
                }
            }
            return tag;
        } else
            throw new IllegalArgumentException("Shouldn't reach this point, but implementation not yet defined");
    }
}
