package advisor;

public class Settings {
    private static boolean access = false;
    private static String clientID = "RANDOM_STRING_THE_FIRST";

    public static void setAccess() {
        access = !access;
    }

    public static boolean getAccess() {
        return access;
    }

    public static String getClientID() {return clientID;}
}
