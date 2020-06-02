package readability;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String fileInput = readFileAsString(args[0]);
            double score = scoreString(fileInput);
            evaluateScore(score);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void evaluateScore(double score) {
        int intScore = (int) Math.ceil(score);
        String temp = null;
        switch (intScore) {
            case 1:
                temp = "5-6";
                break;
            case 2:
                temp = "6-7";
                break;
            case 3:
                temp = "7-9";
                break;
            case 4:
                temp = "9-10";
                break;
            case 5:
                temp = "10-11";
                break;
            case 6:
                temp = "11-12";
                break;
            case 7:
                temp = "12-13";
                break;
            case 8:
                temp = "13-14";
                break;
            case 9:
                temp = "14-15";
                break;
            case 10:
                temp = "15-16";
                break;
            case 11:
                temp = "16-17";
                break;
            case 12:
                temp = "17-18";
                break;
            case 13:
                temp = "18-24";
                break;
            case 14:
                temp = "24+";
                break;
            default:
                temp = "INVALID_AGE";
                break;
        }
        System.out.println("This text should be understood by " + temp + " year olds.");
    }

    public static double scoreString(String input) {
        String[] sentences = input.split("(?<=\\.)|(?<=!)|(?<=\\?)");
        ArrayList<String> words = new ArrayList<>();
        for (String s : sentences) {
            words.addAll(Arrays.asList(s.trim().split("\\s")));
        }
        int numWords = words.size();
        int numSentences = sentences.length;
        int numChars = 0;
        for (String word: words) {
            numChars += word.length();
        }
        double score = 4.71 * ((double) numChars) / numWords + 0.5 * ((double) numWords) / numSentences - 21.43;
        System.out.println("Words: " + numWords);
        System.out.println("Sentences: " + numSentences);
        System.out.println("Characters: " + numChars);
        System.out.println("The score is: " + score);
        return score;
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
