package recognition;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess a number");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                System.out.println("Learning...");
                Neuron p = new Neuron(15, 10);
                int[][] weights = {
                        {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1},  //0
                        {-1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1},  //1
                        {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},  //2
                        {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},  //3
                        {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},  //4
                        {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},  //5
                        {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},  //6
                        {1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},  //7
                        {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},  //8
                        {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1}}; //9
                List<List<Integer>> samplesIn = Arrays.stream(weights).
                        map(a -> Arrays.stream(a).map(i -> i == 1 ? 1 : 0).boxed().collect(Collectors.toList()))
                        .collect(Collectors.toList());
                List<List<Integer>> samplesOut = IntStream.range(0, 10)
                        .mapToObj(i -> IntStream.range(0, 10).map(j -> j == i ? 1 : 0).boxed().collect(Collectors.toList()))
                        .collect(Collectors.toList());
                p.trainNeuron(samplesIn, samplesOut);
                try {
                    SerializationUtils.serialize(p, "trainedNeuron");
                    System.out.println("Done! Saved to the file.");
                } catch (IOException e) {
                    System.out.println("Serialization failed: '");
                    System.out.println(e.getMessage());
                    break;
                }
            }
            else if (choice == 2) {
                Neuron p;
                try {
                    p = (Neuron) SerializationUtils.deserialize("trainedNeuron");
                } catch (IOException | ClassNotFoundException e) {
                    //need to learn itself
                    p = new Neuron(15, 10);
                    int[][] weights = {
                            {1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, 1, 1, 1},  //0
                            {-1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1},  //1
                            {1, 1, 1, -1, -1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1},  //2
                            {1, 1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1},  //3
                            {1, -1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1},  //4
                            {1, 1, 1, 1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1, 1},  //5
                            {1, 1, 1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, 1},  //6
                            {1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1, -1, -1, 1},  //7
                            {1, 1, 1, 1, -1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1},  //8
                            {1, 1, 1, 1, -1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1}}; //9
                    List<List<Integer>> samplesIn = Arrays.stream(weights).
                            map(a -> Arrays.stream(a).map(i -> i == 1 ? 1 : 0).boxed().collect(Collectors.toList()))
                            .collect(Collectors.toList());
                    List<List<Integer>> samplesOut = IntStream.range(0, 10)
                            .mapToObj(i -> IntStream.range(0, 10).map(j -> j == i ? 1 : 0).boxed().collect(Collectors.toList()))
                            .collect(Collectors.toList());
                    p.trainNeuron(samplesIn, samplesOut);
                }
                System.out.println("Input grid: ");
                List<Integer> nodeValues = IntStream.range(0, 5)
                        .flatMap(i -> scanner.nextLine().chars())
                        .map(c -> c == 'X' ? 1 : 0)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                p.loadInputNodes(nodeValues);
                p.compute();
                int number = IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(p::getOutput)).get();
                System.out.println("This number is " + number);
                break;
            }
        }
    }
}
