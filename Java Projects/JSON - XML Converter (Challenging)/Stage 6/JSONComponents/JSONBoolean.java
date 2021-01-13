package converter.JSONComponents;

public class JSONBoolean implements JSONComponent {
    private final boolean bool;

    public JSONBoolean(boolean bool) {
        this.bool = bool;
    }

    @Override
    public String toString() {
        return JSONRepresentation();
    }

    @Override
    public String JSONRepresentation() {
        return Boolean.toString(bool);
    }
}
