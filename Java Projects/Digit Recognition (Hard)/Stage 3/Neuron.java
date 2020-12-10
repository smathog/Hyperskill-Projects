package recognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Neuron implements Serializable {
    //Note: edge_i_j refers to a connection to the jth output node
    // from the ith input node (or in the n+1 case, from the bias node)
    private ArrayList<Node> inputNodes;
    private ArrayList<ArrayList<Edge>> edges;
    private Node biasNode;
    private ArrayList<Node> outputNodes;
    public static final long serialVersionUID = 1L;

    public Neuron(int numInputNodes, int numOutputNodes) {
        Random r = new Random();
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
                double edgeValue = r.nextGaussian();
                if (i == numInputNodes)
                    temp = new Edge(edgeValue, biasNode, outputNodes.get(j));
                else
                    temp = new Edge(edgeValue, inputNodes.get(i), outputNodes.get(j));
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
    public double getOutput(int nodeNum) {
        return outputNodes.get(nodeNum).getValue();
    }

    //Loads the input nodes
    //with the given list of integer values
    public void loadInputNodes(List<Integer> nodeValues) {
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

    public void trainNeuron(List<List<Integer>> inputLoad, List<List<Integer>> expectedOutput) {
        final double learningCoefficient = 0.5d;
        double averageDelta;
        ArrayList<ArrayList<Double>> deltas = edges.stream()
                .map(a -> new ArrayList<>(Collections.nCopies(a.size(), 0d)))
                .collect(Collectors.toCollection(ArrayList::new));
        do {
            averageDelta = 0;
            for (int i = 0; i < deltas.size(); ++i)
                for (int j = 0; j < deltas.get(0).size(); ++j)
                    deltas.get(i).set(j, 0d);

            for (int x = 0; x < inputLoad.size(); ++x) {
                loadInputNodes(inputLoad.get(x));
                compute();
                for (int i = 0; i < outputNodes.size(); ++i) {
                    Node n_i = outputNodes.get(i);
                    for (int j = 0; j < n_i.edgesIn().size(); ++j) {
                        Edge e_j_i = n_i.edgesIn().get(j);
                        double delta = learningCoefficient
                                * e_j_i.getFrom().getValue()
                                * (expectedOutput.get(x).get(i) - n_i.getValue());
                        deltas.get(j).set(i, deltas.get(j).get(i) + delta);
                    }
                }
            }
            //Get averages and update weights
            for (int i = 0; i < deltas.size(); ++i)
                for (int j = 0; j < deltas.get(0).size(); ++j) {
                    double deltaAvg = deltas.get(i).get(j) / inputLoad.size();
                    Edge e = edges.get(i).get(j);
                    e.setWeight(e.getWeight() + deltaAvg);
                    averageDelta += deltaAvg;
                }
            averageDelta /= deltas.size() * deltas.get(0).size();
        } while (Math.abs(averageDelta) > 0.000001);
    }
}
