package search;

public class Main {
    public static void main(String[] args) {
        String inFile = null;
        if (args.length == 2 && "--data".equals(args[0]))
            inFile = args[1];
        SearchEngine se = new SearchEngine(inFile);
    }
}
