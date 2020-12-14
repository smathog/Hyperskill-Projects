package recognition;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        //Utility.serializeData();
        //Utility.serializeToArrays();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess all the numbers");
            System.out.println("3. Guess number from text file");
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println("Your choice: " + choice);
            if (choice == 1) {
                System.out.println("New or Existing Network?");
                String choice2 = scanner.nextLine();
                Neuron p = null;
                if ("Existing".equalsIgnoreCase(choice2)) {
                    try {
                        p = (Neuron) SerializationUtils.deserialize("trainedNeuron");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error deserializing neuron");
                        System.out.println(e.getMessage());
                        System.out.println(e.getStackTrace());
                        break;
                    }
                    System.out.println("Dimensions of network: " + p.getDimensions());
                    Trainer.trainNeuron(p, scanner);

                } else if ("New".equalsIgnoreCase(choice2)) {
                    System.out.println("Enter the size of the layers: ");
                    int[] size = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
                    System.out.println("Learning...");
                    p = new Neuron(size);
                    Trainer.trainNeuron(p, scanner);
                } else
                    continue;
                try {
                    SerializationUtils.serialize(p, "trainedNeuron");
                    System.out.println("Done! Saved to the file.");
                } catch (IOException e) {
                    System.out.println("Serialization failed: '");
                    System.out.println(e.getMessage());
                    break;
                }
            } else {
                Neuron p = null;
                try {
                    p = (Neuron) SerializationUtils.deserialize("C:\\Users\\Scott\\Desktop\\Digit Recognition\\trainedNeuron");
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error deserializing neuron");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    break;
                }

                if (choice == 2) {
                    System.out.println("Guessing: ");
                    int numRight = 0;
                    for (int i = 0; i < 70; ++i) {
                        var data = Utility.deserializeArray(i);
                        for (int j = 0; j < 1000; ++j) {
                            final int jCopy = j;
                            var input = data[j][0];
                            var output = data[j][1];
                            p.loadInputNodes(input);
                            p.compute();
                            int num = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(k -> data[jCopy][1][k])).get();
                            int neuronOutput = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(p::getOutput)).get();
                            if (num == neuronOutput)
                                ++numRight;
                        }
                        System.out.println("Processed: " + (i + 1) * 1000 + "/70000");
                    }
                    System.out.println("The network prediction accuracy: " + numRight + "/" + 70000 + ", " + (100 * numRight / 70000 + "%"));
                } else if (choice == 3) {
                    System.out.print("Enter filename: ");
                    String fileName = scanner.nextLine();
                    var data = Trainer.getFileData(fileName, true);
                    p.loadInputNodes(data.get(0).stream().mapToDouble(Double::doubleValue).toArray());
                    p.compute();
                    int number = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(p::getOutput)).get();
                    System.out.println("This number is " + number);
                    break;
                }
            }
        }
    }
}
