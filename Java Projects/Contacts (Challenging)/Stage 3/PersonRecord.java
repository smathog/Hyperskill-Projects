package contacts;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PersonRecord extends Record {
    private String name;
    private String surname;
    private String birthDate;
    private String gender;

    public PersonRecord(Scanner scanner) {
        setName(scanner);
        setSurname(scanner);
        setBirthDate(scanner);
        setGender(scanner);
        System.out.print("Enter the number: ");
        buildRecord(scanner.nextLine());
    }

    @Override
    public void editRecord(Scanner scanner) {
        System.out.print("Select a field (name, surname, birth, gender, number): ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "name":
                setName(scanner);
                break;
            case "surname":
                setSurname(scanner);
                break;
            case "birth":
                setBirthDate(scanner);
                break;
            case "gender":
                setGender(scanner);
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
        System.out.print("Enter the name: ");
        name = scanner.nextLine();
    }

    private void setSurname(Scanner scanner) {
        System.out.print("Enter the surname: ");
        surname = scanner.nextLine();
    }

    private void setBirthDate(Scanner scanner) {
        System.out.print("Enter the birth date: ");
        try {
            birthDate = LocalDate.parse(scanner.nextLine()).toString();
        } catch (DateTimeParseException e) {
            System.out.println("Bad birth date!");
            birthDate = noData;
        }
    }

    private void setGender(Scanner scanner) {
        System.out.print("Enter the gender (M, F): ");
        String choice = scanner.nextLine();
        if (!("M".equals(choice) || "F".equals(choice))) {
            System.out.println("Bad gender!");
            gender = noData;
        } else
            gender = choice;
    }

    @Override
    public String toString() {
        return "Name: " + name
                + "\nSurname: " + surname
                + "\nBirth date: " + birthDate
                + "\nGender: " + gender
                + "\n" + super.toString();
    }

    @Override
    public String getName() {
        return name + " " + surname;
    }
}
