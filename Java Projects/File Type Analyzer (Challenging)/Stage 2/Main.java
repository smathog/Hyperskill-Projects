package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

public class Main {
    public static void main(String[] args) {
        String algoType = args[0];
        String fileName = args[1];
        String search = args[2];
        String type = args[3];
        try {
            String fileContents = new String(Files.readAllBytes(Paths.get(fileName)));
            BiPredicate<String, String> algo = null;
            switch (algoType) {
                case "--naive":
                    algo = Main::naiveSearch;
                    break;
                case "--KMP":
                    algo = Main::KMPSearch;
                    break;
            }
            var start = System.nanoTime();
            if (algo.test(fileContents, search))
                System.out.println(type);
            else
                System.out.println("Unknown file type");
            var end = System.nanoTime();
            System.out.println("It took " + TimeUnit.SECONDS.convert(end - start, TimeUnit.NANOSECONDS) + " seconds.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean naiveSearch(String text, String pattern) {
        if (text.length() < pattern.length())
            return false;
        for (int i = 0; i < text.length() - pattern.length() + 1; ++i) {
            for (int j = 0; j < pattern.length(); ++j)
                if (text.charAt(i + j) != pattern.charAt(j))
                    break;
                else if (j == pattern.length() - 1)
                    return true;
        }
        return false;
    }

    private static boolean KMPSearch(String text, String pattern) {
        int[] prefixFunc = prefixFunction(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); ++i) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j))
                j = prefixFunc[j - 1];
            if (text.charAt(i) == pattern.charAt(j))
                ++j;
            if (j == pattern.length())
                return true;

        }
        return false;
    }



    private static int[] prefixFunction(String str) {
        int[] prefixFunc = new int[str.length()];
        for (int i = 1; i < str.length(); ++i) {
            int j = prefixFunc[i - 1];
            while (j > 0 && str.charAt(i) != str.charAt(j))
                j = prefixFunc[j - 1];
            if (str.charAt(i) == str.charAt(j))
                ++j;
            prefixFunc[i] = j;
        }
        return prefixFunc;
    }
}
