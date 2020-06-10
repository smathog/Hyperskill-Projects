package readability;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.regex.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReadabilityScore readText = new ReadabilityScore(args[0]);
        readText.execute();
    }
}

class FileToStringConverter {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}

class ReadabilityScore {
    //Members
    private int words = 0;
    private int sentences = 0;
    private int characters = 0;
    private int syllables = 0;
    private int polysyllables = 0;

    private static Scanner scanner = new Scanner(System.in);

    //Constructor - Construction fills all int member fields in event of successful construction
    public ReadabilityScore(String filename) {
        try {
            String fileInput = FileToStringConverter.readFileAsString(filename);

            //Print text
            System.out.println("The text is: ");
            System.out.println(fileInput);

            //Parse and calculate
            String[] sentences = fileInput.split("(?<=\\.)|(?<=!)|(?<=\\?)");
            this.sentences = sentences.length;
            ArrayList<String> words = new ArrayList<>();
            for (String s : sentences) {
                words.addAll(Arrays.asList(s.trim().split("\\s")));
            }
            this.words = words.size();
            for (String word: words) {
                this.characters += word.length();
            }
            //Use regex to find syllables and polysyllables
            Pattern syllablePattern = Pattern.compile("[aeiouyAEIYOU]+[^aeiouyAEIOUY]+|[^aeiouyAEIOUY.]*[aiouyAIOUY]+[^aiouyAIOUY]*$");
            for (String word: words) {
                Matcher syllableMatcher = syllablePattern.matcher(word);
                int numVowels = 0;
                while (syllableMatcher.find()) {
                    numVowels++;
                }
                this.syllables += numVowels == 0 ? 1 : numVowels;
                if (numVowels > 2) {
                    this.polysyllables++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Handles I/O, manages which tests are run
    public void execute() {
        System.out.println();
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String choice = scanner.nextLine();
        System.out.println();
        double sumAges = 0;
        if ("all".equals(choice) || "ARI".equals(choice)) {
            double result = ARI();
            System.out.print("Automated Readability Index: " + result);
            sumAges += evaluateScore(result);
        }
        if ("all".equals(choice) || "FK".equals(choice)) {
            double result = FKI();
            System.out.print("Flesch–Kincaid readability tests: " + result);
            sumAges += evaluateScore(result);
        }
        if ("all".equals(choice) || "SMOG".equals(choice)) {
            double result = SMOG();
            System.out.print("Simple Measure of Gobbledygook: " + result);
            sumAges += evaluateScore(result);
        }
        if ("all".equals(choice) || "CL".equals(choice)) {
            double result = CLI();
            System.out.print("Coleman–Liau index: " + result);
            sumAges += evaluateScore(result);
        }
        sumAges = "all".equals(choice) ? sumAges / 4 : sumAges;
        System.out.println();
        System.out.println("This text should be understood in average by " + sumAges + " year olds.");
    }

    //Returns the ARI Score
    private double ARI() {
        return 4.71 * ((double) this.characters) / this.words
                + 0.5 * ((double) this.words) / this.sentences
                - 21.43;
    }

    //Returns the SMOG Score
    private double SMOG() {
        return 1.043 * Math.sqrt(this.polysyllables * 30 / ((double) this.sentences)) + 3.1291;
    }

    //Returns the CLI Score
    private double CLI() {
        double l = ((double) this.characters) / (100 * this.words);
        double s = ((double) this.sentences) / (100 * this.words);
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    //Returns the FK Score
    private double FKI() {
        return 0.39 * ((double) this.words) / this.sentences
                + 11.8 * ((double) this.syllables) / this.words
                - 15.59;
    }

    //Converts the scores into ages
    private double evaluateScore(double score) {
        int intScore = (int) Math.ceil(score);
        double temp = 0;
        switch (intScore) {
            case 1:
                temp = 6;
                break;
            case 2:
                temp = 7;
                break;
            case 3:
                temp = 9;
                break;
            case 4:
                temp = 10;
                break;
            case 5:
                temp = 11;
                break;
            case 6:
                temp = 12;
                break;
            case 7:
                temp = 13;
                break;
            case 8:
                temp = 14;
                break;
            case 9:
                temp = 15;
                break;
            case 10:
                temp = 16;
                break;
            case 11:
                temp = 17;
                break;
            case 12:
                temp = 18;
                break;
            case 13:
                temp = 24;
                break;
            case 14:
                temp = 24;
                break;
            default:
                temp = -1;
                break;
        }
        System.out.println(" (about " + temp + " year olds).");
        return temp;
    }
}