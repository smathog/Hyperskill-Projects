package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String folderName = args[0];
        String search = args[1];
        String type = args[2];
        try {
            ExecutorService executor = Executors.newCachedThreadPool();
            var files = Files.list(Paths.get(folderName))
                    .map(p -> new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                String fileContents = new String(Files.readAllBytes(p));
                                if (KMPSearch(fileContents, search))
                                    return p.toString() + ": " + type;
                                else
                                    return p.toString() + ": " + "Unknown file type";
                            } catch (IOException e) {
                               return "ERROR: " + e.getMessage();
                            }
                        }
                    })
                    .map(executor::submit)
                    .collect(Collectors.toList());
            files.forEach(f -> {
                try {
                    System.out.println(f.get());
                } catch (Exception e){
                    System.out.println("ruh roh");
                }
            });
            executor.shutdown();
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
