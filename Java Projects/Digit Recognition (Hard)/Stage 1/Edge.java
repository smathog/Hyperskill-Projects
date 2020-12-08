package recognition;

import java.util.ArrayList;

public class Edge {
    private int weight;
    private Node from;
    private Node to;

    public Edge(int weight, Node from, Node to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }
}
