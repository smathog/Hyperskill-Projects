package contacts;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Contacts contacts = new Contacts(new Scanner(System.in));
        contacts.addRecord();
        System.out.println("A Phone Book with a single record created!");
    }
}
