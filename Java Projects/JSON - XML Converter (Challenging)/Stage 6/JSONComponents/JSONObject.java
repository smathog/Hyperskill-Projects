package converter.JSONComponents;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class JSONObject extends JSONContainer {
    private LinkedHashMap<JSONString, JSONComponent> mappings;

    public JSONObject() {
        mappings = new LinkedHashMap<>();
    }

    public boolean hasField(String field) {
        return mappings.containsKey(new JSONString(field));
    }

    public JSONComponent getValue(String field) {
        return mappings.get(new JSONString(field));
    }

    public LinkedHashMap<JSONString, JSONComponent> getMappings() {
        return mappings;
    }

    public void purgeAttributes() {
        //Delete empty keys and those keys that are duplicates when symbols removed
        ArrayList<JSONString> keys = new ArrayList<>(mappings.keySet());
        for (var key : keys) {
            String sk = key.toString();
            if (sk.isEmpty() || "@".equals(sk) || "#".equals(sk))
                mappings.remove(key);
            if ((sk.startsWith("@") || sk.startsWith("#")) && sk.length() >= 2 && mappings.containsKey(new JSONString(sk.substring(1))))
                mappings.remove(key);
        }

        //Reassemble map
        mappings = mappings.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> {
                            String key = e.getKey().toString();
                            if ((key.startsWith("@") || key.startsWith("#")) && key.length() >= 2)
                                return new JSONString(key.substring(1));
                            else
                                return e.getKey();
                        },
                        e -> e.getValue(),
                        (k1, k2) -> k1,
                        LinkedHashMap::new
                ));
    }

    public void add(JSONString key, JSONComponent value) {
        mappings.put(key, value);
    }

    @Override
    public boolean isEmpty() {
        return mappings.isEmpty();
    }

    @Override
    public boolean isJSONPrimitive() {
        return false;
    }

    @Override
    public boolean isJSONObject() {
        return true;
    }

    @Override
    public String JSONRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        var es = mappings.entrySet();
        for (var it = es.iterator(); it.hasNext(); ) {
            var entry = it.next();
            sb.append(" " + entry.getKey().JSONRepresentation());
            sb.append(" :");
            sb.append(" " + entry.getValue().JSONRepresentation());
            if (it.hasNext())
                sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }
}
