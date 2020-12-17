package contacts;

import javax.print.event.PrintJobAttributeEvent;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Contacts {
    private ArrayList<Record> records;
    private Scanner scanner;
    private String serializationFile;

    public Contacts(Scanner scanner, String serializationFile) {
        if (serializationFile != null)
            this.serializationFile = serializationFile;
        else
            this.serializationFile = "Contacts.db";
        this.scanner = scanner;
        try {
            deserializeRecords();
        } catch (IOException | ClassNotFoundException e) {
            //Nothing to deserialize, so just create an empty list
            records = new ArrayList<>();
        }
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
        try {
            serializeRecords();
        } catch (IOException e) {
            System.out.println("Error serializing record!");
        }
    }

    public void countRecords() {
        System.out.println("The Phone Book has " + records.size() + " records.");
    }

    public void searchRecords() {
        System.out.print("Enter search query: ");
        final String query = scanner.nextLine();
        var resultList = records.stream()
                .filter(r -> Pattern.compile(query, Pattern.CASE_INSENSITIVE).matcher(r.toString()).find())
                .collect(Collectors.toList());
        System.out.println("Found " + resultList.size() + " results");
        for (int i = 0; i < resultList.size(); ++i) {
            System.out.println((i + 1) + ". " + resultList.get(i).getName());
        }
        searchLoop:
        while (true) {
            System.out.println();
            System.out.print("[search] Enter action ([number], back, again): ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "back":
                    break searchLoop;
                case "again:": {
                    System.out.print("Enter search query: ");
                    final String query2 = scanner.nextLine();
                    resultList = records.stream()
                            .filter(r -> Pattern.compile(query2, Pattern.CASE_INSENSITIVE).matcher(r.toString()).find())
                            .collect(Collectors.toList());
                    System.out.println("Found " + resultList.size() + " results");
                    for (int i = 0; i < resultList.size(); ++i) {
                        System.out.println((i + 1) + ". " + resultList.get(i).getName());
                    }
                }
                    break;
                default: {
                    int num = Integer.parseInt(choice);
                    interactWithRecord(resultList.get(num - 1));
                    break searchLoop;
                }
            }
        }
    }

    private void interactWithRecord(Record r) {
        recordLoop:
        while (true) {
            System.out.println(r.toString());
            System.out.println();
            System.out.print("[record] Enter action (edit, delete, menu): ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "edit":
                    r.editRecord(scanner);
                    try {
                        serializeRecords();
                        System.out.println("Saved");
                    } catch (IOException e) {
                        System.out.println("Error serializing record!");
                    }
                    break;
                case "delete":
                    records.remove(r);
                    try {
                        serializeRecords();
                        System.out.println("Saved");
                    } catch (IOException e) {
                        System.out.println("Error serializing record!");
                    }
                    break;
                case "menu":
                    break recordLoop;
            }
        }
    }

    public void listRecords() {
        for (int i = 0; i < records.size(); ++i)
            System.out.println((i + 1) + ". " + records.get(i).getName());
        System.out.println();
        System.out.print("[list] Enter action ([number], back): ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "back":
                return;
            default:
                interactWithRecord(records.get(Integer.parseInt(choice) - 1));
        }
    }

    private void serializeRecords() throws IOException {
        /*
        FileOutputStream fos = new FileOutputStream(serializationFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(records);
        oos.close();

         */
    }

    private void deserializeRecords() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(serializationFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        records = (ArrayList<Record>) ois.readObject();
        ois.close();
    }
}
