package converter.ElementModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ElementModel {
    private final String element;
    private String value;
    private ArrayList<ElementModel> nested;
    private LinkedHashMap<String, String> attributes;

    public ElementModel(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setValue(String value) {
        if (nested != null)
            throw new IllegalArgumentException("Cannot have both nested elements and a value");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean containsElements() {
        return nested != null;
    }

    public void addNestedElement(ElementModel elem) {
        if (value != null)
            throw new IllegalArgumentException("Cannot have both nested elements and a value");
        if (nested == null)
            nested = new ArrayList<>();
        nested.add(elem);
    }

    public ArrayList<ElementModel> getNested() {
        return nested;
    }

    public boolean hasAttributes() {
        return attributes != null;
    }

    public void addAttribute(String attrName, String attrValue) {
        if (attributes == null)
            attributes = new LinkedHashMap<>();
        attributes.put(attrName, attrValue);
    }

    public LinkedHashMap<String, String> getAttributes() {
        return attributes;
    }
}
