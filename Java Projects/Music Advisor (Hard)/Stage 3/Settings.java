package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public class Settings {
    private static boolean access = false;
    private static String serverPath = "https://accounts.spotify.com";
    private static String redirectURI = "http://localhost:8080&response_type=code";
    private static String clientID = "SOME_RANDOM_STRING";
    private static String clientSecret = "ANOTHER_RANDOM_STRING";
    private static String authCode;

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
        HttpClient client = HttpClient.newBuilder().build();
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
            System.out.println("response:");
            System.out.println(response.body());
            return true;
        } catch (InterruptedException | IOException e) {
            return false;
        }
    }

    public static String getAuthCode() {
        return authCode;
    }

    public static void setServerPath(String serverPath) {
        Settings.serverPath = serverPath;
    }

    public static void setAccess() {
        access = !access;
    }

    public static boolean getAccess() {
        return access;
    }

    public static String getClientID() {return clientID;}
}
