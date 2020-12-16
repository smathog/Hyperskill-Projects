package banking;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < args.length - 1; ++i)
            if ("-fileName".equals(args[i])) {
                new Menu(args[i + 1]);
                break;
            }
    }
}