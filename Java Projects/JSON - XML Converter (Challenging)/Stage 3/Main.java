package converter;

import converter.XMLComponents.XMLAttribute;
import converter.XMLComponents.XMLContent;
import converter.XMLComponents.XMLContentString;
import converter.XMLComponents.XMLTag;
import converter.XMLParser.XMLParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) {
        try {
            var filePath = Paths.get("test.txt");
            String fileContents = Files.readString(filePath);
            XMLTag tag = XMLParser.parseTag(fileContents, 0).getFirst();
            ArrayDeque<XMLTag> tagStack = new ArrayDeque<>();
            ArrayDeque<String> pathStack = new ArrayDeque<>();
            tagStack.push(tag);
            pathStack.push(tag.getElement());
            while (!tagStack.isEmpty()) {
                //Print relevant information
                tag = tagStack.pop();
                String path = pathStack.pop();
                System.out.println("Element: ");
                System.out.println("path = " + path);
                if (tag.hasContent()) {
                    if (tag.getContent().isEmpty())
                        System.out.println("value = \"\"");
                    else if (tag.getContent().size() == 1 && tag.getContent().get(0).isContentString())
                        System.out.println("value = " + tag.getContent().get(0));
                } else
                    System.out.println("value = null");
                if (tag.hasAttributes()) {
                    System.out.println("attributes: ");
                    for (XMLAttribute attr : tag.getAttributes())
                        System.out.println(attr);
                }
                System.out.println();

                //Add children and path information to stacks
                if (tag.hasContent())
                    for (int i = tag.getContent().size() - 1; i >= 0; --i) {
                        XMLContent content = tag.getContent().get(i);
                        if (content.isTag()) {
                            XMLTag asTag = (XMLTag) content;
                            tagStack.push(asTag);
                            pathStack.push(path + ", " + asTag.getElement());
                        }
                    }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
