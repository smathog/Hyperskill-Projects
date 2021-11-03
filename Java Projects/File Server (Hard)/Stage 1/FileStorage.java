package server;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileStorage {
    private final HashSet<String> storedFiles;
    private final HashSet<String> validOps;

    public FileStorage() {
        storedFiles = new HashSet<>();
        validOps = Arrays.stream(Ops.values())
                .map(op -> op.toString().toLowerCase())
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Accepts input and attempts to respond to it until exit is received
     */
    public void parseLoop() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if ("exit".equals(input)) {
                break;
            } else {
                executeCommand(parseInput(input));
            }
        }
    }

    /**
     *
     * @param response to be executed
     */
    private void executeCommand(ParseResponse response) {
        String file = response.getFileName();
        Ops op = response.getOperation();
        switch (op) {
            case ADD: {
                if (file.matches("file(10|[1-9])") && !storedFiles.contains(file)) {
                    storedFiles.add(file);
                    System.out.printf("The file %s added successfully%n", file);
                } else {
                    System.out.printf("Cannot add the file %s%n", file);
                }
                break;
            }
            case GET: {
                if (storedFiles.contains(file)) {
                    System.out.printf("The file %s was sent%n", file);
                } else {
                    System.out.printf("The file %s not found%n", file);
                }
                break;
            }
            case DELETE: {
                if (storedFiles.remove(file)) {
                    System.out.printf("The file %s was deleted%n", file);
                } else {
                    System.out.printf("The file %s not found%n", file);
                }
                break;
            }
        }
    }

    /**
     *
     * @param input to be validated
     * @return The requested
     */
    private ParseResponse parseInput(String input) {
        String[] portions = input.split(" ");
        return new ParseResponse(Ops.valueOf(portions[0].toUpperCase()), portions[1]);
    }

    public static class ParseResponse {
        private final Ops operation;
        private final String fileName;

        public ParseResponse(Ops operation, String fileName) {
            this.operation = operation;
            this.fileName = fileName;
        }


        public Ops getOperation() {
            return operation;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private enum Ops {
        ADD,
        GET,
        DELETE,
    }
}
