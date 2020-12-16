package contacts;

import java.util.Scanner;

public class Menu {
    private Contacts contacts;
    private Scanner scanner;

    public Menu() {
        scanner = new Scanner(System.in);
        contacts = new Contacts(scanner);
        menuLoop();
    }

    private void menuLoop() {
        menuLoop:
        while (true) {
            System.out.print("Enter action (add, remove, edit, count, list, exit): ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add":
                    contacts.addRecord();
                    break;
                case "remove":
                    contacts.deleteRecord();
                    break;
                case "edit":
                    contacts.editRecord();
                    break;
                case "count":
                    contacts.countRecords();
                    break;
                case "list":
                    contacts.listRecords();
                    break;
                case "exit":
                    break menuLoop;
                default:
                    break;
            }
        }
    }
}
