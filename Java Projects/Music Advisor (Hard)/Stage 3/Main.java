package advisor;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < args.length - 1; ++i)
            if ("-access".equals(args[i]))
                Settings.setServerPath(args[i + 1]);
        Menu menu = new Menu();
        menu.MenuLoop();
    }
}
