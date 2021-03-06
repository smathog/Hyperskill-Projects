package recognition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class Neuron implements Serializable {
    //Note: edge_x_i_j refers to a connection to the jth output node
    // from the ith input node (or in the n+1 case, from the bias node)
    // where x is the column/layer of the input node
    private ArrayList<ArrayList<ArrayList<Edge>>> edges;
    private ArrayList<ArrayList<Node>> nodes;
    public static final long serialVersionUID = 1L;

    public Neuron(int... layers) {
        Random r = new Random();
        edges  = new ArrayList<>();
        nodes = new ArrayList<>();
        //Generate nodes
        for (int i = 0; i < layers.length - 1; ++i) {
            ArrayList<Node> layer_i = new ArrayList<>();
            for (int j = 0; j < layers[i] + 1; ++j) { //+1 for bias node
                if (j == layers[i])
                    layer_i.add(new Node(1)); //new bias node
                else
                    layer_i.add(new Node(0));
            }
            nodes.add(layer_i);
        }
        //add output node layer
        nodes.add(IntStream.range(0, layers[layers.length - 1]).mapToObj(i -> new Node(0)).collect(Collectors.toCollection(ArrayList::new)));

        //Generate edges
        for (int i = 0; i < nodes.size() - 1; ++i) {
            ArrayList<ArrayList<Edge>> edges_i = new ArrayList<>();
            for (int j = 0; j < nodes.get(i).size(); ++j) {
                ArrayList<Edge> edgesOutLayeriNodej = new ArrayList<>();
                for (int k = 0; k < nodes.get(i + 1).size(); ++k) { //-1 to account for bias node
                    if (i != nodes.size() - 2 && k == nodes.get(i + 1).size() - 1)
                        break;
                    Edge temp = new Edge(r.nextGaussian(), nodes.get(i).get(j), nodes.get(i + 1).get(k));
                    edgesOutLayeriNodej.add(temp);
                    temp.getFrom().addEdgeOut(temp);
                    temp.getTo().addEdgeIn(temp);
                }
                edges_i.add(edgesOutLayeriNodej);
            }
            edges.add(edges_i);
        }
    }

    public void compute() {
        for (int i = 1; i < nodes.size(); ++i)
            for (Node n : nodes.get(i))
                n.computeValue();
    }

    //returns the output value for a given output node
    public double getOutput(int nodeNum) {
        return nodes.get(nodes.size() - 1).get(nodeNum).getValue();
    }

    //Loads the input nodes
    //with the given list of integer values
    public void loadInputNodes(List<Integer> nodeValues) {
        if (nodeValues == null || nodeValues.size() != nodes.get(0).size() - 1)
            throw new IllegalArgumentException("Bad argument to loadNodes");
        for (int i = 0; i < nodeValues.size(); ++i)
            nodes.get(0).get(i).setValue(nodeValues.get(i));
    }


    public void trainNeuron(List<List<Integer>> inputLoad, List<List<Integer>> expectedOutput) {
        int generation = 0;
        double magnitude = 0;
        double error = 0;

        /*
        System.out.println("Before training: ");
        for (int n = 0; n < inputLoad.size(); ++n) {
            loadInputNodes(inputLoad.get(n));
            compute();
            System.out.println("Input: " + n + " Output: " + IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(this::getOutput)).get());
        }
         */

        do {
            //empty list to hold gradient
            ArrayList<ArrayList<ArrayList<Double>>> gradient = edges.stream()
                    .map(a -> a.stream()
                            .map(b -> new ArrayList<Double>(Collections.nCopies(b.size(), 0d)))
                            .collect(Collectors.toCollection(ArrayList::new)))
                    .collect(Collectors.toCollection(ArrayList::new));

            //Iterating over each sample
            for (int n = 0; n < inputLoad.size(); ++n) {
                //First: forward compute
                loadInputNodes(inputLoad.get(n));
                compute();
                final int nCopy = n;
                error += 0.5 * IntStream.range(0, nodes.get(nodes.size() - 1).size())
                        .mapToDouble(i -> Math.pow(nodes.get(nodes.size() - 1).get(i).getValue() - expectedOutput.get(nCopy).get(i), 2))
                        .sum();

                //Next: calculate the derivatives of the cost function for this sample
                //with respect to the node values
                ArrayList<ArrayList<Double>> nodeDerivatives = IntStream.range(0, nodes.size())
                        .mapToObj(i -> {
                            if (i == nodes.size() - 1)
                                return new ArrayList<>(Collections.nCopies(nodes.get(i).size(), 0d));
                            else //exclude the bias nodes
                                return new ArrayList<>(Collections.nCopies(nodes.get(i).size() - 1, 0d));
                        })
                        .collect(Collectors.toCollection(ArrayList::new));
                //load the output node derivatives into the final list
                for (int i = 0; i < nodes.get(nodes.size() - 1).size(); ++i)
                    nodeDerivatives.get(nodes.size() - 1).set(i, nodes.get(nodes.size() - 1).get(i).getValue() - expectedOutput.get(n).get(i));
                //using the final list, work backwards to calculate
                for (int i = nodeDerivatives.size() - 2; i >= 1; --i) {
                    var layer = nodeDerivatives.get(i);
                    for (int j = 0; j < layer.size(); ++j) {
                        final var iCopy = i;
                        final var jCopy = j;
                        double derivative = IntStream.range(0, nodes.get(i).get(j).edgesOut().size())
                                .mapToDouble(k -> {
                                    Edge current = nodes.get(iCopy).get(jCopy).edgesOut().get(k);
                                    return current.getWeight()
                                            * sigmoidDerivative(current.getTo().getValue())
                                            * nodeDerivatives.get(iCopy + 1).get(k);
                                })
                                .sum();
                        layer.set(j, derivative);
                    }
                }

                //Next: using the node derivatives, calculate the edge weight derivatives for this sample
                ArrayList<ArrayList<ArrayList<Double>>> edgeDerivatives = edges.stream()
                        .map(a -> a.stream()
                                .map(b -> new ArrayList<Double>(Collections.nCopies(b.size(), 0d)))
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .collect(Collectors.toCollection(ArrayList::new));
                for (int layer = edgeDerivatives.size() - 1; layer >= 0; --layer) {
                    //Start with bias weights
                    var layerEdges = edgeDerivatives.get(layer);
                    for (int i = layerEdges.size() - 1; i >= 0; --i) {
                        var edgesFromNodei = layerEdges.get(i);
                        for (int j = 0; j < edgesFromNodei.size(); ++j) {
                            if (i == layerEdges.size() - 1) //bias node out edge
                                edgesFromNodei.set(j, nodeDerivatives.get(layer + 1).get(j) * sigmoidDerivative(nodes.get(layer + 1).get(j).getValue()));
                            else
                                edgesFromNodei.set(j, layerEdges.get(layerEdges.size() - 1).get(j) * nodes.get(layer).get(i).getValue());
                        }
                    }
                }

                //Add edgeDerivatives into gradient
                for (int i = 0; i < edgeDerivatives.size(); ++i)
                    for (int j = 0; j < edgeDerivatives.get(i).size(); ++j)
                        for (int k = 0; k < edgeDerivatives.get(i).get(j).size(); ++k)
                            gradient.get(i).get(j).set(k, gradient.get(i).get(j).get(k) + edgeDerivatives.get(i).get(j).get(k));
            }

            //Divide gradient entries by number of samples to get actual gradient
            for (int i = 0; i < gradient.size(); ++i)
                for (int j = 0; j < gradient.get(i).size(); ++j)
                    for (int k = 0; k < gradient.get(i).get(j).size(); ++k)
                        gradient.get(i).get(j).set(k, gradient.get(i).get(j).get(k) / inputLoad.size());

            //update weights by subtracting the gradient value
            for (int i = 0; i < edges.size(); ++i)
                for (int j = 0; j < edges.get(i).size(); ++j)
                    for (int k = 0; k < edges.get(i).get(j).size(); ++k) {
                        Edge current = edges.get(i).get(j).get(k);
                        current.setWeight(current.getWeight() - gradient.get(i).get(j).get(k));
                    }

            ++generation;
            magnitude = Math.sqrt(gradient.stream()
                    .flatMap(a -> a.stream().flatMap(b -> b.stream()))
                    .mapToDouble(d -> d * d)
                    .sum());
            error /= inputLoad.size();

            /*
            if (generation == 10 || generation == 100 || generation == 1000 || generation == 10000 || generation == 100000) {
                System.out.println("Magnitude: " + magnitude);
                System.out.println("Error: " + error);
                System.out.println("Generation: " + generation);
            }

             */
        } while (generation < 10000);
/*
        System.out.println("After training: ");
        for (int n = 0; n < inputLoad.size(); ++n) {
            loadInputNodes(inputLoad.get(n));
            compute();
            System.out.println("Input: " + n + " Output: " + IntStream.range(0, 10).boxed().max(Comparator.comparingDouble(this::getOutput)).get());
        }
*/
    }

    private static double sigmoidDerivative(double sigmoidOutput) {
        return sigmoidOutput * (1 - sigmoidOutput);
    }
}

