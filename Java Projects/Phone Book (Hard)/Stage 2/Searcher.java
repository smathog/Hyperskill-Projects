package phonebook;

import java.util.Collection;
import java.util.List;

public class Searcher {
    public static <T, U extends Comparable<T>> boolean linearSearch(T target, Collection<U> collection) {
        for (U u : collection)
            if (u.compareTo(target) == 0)
                return true;
        return false;
    }

    public static <T> boolean jumpSearch(T target, List<Comparable<T>> list) {
        //if collection is empty, obviously not going to find result
        if (list == null || list.isEmpty())
            return false;

        int currentRight = 0;
        int prevRight = 0;
        int jump = (int) Math.sqrt(list.size());

        //Handle case where right at start
        if (list.get(currentRight).compareTo(target) == 0)
            return true;

        while (currentRight < list.size() - 1) {
            currentRight = Math.min(list.size() - 1, currentRight + jump);
            if (list.get(currentRight).compareTo(target) >= 0)
                break;
            prevRight = currentRight;
        }

        //Handle last position case
        if (currentRight == list.size() - 1 && list.get(currentRight).compareTo(target) < 0)
            return false;

        return linearSearch(target, list.subList(++prevRight, ++currentRight));
    }
}
