package phonebook;

import java.util.Comparator;
import java.util.function.*;
import java.util.List;

public class TimedSort <T> {
    private final Timer t;
    private final List<T> l1;
    private final TriConsumer<List<T>, Comparator<T>, Long>  sort;
    private final Comparator<T> comp;
    private boolean state;
    private Long timeOut;

    public TimedSort (List<T> l1, TriConsumer<List<T>, Comparator<T>, Long> sort, Comparator<T> comp, Long timeOut) {
        this.timeOut = timeOut;
        this.comp = comp;
        state = false;
        t = new Timer();
        this.l1 = l1;
        this.sort = sort;
    }

    public void invoke() {
        t.start();
        sort.consume(l1, comp, timeOut);
        t.stop();
        if (timeOut != null && t.getTimeElapsedTotalMS() > timeOut)
            state = false;
        else
            state = true;
    }

    public boolean getState() {
        return state;
    }

    public Timer getT() {
        return t;
    }

    public String elapsedTime() {
        return (t.getTimeElapsedMinutes() + " min. " + t.getTimeElapsedSeconds() + " sec. "
                + t.getTimeElapsedMS() + " ms.");
    }
}
