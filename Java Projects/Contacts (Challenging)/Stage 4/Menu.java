package contacts;

import java.util.Scanner;

public class Menu {
    private Contacts contacts;
    private Scanner scanner;

    public Menu(String fileName) {
        scanner = new Scanner(System.in);
        contacts = new Contacts(scanner, fileName);
        System.out.println();
        menuLoop();
    }

    private void menuLoop() {
        menuLoop:
        while (true) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add":
                    contacts.addRecord();
                    break;
                case "count":
                    contacts.countRecords();
                    break;
                case "search":
                    contacts.searchRecords();
                    break;
                case "list":
                    contacts.listRecords();
                    break;
                case "exit":
                    break menuLoop;
                default:
                    break;
            }
            System.out.println();
        }
    }
}
