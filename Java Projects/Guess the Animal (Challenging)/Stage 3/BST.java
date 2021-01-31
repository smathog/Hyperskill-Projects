package animals;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BST {
    private final ArrayList<AnimalNode> animalsInTree;
    private final Random random;
    private Node root;

    public BST(Animal firstAnimal) {
        AnimalNode an = new AnimalNode(firstAnimal);
        root = an;
        random = new Random();
        animalsInTree = Stream.of(an).collect(Collectors.toCollection(ArrayList::new));
    }

    public Node getRoot() {
        return root;
    }

    public AnimalNode getRandomAnimalNode() {
        return animalsInTree.get(random.nextInt(animalsInTree.size()));
    }

    public QuestionNode insertQuestion(AnimalNode questionAnimal, String question, Animal secondAnimal, boolean yes) {
        QuestionNode questionNode = new QuestionNode(question);
        AnimalNode secondAnimalNode = new AnimalNode(secondAnimal);
        animalsInTree.add(secondAnimalNode);
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


    public static abstract class Node {
        private QuestionNode parent;

        public QuestionNode getParent() {
            return parent;
        }

        public void setParent(QuestionNode parent) {
            this.parent = parent;
        }

        public boolean isQuestion() {
            return false;
        }

        public boolean isAnimal() {
            return false;
        }
    }

    public static class QuestionNode extends Node {
        private Node yesNode;
        private Node noNode;
        private final String value;

        public QuestionNode(String value) {
            this.value = value;
            yesNode = null;
            noNode = null;
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
        private final Animal value;

        public AnimalNode(Animal value) {
            this.value = value;
        }

        public Animal getValue() {
            return value;
        }

        @Override
        public boolean isAnimal() {
            return true;
        }
    }
}
