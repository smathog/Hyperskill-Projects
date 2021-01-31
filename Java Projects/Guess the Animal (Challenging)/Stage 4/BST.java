package animals;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class BST {
    private Node root;

    public BST(Animal firstAnimal) {
        AnimalNode an = new AnimalNode(firstAnimal);
        root = an;
    }

    public BST() {
    }

    private ObjectMapper getObjectMapperForType(FileType fileType) {
        switch (fileType) {
            case JSON:
                return new JsonMapper();
            case XML:
                return new XmlMapper();
            case YAML:
                return new YAMLMapper();
            default:
                throw new IllegalArgumentException("Shouldn't be reached.");
        }
    }

    public void saveTree(String fileName, FileType fileType) {
        try {
            ObjectMapper objectMapper = getObjectMapperForType(fileType);
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (Exception e) {
            System.out.println("ERROR WRITING BST TO JSON WITH FILENAME " + fileName);
            System.out.println(e.getMessage());
        }
    }

    public void loadTree(String fileName, FileType fileType) throws IOException {
        ObjectMapper objectMapper = getObjectMapperForType(fileType);
        root = objectMapper.readValue(new File(fileName), Node.class);
    }

    public Node getRoot() {
        return root;
    }


    public QuestionNode insertQuestion(AnimalNode questionAnimal, String question, Animal secondAnimal, boolean yes) {
        QuestionNode questionNode = new QuestionNode(question);
        AnimalNode secondAnimalNode = new AnimalNode(secondAnimal);
        secondAnimalNode.setParent(questionNode);
        if (yes) {
            questionNode.setYesNode(questionAnimal);
            questionNode.setNoNode(secondAnimalNode);
        } else {
            questionNode.setYesNode(secondAnimalNode);
            questionNode.setNoNode(questionAnimal);
        }
        if (root == questionAnimal) {
            root.setParent(questionNode);
            root = questionNode;
        } else {
            QuestionNode parentNode = questionAnimal.getParent();
            root.setParent(questionNode);
            questionNode.setParent(parentNode);
            if (questionAnimal == parentNode.yesNode)
                parentNode.yesNode = questionNode;
            else
                parentNode.noNode = questionNode;
        }
        return questionNode;
    }

    public enum FileType {
        JSON,
        XML,
        YAML
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = QuestionNode.class, name = "QuestionNode"),
            @JsonSubTypes.Type(value = AnimalNode.class, name = "AnimalNode")
    })
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public static abstract class Node {
        @JsonIgnore
        private QuestionNode parent;

        @JsonIgnore
        public QuestionNode getParent() {
            return parent;
        }

        public void setParent(QuestionNode parent) {
            this.parent = parent;
        }

        @JsonIgnore
        public boolean isQuestion() {
            return false;
        }

        @JsonIgnore
        public boolean isAnimal() {
            return false;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuestionNode extends Node {
        private Node yesNode;
        private Node noNode;
        private String value;

        public QuestionNode() {}

        public QuestionNode(String value) {
            this.value = value;
            yesNode = null;
            noNode = null;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean isQuestion() {
            return true;
        }

        public Node getYesNode() {
            return yesNode;
        }

        public void setYesNode(Node yesNode) {
            this.yesNode = yesNode;
        }

        public Node getNoNode() {
            return noNode;
        }

        public void setNoNode(Node noNode) {
            this.noNode = noNode;
        }
    }

    public static class AnimalNode extends Node {
        private Animal value;
        private String tag = "I'M AN ANIMAL HURR HURR";

        public AnimalNode(Animal animal) {
            this.value = animal;
        }

        public AnimalNode() {}

        public Animal getValue() {
            return value;
        }

        public void setAnimal(Animal animal) {
            this.value = animal;
        }

        public String getTag() {
            return tag;
        }

        @JsonIgnore
        @Override
        public boolean isAnimal() {
            return true;
        }
    }
}
