package converter.JSONComponents;

public class JSONFloat extends JSONNumber {
    private final double num;

    public JSONFloat(double num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return JSONRepresentation();
    }

    @Override
    public String JSONRepresentation() {
        return Double.toString(num);
    }
}
