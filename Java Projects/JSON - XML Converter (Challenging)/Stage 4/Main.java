package converter;

import converter.JSONComponents.JSONComponent;
import converter.JSONComponents.JSONNull;
import converter.JSONComponents.JSONObject;
import converter.JSONComponents.JSONString;
import converter.JSONParser.JSONLexer;
import converter.JSONParser.JSONParser;
import converter.XMLComponents.XMLAttribute;
import converter.XMLComponents.XMLContent;
import converter.XMLComponents.XMLContentString;
import converter.XMLComponents.XMLTag;
import converter.XMLParser.XMLParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            var filePath = Paths.get("test.txt");
            String fileContents = Files.readString(filePath);
            expandJSON(fileContents);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void expandXML(String fileContents) {
        XMLTag tag = XMLParser.parseTag(fileContents, 0).getFirst();
        ArrayDeque<XMLTag> tagStack = new ArrayDeque<>();
        ArrayDeque<String> pathStack = new ArrayDeque<>();
        tagStack.push(tag);
        pathStack.push(tag.getElement());
        while (!tagStack.isEmpty()) {
            //Print relevant information
            tag = tagStack.pop();
            String path = pathStack.pop();
            System.out.println("Element: ");
            System.out.println("path = " + path);
            if (tag.hasContent()) {
                if (tag.getContent().isEmpty())
                    System.out.println("value = \"\"");
                else if (tag.getContent().size() == 1 && tag.getContent().get(0).isContentString())
                    System.out.println("value = " + tag.getContent().get(0));
            } else
                System.out.println("value = null");
            if (tag.hasAttributes()) {
                System.out.println("attributes: ");
                for (XMLAttribute attr : tag.getAttributes())
                    System.out.println(attr);
            }
            System.out.println();

            //Add children and path information to stacks
            if (tag.hasContent())
                for (int i = tag.getContent().size() - 1; i >= 0; --i) {
                    XMLContent content = tag.getContent().get(i);
                    if (content.isTag()) {
                        XMLTag asTag = (XMLTag) content;
                        tagStack.push(asTag);
                        pathStack.push(path + ", " + asTag.getElement());
                    }
                }
        }
    }

    public static void expandJSON(String fileContents) {
        JSONComponent jc = JSONParser.parse(JSONLexer.lex(fileContents), 0).getFirst();
        if (!jc.isJSONObject())
            throw new IllegalArgumentException("Expected JSON Object");
        JSONObject json = (JSONObject) jc;
        ArrayDeque<Pair<String, JSONComponent>> jsonStack = new ArrayDeque<>();
        ArrayDeque<String> pathStack = new ArrayDeque<>();
        addObjectToStacks(json, null, pathStack, jsonStack);
        while (!jsonStack.isEmpty()) {
            var top = jsonStack.pop();
            JSONComponent component = top.getSecond();
            String key = top.getFirst();
            String path = pathStack.pop();
            System.out.println("Element: ");
            System.out.println("path = " + path);
            if (component.isJSONPrimitive()) {
                printValue(component);
            } else if (component.isJSONObject()) {
                JSONObject obj = (JSONObject) component;
                if (obj.getMappings().isEmpty())
                    printValue(obj);
                else if (isValidAttributeObject(obj, key)) {
                    var value = obj.getValue("#" + key);
                    if (value.isJSONPrimitive())
                        printValue(value);
                    else if (value.isJSONObject())
                        addObjectToStacks((JSONObject) value, path, pathStack, jsonStack);
                    else
                        throw new IllegalArgumentException("No implementation defined for JSONArrays");
                    if (obj.getMappings().size() > 1) {
                        System.out.println("attributes: ");
                        obj.getMappings().entrySet().forEach(e -> {
                            if (e.getKey().toString().startsWith("@"))
                                System.out.println(e.getKey().toString().substring(1) + " = \"" + (e.getValue().getClass() == JSONNull.class ? "" : e.getValue()) + "\"");
                        });
                    }
                } else { //strip all keys of attribute tags and iterate
                    obj.purgeAttributes();
                    if (obj.getMappings().isEmpty())
                        printValue(obj);
                    else
                        addObjectToStacks(obj, path, pathStack, jsonStack);
                }
            } else
                throw new IllegalArgumentException("No implementation defined for JSONArray");
            System.out.println();
        }
    }

    //only use on JSONPrimitive == true types
    private static void printValue(JSONComponent component) {
        if (component.getClass() == JSONNull.class)
            System.out.println("value = null");
        else if (component.getClass() == JSONObject.class)
            System.out.println("value = \"\"");
        else
            System.out.println("value = \"" + component + "\"");
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

    private static void addObjectToStacks(JSONObject json, String path, ArrayDeque<String> pathStack, ArrayDeque<Pair<String, JSONComponent>> jsonStack) {
        ArrayList<JSONString> keys = new ArrayList<>(json.getMappings().keySet());
        for (int i = keys.size() - 1; i >= 0; --i) {
            JSONString key = keys.get(i);
            JSONComponent value = json.getValue(key.toString());
            jsonStack.push(new Pair<>(key.toString(), value));
            if (path == null)
                pathStack.push(key.toString());
            else
                pathStack.push(path + ", " + key.toString());
        }
    }
}
