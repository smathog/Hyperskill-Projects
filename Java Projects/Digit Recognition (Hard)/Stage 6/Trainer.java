package recognition;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Trainer {
    public static List<List<Double>> getFileData(String filename, boolean discardSecond) {
        try (final Scanner scanner = new Scanner(new File(filename))){
            var list1 = IntStream.range(0, 28)
                    .mapToDouble(i -> (double) i)
                    .flatMap(i -> Arrays.stream(scanner.nextLine().split("\\s"))
                            .mapToInt(Integer::parseInt)
                            .mapToDouble(j -> ((double) j) / 255))
                    .boxed()
                    .collect(Collectors.toList());
            List<Double> list2 = new ArrayList<>();
            if (!discardSecond) {
                int num = Integer.parseInt(scanner.nextLine());
                list2 = IntStream.range(0, 10).mapToDouble(i -> i == num ? 1d : 0d).boxed().collect(Collectors.toList());
            }
            return List.of(list1, list2);
        } catch (IOException e) {
            System.out.println("Error opening " + filename);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return null;
        }
    }

    public static void printData(List<Double> input, List<Double> output) {
        for (int i = 0; i < 28; ++i) {
            for (int j = 0; j < 28; ++j)
                System.out.print(input.get(i * 28 + j) + "\t\t\t");
            System.out.println();
        }
        System.out.println("Number: " + IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(output::get)).get());
    }

    public static void trainNeuron(Neuron n, Scanner s) {
        System.out.print("Initial epoch runtime: ");
        int numEpochs = Integer.parseInt(s.nextLine());
        int numEpochsCompleted = 0;
        int currentIndex = 0;
        Random r = new Random();
        double successRate = 0;
        double runningAverage = 0;
        int counter = 0;
        while (true) {
            if (currentIndex == 70) {
                ++numEpochsCompleted;
                currentIndex = 0;
                n.incrementEpoch();
                System.out.println("Epoch completed: " + n.getEpoch());
                if (numEpochs == numEpochsCompleted) {
                    System.out.print("Continue? Y/N: ");
                    String choice = s.nextLine();
                    if ("Y".equalsIgnoreCase(choice)) {
                        System.out.println("How many more epochs to run?");
                        numEpochs = Integer.parseInt(s.nextLine());
                        numEpochsCompleted = 0;
                    } else
                        break;
                }
            }
            int numMatches = 0;
            long dsStart = System.currentTimeMillis();
            var data = Utility.deserializeArray(currentIndex);
            for (int i = 0; i < 1000; ++i) {
                double[] input = data[i][0];
                double[] output = data[i][1];
                n.trainNeuron(input, output);
                final int iCopy = i;
                int num = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(j -> data[iCopy][1][j])).get();
                int neuronOutput = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(n::getOutput)).get();
                if (num == neuronOutput)
                    ++numMatches;
            }
            successRate = ((double) numMatches)/ 10;
            ++counter;
            runningAverage = (runningAverage * (counter - 1) + successRate) / counter;
            System.out.println("Iteration: " + counter + "\tSuccess rate: " + successRate + "%\t\tRunning Average: " + runningAverage + "%");
            System.out.println("Total time: " + (System.currentTimeMillis() - dsStart));
            ++currentIndex;
        }
    }
}
