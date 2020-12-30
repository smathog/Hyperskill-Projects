package server;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleStorage {
    private final int size;
    private final ArrayList<String> storage;
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    public SimpleStorage(int size) {
        this.size = size;
        storage = new ArrayList<>(Collections.nCopies(size, ""));
    }

    public String set(int n, String s) {
        if (!checkRange(n))
            return ERROR;
        storage.set(n - 1, s);
        return OK;
    }

    public String get(int n) {
        if (!checkRange(n) || "".equals(storage.get(n - 1))) {
            return ERROR;
        } else {
            return storage.get(n - 1);
        }
    }

    public String delete(int n) {
        if (checkRange(n)) {
            storage.set(n - 1, "");
            return OK;
        }
        return ERROR;
    }

    private boolean checkRange(int n) {
        if (0 <= n - 1 && n - 1 <= size - 1)
            return true;
        else {
            System.out.println(ERROR);
            return false;
        }
    }
}
