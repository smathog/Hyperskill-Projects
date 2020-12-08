package recognition;

import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    //Note: edges_i corresponds to a connection from nodes_i to outputNode
    //edges_(inputNodes.size()) refers to a connection from biasNode to outputNode
    private ArrayList<Node> inputNodes;
    private ArrayList<Edge> edges;
    private Node biasNode;
    private Node outputNode;

    public Perceptron(int numInputNodes) {
        edges  = new ArrayList<>();
        inputNodes = new ArrayList<>();
        for (int i = 0; i < numInputNodes; ++i)
            inputNodes.add(new Node(0));
        biasNode = new Node(1);
        outputNode = new Node(0);
        for (int i = 0; i < numInputNodes + 1; ++i) {
            Edge temp;
            if (i == numInputNodes)
                temp = new Edge(0, biasNode, outputNode);
            else
                temp = new Edge(0, inputNodes.get(i), outputNode);
            edges.add(temp);
            temp.getFrom().addEdgeOut(temp);
            temp.getTo().addEdgeIn(temp);
        }
    }

    public void compute() {
        outputNode.computeValue();
    }

    //returns the output from the perceptron as-is
    public int getOutput() {
        return outputNode.getValue();
    }

    //Loads the input nodes
    //with the given list of integer values
    public void loadNodes(List<Integer> nodeValues) {
        if (nodeValues == null || nodeValues.size() != inputNodes.size())
            throw new IllegalArgumentException("Bad argument to loadNodes");
        for (int i = 0; i < nodeValues.size(); ++i)
            inputNodes.get(i).setValue(nodeValues.get(i));
    }

    //Loads the edge weights
    //with new values from the given list of integer values
    public void loadWeights(List<Integer> edgeWeights) {
        if (edgeWeights == null || edgeWeights.size() != edges.size())
            throw new IllegalArgumentException("Bad argument to edgeWeights");
        for (int i = 0; i < edgeWeights.size(); ++i)
            edges.get(i).setWeight(edgeWeights.get(i));
    }
}
