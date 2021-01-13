package converter.ElementModel;

import converter.JSONComponents.*;
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
import java.util.function.Function;
import java.util.function.Predicate;

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
            XMLTag tag = (XMLTag) XMLParser.parse(fileInput, 0).getFirst();
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

    private static boolean isArray(ElementModel elementModel) {
        //if (elementModel.hasAttributes())
        //    return false;
        if (!elementModel.containsElements())
            return false;
        else if (elementModel.getNested().size() == 1)
            return false;
        else {
            String allName = elementModel.getNested().get(0).getElement();
            return elementModel.getNested().stream().allMatch(e -> allName.equals(e.getElement()));
        }
    }

    private static JSONArray convertElementModelToJSONArray(ElementModel top) {
        JSONArray array = new JSONArray();
        for (ElementModel element : top.getNested()) {
            if (element.hasAttributes() || (element.containsElements()) && !isArray(element))
                array.add(convertElementModelToJSON(element, false));
            else if (isArray(element))
                array.add(convertElementModelToJSONArray(element));
            else
                array.add(new JSONString(element.getValue()));
        }
        return array;
    }

    private static JSONObject convertElementModelToJSON(ElementModel top, boolean first) {
        JSONObject json = new JSONObject();
        Consumer<JSONObject> nestedBuilder = nested -> {
            for (var element : top.getNested()) {
                if (element.hasAttributes() || (element.containsElements() && !isArray(element)))
                    nested.add(new JSONString(element.getElement()), convertElementModelToJSON(element, false));
                else if (isArray(element))
                    nested.add(new JSONString(element.getElement()), convertElementModelToJSONArray(element));
                else
                    nested.add(new JSONString(element.getElement()), new JSONString(element.getValue()));
            }
        };
        if (top.hasAttributes()) {
            Consumer<JSONObject> attrBuilder = attr -> {
                for (var entry : top.getAttributes().entrySet())
                    attr.add(new JSONString("@" + entry.getKey()), new JSONString(entry.getValue()));
                if (isArray(top)) {
                    attr.add(new JSONString("#" + top.getElement()), convertElementModelToJSONArray(top));
                } else if (top.containsElements()) {
                    JSONObject attrValue = new JSONObject();
                    nestedBuilder.accept(attrValue);
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
        } else if (top.containsElements() && !isArray(top)) {
            if (first) {
                JSONObject nested = new JSONObject();
                nestedBuilder.accept(nested);
                json.add(new JSONString(top.getElement()), nested);
            } else
                nestedBuilder.accept(json);
        } else if (isArray(top)) {
            json.add(new JSONString(top.getElement()), convertElementModelToJSONArray(top));
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
            } else //JSONArray
                return convertJSONArraytoElementModel(entry.getKey().toString(), (JSONArray) entry.getValue());
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
                if (entry.getKey().toString().startsWith("@")) {
                    if (entry.getValue().isJSONPrimitive())
                        top.addAttribute(entry.getKey().toString().substring(1), entry.getValue().toString());
                    else //must be empty JSONContainer
                        top.addAttribute(entry.getKey().toString().substring(1), "");
                } else if (entry.getKey().toString().startsWith("#")) {
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
                                top.addNestedElement(convertJSONArraytoElementModel(nestedEntry.getKey().toString(), (JSONArray) nestedEntry.getValue()));
                        }
                    } else {
                        JSONArray valueArr = (JSONArray) entry.getValue();
                        for (JSONComponent c : valueArr.getElementsAsList()) {
                            if (c.isJSONPrimitive()) {
                                ElementModel element = new ElementModel("element");
                                element.setValue(c.toString());
                                top.addNestedElement(element);
                            } else if (c.isJSONObject())
                                top.addNestedElement(convertJSONToElementModelImpl("element", (JSONObject) c));
                            else
                                top.addNestedElement(convertJSONArraytoElementModel("element", (JSONArray) c));
                        }
                    }
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
                    top.addNestedElement(convertJSONArraytoElementModel(entry.getKey().toString(), (JSONArray) entry.getValue()));
            }
        }
        return top;
    }

    private static ElementModel convertJSONArraytoElementModel(String key, JSONArray array) {
        ElementModel top = new ElementModel(key);

        if (array.getElementsAsList().isEmpty()) {
            top.setValue("");
            return top;
        }

        for (JSONComponent entry : array.getElementsAsList()) {
            if (entry.isJSONPrimitive()) {
                ElementModel element = new ElementModel("element");
                element.setValue(entry.toString());
                top.addNestedElement(element);
            } else if (entry.isJSONObject())
                top.addNestedElement(convertJSONToElementModelImpl("element", (JSONObject) entry));
            else //JSONArray
                top.addNestedElement(convertJSONArraytoElementModel("element", (JSONArray) entry));
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
                if (!(entry.getValue().isJSONPrimitive() ||
                        (!entry.getValue().isJSONPrimitive() && ((JSONContainer) entry.getValue()).isEmpty())))
                    return false;
            }
        }
        return true;
    }
}
