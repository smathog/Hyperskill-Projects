package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final long a = 53L;
    private static final long aInverse = 188_679_247L;
    private static final long m = 1_000_000_009L;

    @FunctionalInterface
    private interface TriFunction<P1, P2, P3, R> {
         R call(P1 p1, P2 p2, P3 p3);
    }

    public static void main(String[] args) {
        String database = args[1];
        String folderName = args[0];
        try (Stream<String> stream = Files.lines(Paths.get(database))){
            ExecutorService executor = Executors.newCachedThreadPool();
            var priorities = stream
                    .map(s -> s.replace("\"", ""))
                    .map(s -> s.split(";"))
                    //New simultaneous Rabin-Karp implementation
                    .collect(Collectors.groupingBy(
                            s -> s[1].length(),
                            HashMap::new,
                            Collectors.toMap(
                                    s -> hashString(s[1]),
                                    Function.identity(),
                                    (s, v) -> s,
                                    HashMap::new
                            )
                    ));
                    //Old sequential KMP implementation
/*                    .collect(Collectors.toMap(
                            s -> s[1],
                            Function.identity(),
                            (s, v) -> s,
                            HashMap::new
                    ));*/


            var files = Files.list(Paths.get(folderName))
                    //New simultaneous Rabin-Karp implementation
                    .map(p -> new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                String fileContents = new String(Files.readAllBytes(p));
                                var result = multiLengthRabinKarp(fileContents, priorities);
                                return result.map(s -> p.toString() + ": " + s).orElseGet(() -> p.toString() + ": " + "Unknown file type");
                            } catch (IOException e) {
                                return "ERROR: " + e.getMessage();
                            }
                        }
                    })
                    //Old sequential KMP implementation
/*                    .map(p -> new Callable<String>() {
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
                    })*/
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

    //Returns highest priority matched filetype
    private static Optional<String> multiLengthRabinKarp(String text, HashMap<Integer, HashMap<Long, String[]>> lengthMap) {
        return lengthMap.values().stream()
                .map(stringMap -> singleLengthRabinKarp(text, stringMap))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Comparator.comparingInt(s -> Integer.parseInt(s[0])))
                .map(s -> s[2]);
    }

    //Returns highest priority matched filetype
    private static Optional<String[]> singleLengthRabinKarp(String text, HashMap<Long, String[]> idMap) {
        if (idMap == null || idMap.isEmpty())
            throw new IllegalArgumentException("Empty map passed to singleLengthRabinKarp");
        int length = idMap.entrySet().stream().findAny().get().getValue()[1].length();
        if (length > text.length())
            return Optional.empty();

        //Get hash of first length-long substring and power for rolling hash
        long hash = 0;
        long pow = 0;
        for (int i = 0; i < length; ++i) {
            if (i == 0) {
                hash = charToLong(text.charAt(i));
                pow = 1;
            } else {
                pow = Math.floorMod(pow * a, m);
                hash = Math.floorMod(hash + charToLong(text.charAt(i)) * pow, m);
            }
        }

        String[] match = null;

        TriFunction<Integer, Long, String[], String[]> updateMatch = (i, h, m) -> {
            if (idMap.containsKey(h)) {
                String[] typeMatch = idMap.get(h);
                if (text.substring(i, i + length).equals(typeMatch[1])) {
                    if (m == null)
                        m = typeMatch;
                    else if (Integer.parseInt(m[0]) < Integer.parseInt(typeMatch[0]))
                        m = typeMatch;
                }
            }
            return m;
        };
        match = updateMatch.call(0, hash, match);
        for (int i = 1; i < text.length() - length + 1; ++i) {
            hash = Math.floorMod(hash - charToLong(text.charAt(i - 1)), m);
            hash = Math.floorMod(hash * aInverse, m);
            hash = Math.floorMod(hash + charToLong(text.charAt(i + length - 1)) * pow, m);
            match = updateMatch.call(i, hash, match);
        }
        return Optional.ofNullable(match);
    }

    private static long charToLong(char ch) {
        return ch - 'A' + 1;
    }

    private static long hashString(String key) {
        long hash = 0;
        long pow = 0;
        for (int i = 0; i < key.length(); ++i) {
            if (i == 0) {
                hash = charToLong(key.charAt(i));
                pow = 1;
            } else {
                pow = Math.floorMod(pow * a, m);
                hash = Math.floorMod(hash + charToLong(key.charAt(i)) * pow, m);
            }
        }
        return hash;
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
