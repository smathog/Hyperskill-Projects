package metro;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        buildMap(args[0]).ifPresent(Main::parseCommands);
    }

    private static Optional<LinkedHashMap<String, LinkedList<String>>> buildMap(String fileLocation) {
        Path filePath = Paths.get(fileLocation);
        if (Files.notExists(filePath)) {
            System.out.println("Error! Such a file doesn't exist!");
            return Optional.empty();
        } else {
            // Get file as LinkedList of Strings...
            try {
                Gson gson = new Gson();
                Reader reader = Files.newBufferedReader(filePath);
                Type mapType = new TypeToken<LinkedHashMap<String, LinkedHashMap<String, String>>>(){}.getType();
                LinkedHashMap<String, LinkedHashMap<String, String>> jsonMap = gson.fromJson(reader, mapType);
                return Optional.of(jsonMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> {
                                    var list = e.getValue().entrySet().stream()
                                            .sorted(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())))
                                            .map(Map.Entry::getValue)
                                            .collect(Collectors.toCollection(LinkedList::new));
                                    list.addFirst("depot");
                                    list.addLast("depot");
                                    return list;
                                },
                                (prev, next) -> next,
                                LinkedHashMap::new
                        )));
            } catch (IOException e) {
                System.out.println("Incorrect file.");
                return Optional.empty();
            }
        }
    }

    private static void parseCommands(LinkedHashMap<String, LinkedList<String>> lineMap) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if ("/exit".equals(command)) {
                break;
            } else {
                // String parsing from: https://stackoverflow.com/a/7804472
                List<String> parseList = new ArrayList<>();
                Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
                while (m.find())
                    parseList.add(m.group(1).replace("\"", ""));
                String[] commands =  parseList.toArray(String[]::new);
                if (commands.length == 2 && "/output".equals(commands[0])) {
                    if (lineMap.containsKey(commands[1])) {
                        printLine(lineMap.get(commands[1]));
                    } else {
                        System.out.println("Invalid command.");
                    }
                } else if (commands.length == 3 && lineMap.containsKey(commands[1])) {
                    switch (commands[0]) {
                        case "/append": {
                            var list = lineMap.get(commands[1]);
                            list.set(list.size() - 1, commands[2]);
                            list.addLast("depot");
                            break;
                        }
                        case "/add-head": {
                            var list = lineMap.get(commands[1]);
                            list.set(0, commands[2]);
                            list.addFirst("depot");
                            break;
                        }
                        case "/remove": {
                            var list = lineMap.get(commands[1]);
                            if (!list.remove(commands[2])) {
                                System.out.println("Invalid command.");
                            }
                            break;
                        }
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                } else {
                    System.out.println("Invalid command.");
                }
            }
        }
    }

    private static void printLine(LinkedList<String> line) {
        for (int i = 0; i < line.size() - 2; ++i) {
            System.out.println(line.stream().skip(i)
                    .limit(3)
                    .collect(Collectors.joining("-")));
        }
    }
}
