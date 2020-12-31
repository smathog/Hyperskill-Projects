package server;

public class JsonResponse {
    public String response;
    public String reason;
    public String value;

    public JsonResponse(String response, String reason, String value) {
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

    public String getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }
}
