package phonebook;

public class PhoneEntry <T extends Comparable<T>, U> implements Comparable<T> {
    private T t;
    private U u;

    public PhoneEntry(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getFirst() {
        return t;
    }

    public U getSecond() {
        return u;
    }

    @Override
    public int compareTo(T t) {
        return this.t.compareTo(t);
    }

    @Override
    public String toString() {
        return t.toString() + " " + u.toString();
    }
}
