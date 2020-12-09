package recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    private int value;
    private ArrayList<Edge> edgesIn;
    private ArrayList<Edge> edgesOut;

    public Node(int value) {
        this.value = value;
        this.edgesIn = new ArrayList<>();
        this.edgesOut = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public void computeValue() {
        value = edgesIn.stream()
                .mapToInt(e -> e.getWeight() * e.getFrom().getValue())
                .sum();
    }

    public void setValue(int value) {
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
}
