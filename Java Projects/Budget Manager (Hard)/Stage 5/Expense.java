package budget;

public class Expense implements Comparable<Expense> {
    private final String name;
    private final double price;
    private final Type type;

    public Expense(String name, double price, Type t) {
        this.name = name;
        this.price = price;
        this.type =t;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + " $" + String.format("%,.02f", price);
    }

    @Override
    public int compareTo(Expense e) {
        return Double.compare(e.price, this.price);
    }

    public enum Type {
        FOOD("Food"),
        CLOTHES("Clothes"),
        ENTERTAINMENT("Entertainment"),
        OTHER("Other");

        public final String name;

        Type(String name) {
            this.name = name;
        }
    };
}
