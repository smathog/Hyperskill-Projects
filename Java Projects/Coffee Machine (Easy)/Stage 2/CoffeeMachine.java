package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write how many cups of coffee you will need:");
        int numCups = scanner.nextInt();
        System.out.println("For " + numCups + " cups of coffee you will need:");
        System.out.println(numCups * 200 + " ml of water");
        System.out.println(numCups * 50 + " ml of milk");
        System.out.println(numCups * 15 + " g of coffee beans");
        
    }
}
