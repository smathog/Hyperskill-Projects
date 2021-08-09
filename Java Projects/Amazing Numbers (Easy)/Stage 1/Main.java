package numbers;

import java.text.MessageFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a natural number: ");
        int num = Integer.parseInt(scanner.nextLine());
        if (num > 0) {
            if (num % 2 == 0) {
                System.out.println("This number is Even.");
            } else {
                System.out.println("This number is Odd.");
            }
            //Determine whether num is Buzz number
            String explanation;
            boolean isBuzz;
            if (num % 7 == 0 && num % 10 == 7) {
                explanation = "is divisible by 7 and ends with 7";
                isBuzz = true;
            } else if (num % 7 == 0 && num % 10 != 7) {
                explanation = "is divisible by 7";
                isBuzz = true;
            } else if (num % 7 != 0 && num % 10 == 7) {
                explanation = "ends with 7";
                isBuzz = true;
            } else {
                explanation = "is neither divisible by 7 nor does it end with 7";
                isBuzz = false;
            }
            //Output regarding Buzz
            if (isBuzz) {
                System.out.println("It is a Buzz number.");
            } else {
                System.out.println("It is not a Buzz number.");
            }
            System.out.println("Explanation: ");
            System.out.println(MessageFormat.format("{0} {1}.", num, explanation));
        } else {
            System.out.println("This number is not natural!");
        }
    }
}
