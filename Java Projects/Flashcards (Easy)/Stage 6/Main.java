package flashcards;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        FlashCardDeck deck = new FlashCardDeck();
        while (deck.handleInput()) {
            deck.logThenPrint("");
        }
    }
}

class FlashCardDeck {
    private final Map<String, String> termMap = new LinkedHashMap<>();
    private final Map<String, String> definitionMap = new LinkedHashMap<>();
    private final Map<String, Integer> hardestTermsMap = new LinkedHashMap<>();
    private final List<String> log = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public boolean handleInput() {
        logThenPrint("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats): ");
        String command = getAndLogInputString();
        if ("add".equals(command)) {
            addCardWithChecks();
            return true;
        } else if ("remove".equals(command)) {
            removeCardWithChecks();
            return true;
        } else if ("import".equals(command)) {
            importCardsFromFile();
            return true;
        } else if ("export".equals(command)) {
            exportCardsToFile();
            return true;
        } else if ("ask".equals(command)) {
            quizNQuestions();
            return true;
        } else if ("log".equals(command)) {
            log();
            return true;
        } else if ("hardest card".equals(command)) {
            identifyHardestTerms();
            return true;
        } else if ("reset stats".equals(command)) {
            resetStats();
            return true;
        } else if ("exit".equals(command)) {
            logThenPrint("Bye bye!");
            return false;
        } else {
            logThenPrint("Invalid command input. Try again.");
            return true;
        }
    }

    private void identifyHardestTerms() {
        int mostWrong = hardestTermsMap.isEmpty() ? 0 : Collections.max(hardestTermsMap.values());
        if (mostWrong == 0) {
            logThenPrint("There are no cards with errors.");
        } else {
            ArrayList<String> hardestTerms = new ArrayList<>();
            for (String term : hardestTermsMap.keySet()) {
                if (hardestTermsMap.get(term).intValue() == mostWrong) {
                    hardestTerms.add(term);
                }
            }
            if (hardestTerms.size() == 1) {
                logThenPrint("The hardest card is "
                            + "\"" + hardestTerms.get(0)  + "\"."
                            + " You have " + mostWrong
                            + " errors answering it.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hardestTerms.size(); i++) {
                    if (i == 0) {
                        sb.append("\"" + hardestTerms.get(i) + "\",");
                    } else if (i == hardestTerms.size() - 1) {
                        sb.append(" \"" + hardestTerms.get(i) + "\"");
                    } else {
                        sb.append(" \"" + hardestTerms.get(i) + "\",");
                    }
                }
                logThenPrint("The hardest cards are "
                            + sb.toString() + "."
                            + "You have " + mostWrong
                            + " errors in answering them.");
            }
        }
    }

    private void resetStats() {
        hardestTermsMap.clear();
        for (String term : termMap.keySet()) {
            hardestTermsMap.put(term, 0);
        }
        logThenPrint("Card statistics has been reset");
    }

    public void logThenPrint(String out) {
        log.add(out);
        System.out.println(out);
    }

    private String getAndLogInputString() {
        String input = scanner.nextLine();
        log.add(input);
        return input;
    }

    private int getAndLogInputInt() {
        String input = scanner.nextLine();
        int temp = Integer.parseInt(input);
        log.add(input);
        return temp;
    }

    private void log() {
        logThenPrint("File name: ");
        String filename = getAndLogInputString();
        File exportFile = new File(filename);
        try (PrintWriter printWriter = new PrintWriter(exportFile)) {
            for (var logEntry : log) {
                printWriter.println(logEntry);
            }
            logThenPrint("The log has been saved.");
        } catch (FileNotFoundException e) {
            logThenPrint("File not found : " + e.getMessage());
        }
    }

    private boolean checkDuplicateTerm(String term) {
        if (termMap.containsKey(term)) {
            logThenPrint("The card \"" + term + "\" already exists.");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDuplicateDefinition(String definition) {
        if (definitionMap.containsKey(definition)) {
            logThenPrint("The definition \"" + definition + "\" already exists.");
            return false;
        } else {
            return true;
        }
    }

    private void addCardWithChecks() {
        logThenPrint("The card: ");
        String term = getAndLogInputString();
        //Ensure is not a duplicate term
        if (!checkDuplicateTerm(term)) {
            return;
        }
        logThenPrint("The definition of the card: ");
        String definition = getAndLogInputString();
        if (!checkDuplicateDefinition(definition)) {
            return;
        }
        addCard(term, definition, 0);
        logThenPrint("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
    }

    private void addCard(String term, String definition, int numErrors) {
        termMap.put(term, definition);
        definitionMap.put(definition, term);
        hardestTermsMap.put(term, numErrors);
    }

    private void removeCardWithChecks() {
        logThenPrint("The card: ");
        String term = getAndLogInputString();
        if (termMap.containsKey(term)) {
            String definition = termMap.get(term);
            termMap.remove(term);
            definitionMap.remove(definition);
            hardestTermsMap.remove(term);
            logThenPrint("The card has been removed.");
        } else {
            logThenPrint("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    private void importCardsFromFile() {
        logThenPrint("File name: ");
        String filename = getAndLogInputString();
        File importFile = new File(filename);
        if (!importFile.exists()) {
            logThenPrint("File not found.");
            return;
        } else {
            try (Scanner fileScanner = new Scanner(importFile)) {
                int counter = 0;
                while (fileScanner.hasNext()) {
                    String term = fileScanner.nextLine();
                    String definition = fileScanner.nextLine();
                    int numErrors = Integer.parseInt(fileScanner.nextLine());
                    if (termMap.containsKey(term)) {
                        String oldDefinition = termMap.get(term);
                        definitionMap.remove(oldDefinition);
                    }
                    addCard(term, definition, numErrors);
                    counter++;
                }
                logThenPrint(counter + " cards have been loaded.");
            } catch (FileNotFoundException e) {
                logThenPrint("File not found: " + e.getMessage());
            }
        }
    }

    private void exportCardsToFile() {
        logThenPrint("File name: ");
        String filename = getAndLogInputString();
        File exportFile = new File(filename);
        try (PrintWriter printWriter = new PrintWriter(exportFile)) {
            int counter = 0;
            for (var pair : termMap.entrySet()) {
                printWriter.println(pair.getKey());
                printWriter.println(pair.getValue());
                printWriter.println(hardestTermsMap.get(pair.getKey()).intValue());
                counter++;
            }
            logThenPrint(counter + " cards have been saved.");
        } catch (FileNotFoundException e) {
            logThenPrint("File not found : " + e.getMessage());
        }
    }

    private void quizNQuestions() {
        logThenPrint("How many times to ask?");
        int n = getAndLogInputInt();
        String[] termArray = new String[termMap.size()];
        System.arraycopy(termMap.keySet().toArray(), 0, termArray, 0, termMap.size());
        Random random = new Random(1);
        for (int i = 0; i < n; i++) {
            int questionNum = random.nextInt(termArray.length);
            String term = termArray[questionNum];
            logThenPrint("Print the definition of \"" + term + "\":");
            String definition = getAndLogInputString();
            checkResponse(term, definition);
        }
    }

    private boolean checkResponse(String term, String definition) {
        if (!definitionMap.containsKey(definition)) {
            logThenPrint("Wrong answer. The correct one is \"" + termMap.get(term) + "\".");
            hardestTermsMap.put(term, hardestTermsMap.get(term) + 1);
            return false;
        } else if (!definition.equalsIgnoreCase(termMap.get(term))) {
            logThenPrint("Wrong answer. The correct one is \"" + termMap.get(term)
                    + "\", you've just written the definition of \"" + definitionMap.get(definition)
                    + "\".");
            hardestTermsMap.put(term, hardestTermsMap.get(term) + 1);
            return false;
        } else {
            logThenPrint("Correct answer.");
            return true;
        }
    }
}

