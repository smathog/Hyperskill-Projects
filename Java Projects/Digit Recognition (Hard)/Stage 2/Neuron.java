package recognition;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
    //Note: edge_i_j refers to a connection to the jth output node
    // from the ith input node (or in the n+1 case, from the bias node)
    private ArrayList<Node> inputNodes;
    private ArrayList<ArrayList<Edge>> edges;
    private Node biasNode;
    private ArrayList<Node> outputNodes;

    public Neuron(int numInputNodes, int numOutputNodes) {
        edges  = new ArrayList<>();
        inputNodes = new ArrayList<>();
        outputNodes = new ArrayList<>();
        for (int i = 0; i < numInputNodes; ++i)
            inputNodes.add(new Node(0));
        for (int i = 0; i < numOutputNodes; ++i)
            outputNodes.add(new Node(0));
        biasNode = new Node(1);
        for (int i = 0; i < numInputNodes + 1; ++i) {
            ArrayList<Edge> nodeiEdgesOut = new ArrayList<>();
            for (int j = 0; j < numOutputNodes; ++j) {
                Edge temp;
                if (i == numInputNodes)
                    temp = new Edge(0, biasNode, outputNodes.get(j));
                else
                    temp = new Edge(0, inputNodes.get(i), outputNodes.get(j));
                nodeiEdgesOut.add(temp);
                temp.getFrom().addEdgeOut(temp);
                temp.getTo().addEdgeIn(temp);
            }
            edges.add(nodeiEdgesOut);
        }
    }

    public void compute() {
        for (var outputNode : outputNodes)
            outputNode.computeValue();
    }

    //returns the output value for a given output node
    public int getOutput(int nodeNum) {
        return outputNodes.get(nodeNum).getValue();
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
    //with new values from the array
    //note: edgeWeights[i][j] corresponds to edges_i_j
    public void loadWeights(int[][] edgeWeights) {
        for (int i = 0; i < edgeWeights.length; ++i)
            for (int j = 0; j < edgeWeights[i].length; ++j)
                edges.get(i).get(j).setWeight(edgeWeights[i][j]);
    }
}
