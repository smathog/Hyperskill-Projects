package recognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node implements Serializable {
    private double value;
    private ArrayList<Edge> edgesIn;
    private ArrayList<Edge> edgesOut;
    public static final long serialVersionUID = 1L;

    public Node(double value) {
        this.value = value;
        this.edgesIn = new ArrayList<>();
        this.edgesOut = new ArrayList<>();
    }

    public double getValue() {
        return value;
    }

    public void computeValue() {
        value = 1 / (1 + Math.exp(-edgesIn.stream()
                .mapToDouble(e -> e.getWeight() * e.getFrom().getValue())
                .sum()));
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addEdgeIn(Edge in) {
        edgesIn.add(in);
    }

    public void addEdgeOut(Edge out) {
        edgesOut.add(out);
    }

    //Returns a list of nodes this node has an outgoing edge to
    public List<Node> connectedNodes() {
        return edgesOut.stream().map(Edge::getTo).collect(Collectors.toList());
    }

    public List<Edge> edgesIn() { return edgesIn;}

    public List<Edge> edgesOut() { return edgesOut;}
}
