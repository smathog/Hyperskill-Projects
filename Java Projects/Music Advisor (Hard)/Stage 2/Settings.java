package advisor;

public class Settings {
    private static boolean access = false;
    private static String clientID = "f030e1f80d4c4a6aa5e967b371431971";

    public static void setAccess() {
        access = !access;
    }

    public static boolean getAccess() {
        return access;
    }

    public static String getClientID() {return clientID;}
}
