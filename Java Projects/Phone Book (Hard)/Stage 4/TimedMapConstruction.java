package phonebook;

import java.util.function.*;
import java.util.Map;
import java.util.List;

public class TimedMapConstruction <T extends Comparable<T>, U> {
    private final Timer t;
    private final List<PhoneEntry<T, U>> list;
    private final Supplier<Map<T, U>> supplier;

    public TimedMapConstruction(List<PhoneEntry<T, U>> list,  Supplier<Map<T, U>> supplier) {
        this.list = list;
        this.supplier = supplier;
        t = new Timer();
    }

    public Map<T, U> create() {
        t.start();
        var map = supplier.get();
        for (PhoneEntry<T, U>  pe : list)
            map.put(pe.getFirst(), pe.getSecond());
        t.stop();
        return map;
    }

    public Timer getT() {
        return t;
    }

    public String elapsedTime() {
        return (t.getTimeElapsedMinutes() + " min. " + t.getTimeElapsedSeconds() + " sec. "
                + t.getTimeElapsedMS() + " ms.");
    }
}
