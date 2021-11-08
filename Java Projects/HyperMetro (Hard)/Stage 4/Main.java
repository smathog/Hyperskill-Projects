package metro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        buildMap(args[0]).ifPresent(Main::parseCommands);
    }

    private static Optional<LinkedHashMap<String, Line>> buildMap(String fileLocation) {
        Path filePath = Paths.get(fileLocation);
        if (Files.notExists(filePath)) {
            System.out.println("Error! Such a file doesn't exist!");
            return Optional.empty();
        } else {
            try {
                Reader reader = Files.newBufferedReader(filePath);
                JsonObject root = new JsonParser().parse(reader).getAsJsonObject();
                LinkedHashMap<String, Line> lineMap = new LinkedHashMap<>();
                // Transfers are hooked up once the lineMap is completed
                ArrayList<String[]> transfersList = new ArrayList<>();
                for (var obj: root.entrySet()) {
                    String lineName = obj.getKey().replace("\"", "");
                    Line line = new Line(lineName);
                    for (var entry : obj.getValue().getAsJsonObject().entrySet()) {
                        String stationName = entry.getValue().getAsJsonObject()
                                .getAsJsonPrimitive("name").getAsString().replace("\"", "");
                        line.appendStation(stationName);
                        JsonArray transfer = entry.getValue().getAsJsonObject()
                                .getAsJsonArray("transfer");
                        if (transfer.size() != 0) {
                            String transferLine = transfer.get(0).getAsJsonObject()
                                    .get("line").getAsString().replace("\"", "");
                            String transferStation = transfer.get(0).getAsJsonObject()
                                    .get("station").getAsString().replace("\"", "");
                            transfersList.add(new String[]{lineName, stationName, transferLine, transferStation});
                        }
                    }
                    lineMap.put(lineName, line);

                }
                // Add transfers to all lines
                for (String[] transferInfo : transfersList) {
                    lineMap.get(transferInfo[0]).addTransfer(transferInfo[1],
                            lineMap.get(transferInfo[2]),
                            transferInfo[3]);
                }

                return Optional.of(lineMap);
            } catch (IOException e) {
                System.out.println("Incorrect file.");
                return Optional.empty();
            }
        }
    }

    private static void parseCommands(LinkedHashMap<String, Line> lineMap) {
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
                        lineMap.get(commands[1]).printLine();
                    } else {
                        System.out.println("Invalid command.");
                    }
                } else if (commands.length == 3 && lineMap.containsKey(commands[1])) {
                    var line = lineMap.get(commands[1]);
                    switch (commands[0]) {
                        case "/append": {
                            line.appendStation(commands[2]);
                            break;
                        }
                        case "/add-head": {
                            line.addHead(commands[2]);
                            break;
                        }
                        case "/remove": {
                            if (!line.removeStation(commands[2])) {
                                System.out.println("Invalid command.");
                            }
                            break;
                        }
                        default:
                            System.out.println("Invalid command.");
                            break;
                    }
                } else if (commands.length == 5
                        && ("/connect".equals(commands[0])
                        || "/route".equals(commands[0]))
                        && lineMap.containsKey(commands[1])
                        && lineMap.containsKey(commands[3])) {
                    Line firstLine = lineMap.get(commands[1]);
                    Line secondLine = lineMap.get(commands[3]);
                    if ("/connect".equals(commands[0])) {
                        firstLine.addTransfer(commands[2], secondLine, commands[4]);
                        secondLine.addTransfer(commands[4], firstLine, commands[2]);
                    }
                    else { //if route requested
                        Line.printRoute(firstLine, commands[2], secondLine, commands[4]);
                    }
                } else {
                    System.out.println("Invalid command.");
                }
            }
        }
    }
}
