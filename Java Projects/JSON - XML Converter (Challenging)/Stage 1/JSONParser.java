package converter;

import java.util.ArrayList;
import java.util.regex.*;

public class JSONParser {
    private static final Pattern JSONPattern = Pattern.compile("\\{\"(.*?)\":(\"(.*)\"|(.*))}");

    public static ArrayList<String> parseJSON(String s) {
        ArrayList<String> groupList = null;
        Matcher m = JSONPattern.matcher(s);
        if (m.matches()) {
            groupList = new ArrayList<>();
            groupList.add(m.group(1)); //element
            if (m.group(3) == null) //content
                groupList.add(m.group(4));
            else
                groupList.add(m.group(3));
        }
        return groupList;
    }
}
