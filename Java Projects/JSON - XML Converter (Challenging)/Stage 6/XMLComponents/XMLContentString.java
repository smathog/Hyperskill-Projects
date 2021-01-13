package converter.XMLComponents;

public class XMLContentString extends XMLContent {
    private String contentString;

    public XMLContentString(String contentString) {
        this.contentString = contentString;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    @Override
    public boolean isContentString() {
        return true;
    }

    @Override
    public String toString() {
        return contentString;
    }
}
