package solver;

import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        double[] nums = Arrays.stream(new Scanner(System.in).nextLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        System.out.println(nums[1] / nums[0]);
    }
}
