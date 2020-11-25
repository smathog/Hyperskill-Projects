package calculator;

import java.util.Scanner;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Arrays.stream(scanner.nextLine().split(" "))
            .mapToInt(Integer::parseInt)
            .sum());
    }
}
