package converter.JSONComponents;

public class JSONString implements JSONComponent{
    private final String str;

    public JSONString(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public String JSONRepresentation() {
        return '"' + str + '"';
    }
}
