package blockchain;

import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        IntStream.range(0, 5).
                mapToObj(i -> new Block())
                .forEach(b -> {
                    System.out.println(b);
                    System.out.println();
                });
    }
}
