package converter.ElementModel;

import com.google.gson.JsonElement;
import converter.JSONComponents.JSONComponent;
import converter.JSONComponents.JSONNull;
import converter.JSONComponents.JSONObject;
import converter.JSONComponents.JSONString;
import converter.JSONParser.JSONLexer;
import converter.JSONParser.JSONParser;
import converter.Pair;
import converter.XMLComponents.XMLAttribute;
import converter.XMLComponents.XMLComponent;
import converter.XMLComponents.XMLContent;
import converter.XMLComponents.XMLContentString;
import converter.XMLComponents.XMLTag;
import converter.XMLParser.XMLParser;

import java.util.function.Consumer;

public class ConverterUtilities {
    public static void convert(String fileInput) {
        if (fileInput.startsWith("{")) {
            JSONComponent comp = JSONParser.parse(JSONLexer.lex(fileInput), 0).getFirst();
            if (!comp.isJSONObject())
                throw new IllegalArgumentException("Expected JSONObject from file!");
            JSONObject obj = (JSONObject) comp;
            //System.out.println("PARSED OBJECT: ");
            //System.out.println(comp.JSONRepresentation());
            ElementModel model = convertJSONToElementModel(obj);
            //System.out.println("ELEMENT MODEL: ");
            //System.out.println(model);
            System.out.println(convertElementModelToXML(model).toString());
        } else if (fileInput.startsWith("<")) {
            XMLTag tag = (XMLTag) XMLParser.parseTag(fileInput, 0).getFirst();
            //System.out.println("PARSED OBJECT: ");
            //System.out.println(tag);
            ElementModel model = convertXMLToElementModel(tag);
            //System.out.println("ELEMENT MODEL :");
            //System.out.println(model);
            System.out.println(convertElementModelToJSON(model, true).JSONRepresentation());
        } else
            throw new IllegalArgumentException("Input is neither valid JSON nor XML");
    }

    private static XMLTag convertElementModelToXML(ElementModel top) {
        XMLTag tag = new XMLTag();
        tag.setElement(top.getElement());
        if (top.hasAttributes())
            for (var entry : top.getAttributes().entrySet())
                tag.addAttribute(new XMLAttribute(entry.getKey(), (entry.getValue() == null ? "" : entry.getValue())));
        if (top.containsElements()) {
            for (var elem : top.getNested())
                tag.addContent(convertElementModelToXML(elem));
        } else
            if (top.getValue() != null)
                tag.addContent(new XMLContentString(top.getValue()));
        return tag;
    }

    private static ElementModel convertXMLToElementModel(XMLTag tag) {
        ElementModel top = new ElementModel(tag.getElement());
        if (tag.hasAttributes())
            for (XMLAttribute attr : tag.getAttributes())
                top.addAttribute(attr.getAttribute(), attr.getValue());
        if (tag.hasContent()) {
            if (tag.getContent().isEmpty())
                top.setValue("");
            else if (tag.getContent().size() == 1 && tag.getContent().get(0).isContentString())
                top.setValue(((XMLContentString) tag.getContent().get(0)).getContentString());
            else {
                for (XMLContent content : tag.getContent()) {
                    if (!content.isTag())
                        throw new IllegalArgumentException("Should've been a tag!");
                    top.addNestedElement(convertXMLToElementModel((XMLTag) content));
                }
            }
        }
        return top;
    }

    private static JSONObject convertElementModelToJSON(ElementModel top, boolean first) {
        JSONObject json = new JSONObject();
        if (top.hasAttributes()) {
            Consumer<JSONObject> attrBuilder = attr -> {
                for (var entry : top.getAttributes().entrySet())
                    attr.add(new JSONString("@" + entry.getKey()), new JSONString(entry.getValue()));
                if (top.containsElements()) {
                    JSONObject attrValue = new JSONObject();
                    for (var element : top.getNested()) {
                        if (element.hasAttributes() || element.containsElements())
                            attrValue.add(new JSONString(element.getElement()), convertElementModelToJSON(element, false));
                        else
                            attrValue.add(new JSONString(element.getElement()), new JSONString(element.getValue()));
                    }
                    attr.add(new JSONString("#" + top.getElement()), attrValue);
                } else
                    attr.add(new JSONString("#" + top.getElement()), new JSONString(top.getValue()));
            };
            if (first) {
                JSONObject attr = new JSONObject();
                attrBuilder.accept(attr);
                json.add(new JSONString(top.getElement()), attr);
            } else
                attrBuilder.accept(json);
        } else if (top.containsElements()) {
            Consumer<JSONObject> nestedBuilder = nested -> {
                for (var element : top.getNested()) {
                    if (element.hasAttributes() || element.containsElements())
                        nested.add(new JSONString(element.getElement()), convertElementModelToJSON(element, false));
                    else
                        nested.add(new JSONString(element.getElement()), new JSONString(element.getValue()));
                }
            };
            if (first) {
                JSONObject nested = new JSONObject();
                nestedBuilder.accept(nested);
                json.add(new JSONString(top.getElement()), nested);
            } else
                nestedBuilder.accept(json);
        } else
            json.add(new JSONString(top.getElement()), new JSONString(top.getValue()));
        return json;
    }

    private static ElementModel convertJSONToElementModel(JSONObject json) {
        if (json.getMappings().size() == 0)
            throw new IllegalArgumentException("Malformed JSONObject passed for conversion; contains no mapping!");
        else if (json.getMappings().size() == 1) {
            var entry = json.getMappings().entrySet().stream().findFirst().get();
            if (entry.getValue().isJSONObject())
                return convertJSONToElementModelImpl(entry.getKey().toString(), (JSONObject) entry.getValue());
            else if (entry.getValue().isJSONPrimitive()) {
                ElementModel temp = new ElementModel(entry.getKey().toString());
                temp.setValue(entry.getValue().toString());
                return temp;
            } else
                throw new IllegalArgumentException("No implementation defined for JSONArrays");
        } else
            return convertJSONToElementModelImpl("root", json);
    }

    private static ElementModel convertJSONToElementModelImpl(String key, JSONObject json) {
        ElementModel top = new ElementModel(key);

        if (json.getMappings().isEmpty()) {
            top.setValue("");
            return top;
        }

        if (isValidAttributeObject(json, key)) {
            for (var entry : json.getMappings().entrySet()) {
                if (entry.getKey().toString().startsWith("@"))
                    top.addAttribute(entry.getKey().toString().substring(1), entry.getValue().toString());
                else if (entry.getKey().toString().startsWith("#")) {
                    if (entry.getValue().isJSONPrimitive())
                        top.setValue(entry.getValue().toString());
                    else if (entry.getValue().isJSONObject()) {
                        JSONObject valueObj = (JSONObject) entry.getValue();
                        for (var nestedEntry : valueObj.getMappings().entrySet()) {
                            if (nestedEntry.getValue().isJSONPrimitive()) {
                                ElementModel elem = new ElementModel(nestedEntry.getKey().toString());
                                elem.setValue(nestedEntry.getValue().toString());
                                top.addNestedElement(elem);
                            } else if (nestedEntry.getValue().isJSONObject())
                                top.addNestedElement(convertJSONToElementModelImpl(nestedEntry.getKey().toString(), (JSONObject) nestedEntry.getValue()));
                            else
                                throw new IllegalArgumentException("No specification provided for JSONArrays");
                        }
                    } else
                        throw new IllegalArgumentException("No specification provided for JSONArrays");
                } else
                    throw new IllegalArgumentException("Malformed JSON with attribute object passed test function!");
            }
        } else {
            //cleanup any illegal/malformed symbols
            json.purgeAttributes();

            for (var entry : json.getMappings().entrySet()) {
                if (entry.getValue().isJSONPrimitive()) {
                    ElementModel nested = new ElementModel(entry.getKey().toString());
                    nested.setValue(entry.getValue().toString());
                    top.addNestedElement(nested);
                } else if (entry.getValue().isJSONObject())
                    top.addNestedElement(convertJSONToElementModelImpl(entry.getKey().toString(), (JSONObject) entry.getValue()));
                else
                    throw new IllegalArgumentException("No specification provided for JSONArray");
            }
        }
        return top;
    }

    private static boolean isValidAttributeObject(JSONObject object, String key) {
        if (!object.hasField("#" + key))
            return false;
        for (var entry : object.getMappings().entrySet()) {
            String entryKey = entry.getKey().toString();
            if (entryKey.startsWith("#") && !entryKey.equals("#" + key))
                return false;
            if (!entryKey.equals("#" + key)) {
                if (!(entryKey.startsWith("@") && entryKey.length() >= 2))
                    return false;
                if (!entry.getValue().isJSONPrimitive())
                    return false;
            }
        }
        return true;
    }
}
