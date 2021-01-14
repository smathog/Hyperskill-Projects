package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String database = args[1];
        String folderName = args[0];
        try (Stream<String> stream = Files.lines(Paths.get(database))){
            ExecutorService executor = Executors.newCachedThreadPool();
            var priorities = stream
                    .map(s -> s.replace("\"", ""))
                    .map(s -> s.split(";"))
                    .collect(Collectors.toMap(
                            s -> s[1],
                            Function.identity(),
                            (s, v) -> s,
                            HashMap::new
                    ));
            var files = Files.list(Paths.get(folderName))
                    .map(p -> new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                String fileContents = new String(Files.readAllBytes(p));
                                var result = priorities.entrySet().stream()
                                        .filter(e -> KMPSearch(fileContents, e.getKey()))
                                        .max(Comparator.comparingInt(e -> Integer.parseInt(e.getValue()[0])));
                                if (result.isPresent())
                                    return p.toString() + ": " + result.get().getValue()[2];
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
