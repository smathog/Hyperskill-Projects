package metro;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
                    ArrayList<Map<String, List<String>>> nextMapList = new ArrayList<>();
                    ArrayList<Map<String, List<String>>> prevMapList = new ArrayList<>();
                    for (var entryObject : obj.getValue().getAsJsonArray()) {
                        var entry = entryObject.getAsJsonObject();
                        // Create a new station for this line
                        String stationName = entry
                                .getAsJsonPrimitive("name")
                                .getAsString().replace("\"", "");
                        JsonElement timeEntry = entry
                                .getAsJsonPrimitive("time");
                        int time = timeEntry == null ? 0 : timeEntry.getAsInt();
                        line.createStation(stationName, time);

                        // Record the next stops for this station
                        ArrayList<String> nextNames = new ArrayList<>();
                        for (JsonElement nextStation : entry.getAsJsonArray("next")) {
                            nextNames.add(nextStation.getAsString());
                        }
                        nextMapList.add(Collections.singletonMap(stationName, nextNames));

                        // Record the previous stops for this station
                        ArrayList<String> previousNames = new ArrayList<>();
                        for (JsonElement prevStation : entry.getAsJsonArray("prev")) {
                            previousNames.add(prevStation.getAsString());
                        }
                        prevMapList.add(Collections.singletonMap(stationName, previousNames));

                        // Record all transfers to be made
                        JsonArray transfer = entry.getAsJsonArray("transfer");
                        for (JsonElement element : transfer) {
                            String transferLine = element.getAsJsonObject()
                                    .get("line").getAsString().replace("\"", "");
                            String transferStation = element.getAsJsonObject()
                                    .get("station").getAsString().replace("\"", "");
                            transfersList.add(new String[]{lineName, stationName, transferLine, transferStation});
                        }
                    }
                    // Link all next stations for this line:
                    for (var map : nextMapList) {
                        map.forEach(line::setNext);
                    }

                    // Link all prev stations for this line:
                    for (var map : prevMapList) {
                        map.forEach(line::setPrev);
                    }

                    // Add line to map
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
                String[] commands = parseList.toArray(String[]::new);
                if (commands.length == 5
                        && ("/route".equals(commands[0])
                        || "/fastest-route".equals(commands[0]))
                        && lineMap.containsKey(commands[1])
                        && lineMap.containsKey(commands[3])) {
                    Line firstLine = lineMap.get(commands[1]);
                    Line secondLine = lineMap.get(commands[3]);
                    if ("/route".equals(commands[0])) {
                        Line.printRoute(firstLine, commands[2], secondLine, commands[4]);
                    } else { //fastest route
                        Line.printFastestRoute(firstLine, commands[2], secondLine, commands[4]);
                    }
                } else {
                    System.out.println("Invalid command.");
                }
            }
        }
    }
}
