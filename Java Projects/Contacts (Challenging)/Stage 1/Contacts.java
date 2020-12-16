package contacts;

import java.util.ArrayList;
import java.util.Scanner;

public class Contacts {
    private ArrayList<Record> records;
    private Scanner scanner;

    public Contacts(Scanner scanner) {
        records = new ArrayList<>();
        this.scanner = scanner;
    }

    public void addRecord() {
        System.out.println("Enter the name of the person:");
        String name = scanner.nextLine();
        System.out.println("Enter the surname of the person:");
        String surname = scanner.nextLine();
        System.out.println("Enter the number:");
        String number = scanner.nextLine();
        records.add(new Record(name, surname, number));
        System.out.println();
        System.out.println("A record created!");
    }
}
