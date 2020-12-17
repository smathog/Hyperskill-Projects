package contacts;

import java.util.Scanner;

public class OrganizationRecord extends Record {
    private String name;
    private String address;

    public OrganizationRecord(Scanner scanner) {
        setName(scanner);
        setAddress(scanner);
        System.out.print("Enter the number: ");
        buildRecord(scanner.nextLine());
    }

    @Override
    public void editRecord(Scanner scanner) {
        System.out.print("Select a field (name, address, number): ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "name":
                setName(scanner);
                break;
            case "address":
                setAddress(scanner);
                break;
            case "number":
                System.out.print("Enter the number: ");
                setPhoneNumber(scanner.nextLine());
                break;
            default:
                break;
        }
        setTimeLastEdited();
    }

    private void setName(Scanner scanner) {
        System.out.print("Enter the organization name: ");
        name = scanner.nextLine();
    }

    private void setAddress(Scanner scanner) {
        System.out.println("Enter the address: ");
        address = scanner.nextLine();
    }

    @Override
    public String toString() {
        return "Organization name: " + name
                + "\nAddress: " + address
                + "\n" + super.toString();
    }

    @Override
    public String getName() {
        return name;
    }
}
