package contacts;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu m;
        if (args.length == 2)
            m = new Menu(args[1]);
        else
            m = new Menu(null);
    }
}
