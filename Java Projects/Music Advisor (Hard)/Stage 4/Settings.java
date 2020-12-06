package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public class Settings {
    private static boolean access = false;
    private static String serverPath = "https://accounts.spotify.com";
    private static String APIserverPath = "https://api.spotify.com";
    private static String redirectURI = "http://localhost:8080&response_type=code";
    private static String clientID = "ONE_RANDOM_STRING";
    private static String clientSecret = "AnOTHER_RANDOM_STRING";
    private static String authCode;
    private static String accessToken;
    private static HttpClient client = HttpClient.newBuilder().build();

    public static boolean authorizeAccess() {
        Server server = Server.getServer();
        server.start();
        System.out.println("use this link to request the access code: ");
        System.out.println(serverPath
                + "/authorize?client_id="
                + Settings.getClientID()
                + "&redirect_uri=" + redirectURI);
        System.out.println("waiting for code...");
        while (server.getCode() == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        if (server.getCode() != null) {
            System.out.println("code received");
            authCode = server.getCode();
            server.stop();
            return true;
        } else {
            System.out.println("try again");
            server.stop();
            return false;
        }
    }

    public static boolean getAccessToken() {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverPath + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code"
                        + "&code=" + authCode
                        + "&redirect_uri=" + redirectURI
                        + "&client_id=" + clientID
                        + "&client_secret=" + clientSecret))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject tokenObject = JsonParser.parseString(response.body()).getAsJsonObject();
            accessToken = tokenObject.get("access_token").getAsString();
            return true;
        } catch (InterruptedException | IOException e) {
            return false;
        }
    }

    public static JsonObject getRequest(String extension) {
        HttpRequest request = null;
        try {
             request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + Settings.displayAccessToken())
                    .uri(URI.create(APIserverPath + extension))
                    .GET()
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println(APIserverPath + extension);
            System.out.println(e.getMessage());
        }
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String getAPIserverPath() {
        return APIserverPath;
    }

    public static String getAuthCode() {
        return authCode;
    }

    public static String displayAccessToken() {
        return accessToken;
    }

    public static void setServerPath(String serverPath) {
        Settings.serverPath = serverPath;
    }

    public static void setAPIserverPath(String APIserverPath) {
        Settings.APIserverPath = APIserverPath;
    }

    public static void setAccess() {
        access = !access;
    }

    public static boolean getAccess() {
        return access;
    }

    public static String getClientID() {return clientID;}
}
