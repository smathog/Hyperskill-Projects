package recognition;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Perceptron p = new Perceptron(9);
        p.loadWeights(List.of(2, 1, 2, 4, -4, 4, 2, -1, 2, -5));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input grid: ");
        List<Integer> nodeValues = IntStream.range(0, 3)
                .flatMap(i -> scanner.nextLine().chars())
                .map(c -> c == 'X' ? 1 : 0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        p.loadNodes(nodeValues);
        p.compute();
        System.out.println("This number is " + (p.getOutput() > 0 ? 0 : 1));
    }
}
