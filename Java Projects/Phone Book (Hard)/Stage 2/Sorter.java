package phonebook;

import java.util.Comparator;
import java.util.List;

public class Sorter {
    public static <T> void bubbleSort(List<T> list, Comparator<T> comparator, Long timeout) {
        long startTime = System.currentTimeMillis();
        sortLoop:
        for (int i = 0; i < list.size() - 1; ++i)
            for (int j = 0; j < list.size() - 1 - i; ++j) {
                if (timeout != null && System.currentTimeMillis() - startTime > timeout) {
                    break sortLoop;
                }
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
    }
}
