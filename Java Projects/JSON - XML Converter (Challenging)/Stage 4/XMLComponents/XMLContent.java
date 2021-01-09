package converter.XMLComponents;

public abstract class XMLContent implements  XMLComponent{
    public boolean isTag() {
        return false;
    }

    public boolean isContentString() {
        return false;
    }
}
