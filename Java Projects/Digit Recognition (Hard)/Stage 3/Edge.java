package recognition;

import java.io.Serializable;

public class Edge implements Serializable {
    private double weight;
    private Node from;
    private Node to;
    public static final long serialVersionUID = 1L;

    public Edge(double weight, Node from, Node to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }
}
