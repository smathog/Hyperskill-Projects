package converter.JSONComponents;

import java.util.LinkedHashMap;

public class JSONObject extends JSONContainer {
    private final LinkedHashMap<JSONString, JSONComponent> mappings;

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

    public void add(JSONString key, JSONComponent value) {
        mappings.put(key, value);
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
