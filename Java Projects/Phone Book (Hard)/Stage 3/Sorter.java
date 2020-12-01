package phonebook;

import java.util.Comparator;
import java.util.List;

public class Sorter {
    public static <T> void bubbleSort(List<T> list, Comparator<T> comparator, Long timeout) {
        long startTime = System.currentTimeMillis();
        sortLoop:
        for (int i = 0; i < list.size() - 1; ++i) {
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

    public static <T> void quickSort(List<T> list, Comparator<T> comparator, Long timeOut) {
        quickSort(list, comparator, System.currentTimeMillis(), timeOut, 0, list.size());
    }

    private static<T> void quickSort(List<T> list, Comparator<T> comparator, Long startTime, Long timeOut, int left, int right) {
        if (timeOut != null && System.currentTimeMillis() - startTime > timeOut)
            return;
        if (left + 1 == right || left == right)
            return;
        int p = partition(list, left, right, comparator);
        quickSort(list, comparator, startTime, timeOut, left, p);
        quickSort(list, comparator, startTime, timeOut, p, right);
    }

    private static <T> int partition(List<T> list, int leftInclusive, int rightExclusive, Comparator<T> comparator) {
        int pivotIndex = leftInclusive;
        for (int i = leftInclusive; i < rightExclusive - 1; ++i) {
            if (comparator.compare(list.get(i), list.get(rightExclusive - 1)) < 0) {
                T temp = list.get(i);
                list.set(i, list.get(pivotIndex));
                list.set(pivotIndex, temp);
                ++pivotIndex;
            }
        }
        T temp = list.get(rightExclusive - 1);
        list.set(rightExclusive - 1, list.get(pivotIndex));
        list.set(pivotIndex, temp);
        return pivotIndex;
    }
}
