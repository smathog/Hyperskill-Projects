package server;

import com.google.gson.JsonElement;

public class JsonResponse {
    public String response;
    public String reason;
    public JsonElement value;

    public JsonResponse(String response, String reason, JsonElement value) {
        this.response = response;
        this.reason = reason;
        this.value = value;
    }

    public String getResponse() {
        return response;
    }

    public String getReason() {
        return reason;
    }

    public boolean hasReason() {
        return reason != null;
    }

    public JsonElement getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }
}
