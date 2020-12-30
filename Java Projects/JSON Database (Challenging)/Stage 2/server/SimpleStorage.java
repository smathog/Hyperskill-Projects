package server;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleStorage {
    private final ArrayList<String> storage;
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    public SimpleStorage() {
        storage = new ArrayList<>(Collections.nCopies(100, ""));
    }

    public void set(int n, String s) {
        if (!checkRange(n))
            return;
        storage.set(n - 1, s);
        System.out.println(OK);
    }

    public void get(int n) {
        if (!checkRange(n)) {
        } else if ("".equals(storage.get(n - 1))) {
            System.out.println(ERROR);
        } else {
            System.out.println(storage.get(n - 1));
        }
    }

    public void delete(int n) {
        if (checkRange(n)) {
            System.out.println(OK);
            storage.set(n - 1, "");
        }
    }

    private boolean checkRange(int n) {
        if (0 <= n - 1 && n - 1 <= 99)
            return true;
        else {
            System.out.println(ERROR);
            return false;
        }
    }
}
