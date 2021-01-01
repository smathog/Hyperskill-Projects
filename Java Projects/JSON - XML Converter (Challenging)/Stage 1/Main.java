package converter;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        ArrayList<String> parts = JSONParser.parseJSON(input);
        if (parts == null) {
            parts = XMLParser.parseXML(input);
            System.out.print("{\"" + parts.get(0) + "\":");
            if (parts.get(1) == null)
                System.out.println(" " + parts.get(1) + " }" );
            else
                System.out.println("\"" + parts.get(1) + "\"}");
        } else {
            if (parts.get(1).equals("null"))
                System.out.println("<" + parts.get(0) + "/>");
            else
                System.out.println("<" + parts.get(0) + ">" + parts.get(1)
                + "</" + parts.get(0) + ">");
        }
    }
}
