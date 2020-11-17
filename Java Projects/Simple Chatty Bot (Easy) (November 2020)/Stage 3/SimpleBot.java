package bot;

import java.util.Scanner;

public class SimpleBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! My name is Aid.");
        System.out.println("I was created in 2018.");
        System.out.println("Please, remind me your name.");

        String name = scanner.nextLine();

        System.out.println("What a great name you have, " + name + "!");
        System.out.println("Let me guess your age.");
        System.out.println("Enter remainders of dividing your age by 3, 5 and 7.");

        int[] mods = new int[3];
        for (int i = 0; i < 3; ++i)
            mods[i] = Integer.parseInt(scanner.nextLine());
        int age = (mods[0] * 70 + mods[1] * 21 + mods[2] * 15) % 105;

        System.out.println("Your age is " + age + "; that's a good time to start programming!");
    }
}
