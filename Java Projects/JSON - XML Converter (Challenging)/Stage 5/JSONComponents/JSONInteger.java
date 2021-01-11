package converter.JSONComponents;

public class JSONInteger extends JSONNumber {
    private final int num;

    public JSONInteger(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return JSONRepresentation();
    }

    @Override
    public String JSONRepresentation() {
        return Integer.toString(num);
    }
}
