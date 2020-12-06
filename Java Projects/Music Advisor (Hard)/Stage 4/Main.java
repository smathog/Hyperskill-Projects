package advisor;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < args.length - 1; ++i) {
            if ("-access".equals(args[i]))
                Settings.setServerPath(args[i + 1]);
            else if ("-resource".equals(args[i]))
                Settings.setAPIserverPath(args[i + 1]);
        }
        Menu menu = new Menu();
        menu.MenuLoop();
    }
}
