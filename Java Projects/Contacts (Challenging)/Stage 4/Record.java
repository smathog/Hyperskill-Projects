package contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ChronoUnit.*;
import java.util.Scanner;
import java.util.regex.*;

public abstract class Record implements Serializable {
    private static Pattern numberPattern = Pattern.compile("((\\+?[^_\\W]{1,})([ \\-][^_\\W]{2,})*)" +
            "|((\\+?\\([^_\\W]{1,}\\))([ \\-][^_\\W]{2,})*)" +
            "|(\\+?[^_\\W]{1,}[ \\-]\\([^_\\W]{2,}\\)([ \\-][^_\\W]{2,})*)");
    protected static String noData = "[no data]";
    public static long serialVersionUID = 1L;

    private LocalDateTime timeCreated;
    private LocalDateTime timeLastEdited;
    private String phoneNumber;

    public void buildRecord(String phoneNumber) {
        setPhoneNumber(phoneNumber);
        setTimeCreated();
        setTimeLastEdited();
    }

    public abstract void editRecord(Scanner scanner);

    protected void setTimeCreated() {
        timeCreated = LocalDateTime.now();
    }

    protected void setTimeLastEdited() {
        timeLastEdited = LocalDateTime.now();
    }

    protected void setPhoneNumber(String number) {
        Matcher m = numberPattern.matcher(number);
        if (m.matches())
            phoneNumber = number;
        else {
            System.out.println("Wrong number format!");
            phoneNumber = noData;
        }
    }

    @Override
    public String toString() {
        return "Number: "
                + phoneNumber
                + "\nTime created: "
                + timeCreated.truncatedTo(ChronoUnit.MINUTES).toString()
                + "\nTime last edit: "
                + timeLastEdited.truncatedTo(ChronoUnit.MINUTES).toString();
    }

    public abstract String getName();
}
