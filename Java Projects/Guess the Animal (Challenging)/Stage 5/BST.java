package animals;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BST {
    private final Comparator<Animal> animalComparator = Comparator.comparing(Animal::getName);

    private Node root;
    private HashSet<Animal> animalSet;
    private TreeSet<Animal> orderedAnimalSet;

    public BST(Animal firstAnimal) {
        AnimalNode an = new AnimalNode(firstAnimal);
        root = an;
        animalSet = new HashSet<>();
        animalSet.add(firstAnimal);
        orderedAnimalSet = new TreeSet<>(animalComparator);
        orderedAnimalSet.add(firstAnimal);
    }

    public BST() {
        root = null;
        animalSet = new HashSet<>();
        orderedAnimalSet = new TreeSet<>(animalComparator);
    }

    public void getStats() {
        System.out.println("The Knowledge Tree stats");
        System.out.println();
        String format = "%-30s %20s %n";
        if (root.isAnimal())
            System.out.printf(format, "- root node", ((AnimalNode) root).getValue());
        else
            System.out.printf(format, "- root node", ((QuestionNode) root).getValue());
        int numNodes = getNumNodes(root);
        System.out.printf(format, "- total number of nodes", numNodes);
        System.out.printf(format, "- total number of animals", animalSet.size());
        System.out.printf(format, "- total number of statements", (numNodes - animalSet.size()));
        System.out.printf(format, "- height of the tree", (getMaxDepth(root) - 1));
        System.out.printf(format, "- minimum depth", (getMinDepth(root) - 1));
        System.out.printf(format, "- average depth", getAverageDepth(root));
    }

    private int getNumNodes(Node start) {
        if (start.isQuestion()) {
            QuestionNode questionNode = (QuestionNode) start;
            return getNumNodes(questionNode.yesNode) + getNumNodes(questionNode.noNode) + 1;
        } else
            return 1;
    }

    private int getMaxDepth(Node start) {
        if (start.isQuestion()) {
            QuestionNode qn = (QuestionNode) start;
            return Math.max(getMaxDepth(qn.yesNode), getMaxDepth(qn.noNode)) + 1;
        } else
            return 1;
    }

    private int getMinDepth(Node start) {
        if (start.isQuestion()) {
            QuestionNode q = (QuestionNode) start;
            return Math.min(getMinDepth(q.yesNode), getMinDepth(q.noNode)) + 1;
        } else
            return 1;
    }

    private double getAverageDepth(Node start) {
        ArrayList<Integer> depths = new ArrayList<>();
        loadDepthList(root, depths, 0);
        return depths.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    private void loadDepthList(Node start, ArrayList<Integer> depths, int currentDepth) {
        depths.add(currentDepth);
        ++currentDepth;
        if (start.isQuestion()) {
            QuestionNode q = (QuestionNode) start;
            loadDepthList(q.yesNode, depths, currentDepth);
            loadDepthList(q.noNode, depths, currentDepth);
        }
    }

    public boolean hasAnimal(Animal animal) {
        return animalSet.contains(animal);
    }

    private boolean listFactsImpl(ArrayList<String> facts, Node current, Animal target) {
        if (current == null)
            return false;
        else if (current.isAnimal()) {
            AnimalNode currentAnimal = (AnimalNode) current;
            return currentAnimal.getValue().equals(target);
        } else {
            QuestionNode currentQuestion = (QuestionNode) current;
            if (listFactsImpl(facts, currentQuestion.yesNode, target)) {
                facts.add(currentQuestion.getPositiveStatement());
                return true;
            } else if (listFactsImpl(facts, currentQuestion.noNode, target)) {
                facts.add(currentQuestion.getNegativeStatement());
                return true;
            } else
                return false;
        }
    }

    public ArrayList<String> listFacts(Animal target) {
        ArrayList<String> facts = new ArrayList<>();
        listFactsImpl(facts, root, target);
        return facts;
    }

    public HashSet<Animal> getAnimalSet() {
        return animalSet;
    }

    public void setAnimalSet(HashSet<Animal> animalSet) {
        this.animalSet = animalSet;
    }

    public TreeSet<Animal> getOrderedAnimalSet() {
        return orderedAnimalSet;
    }

    public void setOrderedAnimalSet(TreeSet<Animal> orderedAnimalSet) {
        this.orderedAnimalSet = orderedAnimalSet;
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
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File("list" + fileName), animalSet.toArray(new Animal[]{}));
        } catch (Exception e) {
            System.out.println("ERROR WRITING BST TO JSON WITH FILENAME " + fileName);
            System.out.println(e.getMessage());
        }
    }

    public void loadTree(String fileName, FileType fileType) throws IOException {
        ObjectMapper objectMapper = getObjectMapperForType(fileType);
        root = objectMapper.readValue(new File(fileName), Node.class);
        Animal[] arr = objectMapper.readValue(new File("list" + fileName), Animal[].class);
        animalSet = Arrays.stream(arr).collect(Collectors.toCollection(HashSet::new));
        orderedAnimalSet = Arrays.stream(arr).collect(Collectors.toCollection(() -> new TreeSet<>(animalComparator)));
    }

    public Node getRoot() {
        return root;
    }

    public QuestionNode insertQuestion(AnimalNode questionAnimal, String question, String positiveStatement, String negativeStatement, Animal secondAnimal, boolean yes) {
        QuestionNode questionNode = new QuestionNode(question, positiveStatement, negativeStatement);
        AnimalNode secondAnimalNode = new AnimalNode(secondAnimal);
        animalSet.add(secondAnimal);
        orderedAnimalSet.add(secondAnimal);
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

    //Print function based on the one found in this article:
    //https://www.baeldung.com/java-print-binary-tree-diagram
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        traverseNode(sb, "", "", root, false);
        return sb.toString();
    }

    private void traverseNode(StringBuilder sb, String padding, String pointer, Node node, boolean nodeAfter) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            if (node.isAnimal())
                sb.append(((AnimalNode) node).getValue());
            else
                sb.append(((QuestionNode) node).getValue());
            sb.append('\n');

            String noPointer = "└──";
            String yesPointer = "├──";

            Consumer<String> nextCall = s -> {
                QuestionNode qNode = (QuestionNode) node;
                traverseNode(sb, s, yesPointer, qNode.yesNode, true);
                traverseNode(sb, s, noPointer, qNode.noNode, false);
            };

            if (node == root) {
                nextCall.accept("");
            } else if (node.isQuestion()) {
                int length = pointer.length() + ((QuestionNode) node).getValue().length();
                StringBuilder pb = new StringBuilder(padding);
                if (nodeAfter)
                    pb.append("|" + " ".repeat(length));
                else
                    pb.append(" " + " ".repeat(length));
                nextCall.accept(pb.toString());
            }
        }
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
        private QuestionNode parent;

        @JsonBackReference
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
        private String positiveStatement;
        private String negativeStatement;

        public QuestionNode() {}

        public QuestionNode(String value, String positiveStatement, String negativeStatement) {
            this.value = value;
            this.positiveStatement = positiveStatement;
            this.negativeStatement = negativeStatement;
            yesNode = null;
            noNode = null;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getPositiveStatement() {
            return positiveStatement;
        }

        public void setPositiveStatement(String positiveStatement) {
            this.positiveStatement = positiveStatement;
        }

        public String getNegativeStatement() {
            return negativeStatement;
        }

        public void setNegativeStatement(String negativeStatement) {
            this.negativeStatement = negativeStatement;
        }

        @Override
        public boolean isQuestion() {
            return true;
        }

        @JsonManagedReference
        public Node getYesNode() {
            return yesNode;
        }

        public void setYesNode(Node yesNode) {
            this.yesNode = yesNode;
        }

        @JsonManagedReference
        public Node getNoNode() {
            return noNode;
        }

        public void setNoNode(Node noNode) {
            this.noNode = noNode;
        }
    }

    public static class AnimalNode extends Node {
        private Animal value;
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

        @Override
        public boolean isAnimal() {
            return true;
        }
    }
}
