package search;

public class Person {
    private String firstName;
    private String lastName;
    private String email;

    public Person(String line) {
        String[] args = line.split(" ");
        firstName = args[0];
        if (args.length >= 2)
            lastName = args[1];
        if (args.length >= 3)
            email = args[2];
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + (email != null ? " " + email : "");
    }
}
