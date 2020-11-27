package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String target = scanner.nextLine();
        int index = Arrays.stream(line.split(" "))
                .collect(Collectors.toCollection(ArrayList::new))
                .indexOf(target);
        if (index != -1)
            System.out.println(index + 1);
        else
            System.out.println("Not Found");
    }
}
