package phonebook;

import java.util.function.*;
import java.util.List;

public class TimedAction {
    private final Timer t;
    private int result;
    private final int size;

    public <T> TimedAction(List<T> l1, List<? extends Comparable<T>> l2, BiPredicate<T, List<? extends Comparable<T>>> matcher) {
        size = l1.size();
        t = new Timer();
        t.start();
        for (T t : l1)
            result += matcher.test(t, l2) ? 1 : 0;
        t.stop();
    }

    public Timer getT() {
        return t;
    }

    public String getResult() {
        return result + " / " + size;
    }

    public String elapsedTime() {
        return (t.getTimeElapsedMinutes() + " min. " + t.getTimeElapsedSeconds() + " sec. "
        + t.getTimeElapsedMS() + " ms.");
    }
}
