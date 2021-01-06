package converter.XMLComponents;

import java.util.ArrayList;

public class XMLTag extends XMLContent {
    private String element;
    private ArrayList<XMLAttribute> attributes;
    private XMLContent content;

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public boolean hasAttributes() {
        return attributes != null;
    }

    public void addAttribute(XMLAttribute attribute) {
        if (attributes == null)
            attributes = new ArrayList<>();
        attributes.add(attribute);
    }

    public ArrayList<XMLAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<XMLAttribute> attributes) {
        this.attributes = attributes;
    }

    public boolean hasContent() {
        return content != null;
    }

    public XMLContent getContent() {
        return content;
    }

    public void setContent(XMLContent content) {
        this.content = content;
    }

    @Override
    public boolean isTag() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(element);
        if (hasAttributes()) {
            sb.append(' ');
            for (int i = 0; i < attributes.size(); ++i) {
                sb.append(attributes.get(i));
                if (i != attributes.size() - 1)
                    sb.append(' ');
            }
        }
        if (hasContent()) {
            sb.append('>');
            sb.append(content);
            sb.append("</");
            sb.append(element);
            sb.append(">");
        } else
            sb.append("/>");
        return sb.toString();
    }
}
