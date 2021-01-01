package converter;

import java.util.regex.*;
import java.util.ArrayList;

public class XMLParser {
    private static final Pattern SINGLE_TAG_PATTERN = Pattern.compile("<(.*?)/>");
    private static final Pattern  PAIRED_TAG_PATTERN = Pattern.compile("<(.*?)>(.*)<\\/.*?>");

    public static ArrayList<String> parseXML(String s) {
        ArrayList<String> groupList = null;
        Matcher single = SINGLE_TAG_PATTERN.matcher(s);
        if (single.matches()) {
            groupList = new ArrayList<>();
            groupList.add(single.group(1)); //tag label
            groupList.add(null);
            return groupList;
        }
        Matcher paired = PAIRED_TAG_PATTERN.matcher(s);
        if (paired.matches()) {
            groupList = new ArrayList<>();
            groupList.add(paired.group(1)); //tag label
            groupList.add(paired.group(2)); //content
        }
        return groupList;
    }
}
