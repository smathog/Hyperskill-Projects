package server;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimpleStorage s = new SimpleStorage();
        whileLoop:
        while (true) {
            String[] inputs = scanner.nextLine().split(" ", 3);
            String command = inputs[0];
            switch (command) {
                case "set":
                    s.set(Integer.parseInt(inputs[1]), inputs[2]);
                    break;
                case "get":
                    s.get(Integer.parseInt(inputs[1]));
                    break;
                case "delete":
                    s.delete(Integer.parseInt(inputs[1]));
                    break;
                case "exit":
                    break whileLoop;
                default:
                    break;
            }
        }
    }
}
