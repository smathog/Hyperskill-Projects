package converter.XMLComponents;

public class XMLAttribute implements XMLComponent {
    private String attribute;
    private String value;

    public XMLAttribute() {}

    public XMLAttribute(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(attribute);
        sb.append(" = ");
        sb.append('"');
        sb.append(value);
        sb.append('"');
        return sb.toString();
    }
}
