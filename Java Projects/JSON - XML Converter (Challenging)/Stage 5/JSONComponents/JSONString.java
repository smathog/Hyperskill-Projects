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
        if (str == null)
            return null;
        else
            return '"' + str + '"';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other.getClass() != JSONString.class)
            return false;
        JSONString js = (JSONString) other;
        return this.str.equals(js.str);
    }
}
