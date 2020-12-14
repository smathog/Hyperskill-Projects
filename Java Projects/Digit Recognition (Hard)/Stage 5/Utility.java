package recognition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utility {
    private static final String dir = "C:\\Users\\Scott\\Desktop\\MNIST Data\\data";

    public static void serializeData() {
        AtomicInteger ai = new AtomicInteger(0);
        IntStream.rangeClosed(1, 70000).parallel().forEach(i -> {
            try {
                SerializationUtils.serialize(Trainer.getFileData(dir + "\\" + String.format("%05d", i) + ".txt", false), "sample" + i);
                if (ai.addAndGet(1) % 1000 == 0)
                    System.out.println("Serializing: " + ai.get() + "/70000");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public static void serializeToArrays() {
        List<Integer> randomIndices = IntStream.rangeClosed(1, 70000).boxed().collect(Collectors.toList());
        Collections.shuffle(randomIndices);
        IntStream.range(0, 70).parallel().forEach(i -> {
            try {
                double[][][] array_i = new double[1000][2][];
                for (int j = 0; j < 1000; ++j) {
                    var list = deserializeDigit(randomIndices.get(i * 1000 + j));
                    array_i[j][0] = list.get(0).stream().mapToDouble(Double::doubleValue).toArray();
                    array_i[j][1] = list.get(1).stream().mapToDouble(Double::doubleValue).toArray();
                }
                SerializationUtils.serialize(array_i, "array" + i);
                System.out.println("Serialized array_" + i);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public static List<List<Double>> deserializeDigit(int i) {
        try {
            return (List<List<Double>>) SerializationUtils.deserialize("sample" + i);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deserializing sample " + i);
            return null;
        }
    }

    public static double[][][] deserializeArray(int i) {
        try {
            return (double[][][]) SerializationUtils.deserialize("array" + i);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deserializing sample " + i);
            return null;
        }
    }
}
