package phonebook;

@FunctionalInterface
public interface TriConsumer <T, U, V> {
    public abstract void consume(T t, U u, V v);
}
