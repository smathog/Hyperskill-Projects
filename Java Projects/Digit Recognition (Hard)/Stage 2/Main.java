package recognition;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
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
        int[][] convertedWeights = IntStream.range(0, 16)
                .mapToObj(i -> i < 15 ?
                        Arrays.stream(weights).mapToInt(a -> a[i]).toArray() :
                        new int[]{-1, 6, 1, 0, 2, 0, -1, 3, -2, -1})
                .toArray(int[][]::new);
        p.loadWeights(convertedWeights);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input grid: ");
        List<Integer> nodeValues = IntStream.range(0, 5)
                .flatMap(i -> scanner.nextLine().chars())
                .map(c -> c == 'X' ? 1 : 0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        p.loadNodes(nodeValues);
        p.compute();
        int number = IntStream.range(0, 10).boxed().max(Comparator.comparingInt(p::getOutput)).get();
        System.out.println("This number is " + number);
    }
}
