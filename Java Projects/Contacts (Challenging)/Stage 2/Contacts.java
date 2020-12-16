package contacts;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class Contacts {
    private static Pattern numberPattern = Pattern.compile("((\\+?[^_\\W]{1,})([ \\-][^_\\W]{2,})*)" +
            "|((\\+?\\([^_\\W]{1,}\\))([ \\-][^_\\W]{2,})*)" +
            "|(\\+?[^_\\W]{1,}[ \\-]\\([^_\\W]{2,}\\)([ \\-][^_\\W]{2,})*)");

    private ArrayList<Record> records;
    private Scanner scanner;

    public Contacts(Scanner scanner) {
        records = new ArrayList<>();
        this.scanner = scanner;
    }

    public void addRecord() {
        System.out.print("Enter the name of the person: ");
        String name = scanner.nextLine();
        System.out.print("Enter the surname of the person: ");
        String surname = scanner.nextLine();
        System.out.print("Enter the number: ");
        String number = scanner.nextLine();
        if (validateNumber(number)) {
            records.add(new Record(name, surname, number));
        } else {
            System.out.println("Wrong number format!");
            records.add(new Record(name, surname, ""));
        }
        System.out.println("The record added.");
    }

    public void countRecords() {
        System.out.println("The Phone Book has " + records.size() + " records.");
    }

    public void deleteRecord() {
        if (records.isEmpty()) {
            System.out.println("No records to remove!");
            return;
        }
        listRecords();
        System.out.print("Select a record: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        records.remove(choice);
        System.out.println("The record removed!");
    }

    public void editRecord() {
        if (records.isEmpty()) {
            System.out.println("No records to edit!");
            return;
        }
        listRecords();
        System.out.print("Select a record: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        Record r = records.get(choice);
        System.out.print("Select a field (name, surname, number): ");
        String field = scanner.nextLine();
        if ("number".equals(field)) {
            System.out.print("Enter number: ");
            String number = scanner.nextLine();
            if (validateNumber(number))
                r.setNumber(number);
            else {
                System.out.println("Wrong number format!");
                r.setNumber("");
            }
            System.out.println("The record updated!");
        } else if ("name".equals(field)) {
            System.out.print("Enter name: ");
            r.setName(scanner.nextLine());
            System.out.println("The record updated!");
        } else if ("surname".equals(field)) {
            System.out.print("Enter surname: ");
            r.setSurname(scanner.nextLine());
            System.out.println("The record updated!");
        }
    }

    public void listRecords() {
        for (int i = 0; i < records.size(); ++i)
            System.out.println((i + 1) + ". " + records.get(i));
    }

    private boolean validateNumber(String number) {
        Matcher m = numberPattern.matcher(number);
        return m.matches();
    }
}
