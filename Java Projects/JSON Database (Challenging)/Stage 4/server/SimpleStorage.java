package server;

import com.google.gson.Gson;

import java.util.HashMap;


public class SimpleStorage {
    private final HashMap<String, String> storage;
    private static final Gson gson = new Gson();
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";
    private static final String NSKERROR = "No such key";

    public SimpleStorage() {
        storage = new HashMap<>();
    }

    public String set(String key, String s) {
        JsonResponse jr;
        storage.put(key, s);
        jr = new JsonResponse(OK, null, null);
        return gson.toJson(jr);
    }

    public String get(String key) {
        JsonResponse jr;
        if (!storage.containsKey(key) || "".equals(storage.get(key)))
            jr = new JsonResponse(ERROR, NSKERROR, null);
        else
            jr = new JsonResponse(OK, null, storage.get(key));
        return gson.toJson(jr);
    }

    public String delete(String key) {
        JsonResponse jr;
        if (storage.containsKey(key) && !"".equals(storage.get(key))) {
            storage.put(key, "");
            jr = new JsonResponse(OK, null, null);
        } else
            jr = new JsonResponse(ERROR, NSKERROR, null);
        return gson.toJson(jr);
    }
}
