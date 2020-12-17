package contacts;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class Contacts {
    private ArrayList<Record> records;
    private Scanner scanner;

    public Contacts(Scanner scanner) {
        records = new ArrayList<>();
        this.scanner = scanner;
    }

    public void addRecord() {
        System.out.print("Enter the type (person, organization): ");
        String type = scanner.nextLine();
        switch (type) {
            case "person":
                records.add(new PersonRecord(scanner));
                break;
            case "organization":
                records.add(new OrganizationRecord(scanner));
                break;
            default:
                break;
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
        r.editRecord(scanner);
        System.out.println("The record updated!");
    }

    public void infoRecord() {
        listRecords();
        System.out.println("Enter the index to show info: ");
        Record r = records.get(Integer.parseInt(scanner.nextLine()) - 1);
        System.out.println(r.toString());
    }

    public void listRecords() {
        for (int i = 0; i < records.size(); ++i)
            System.out.println((i + 1) + ". " + records.get(i).getName());
    }
}
