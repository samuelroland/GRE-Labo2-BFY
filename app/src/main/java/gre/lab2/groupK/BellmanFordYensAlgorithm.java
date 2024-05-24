package gre.lab2.groupK;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraph.Edge;

/**
 * Implements the Bellman-Ford-Yens's algorithm
 */
public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
    static final int SENTINEL = -1; // sentinel value in the queue
    static final int VIEWED = -2; // to put in preds when the vertex is found
    static final int NO_PRED = -1; // in the predecessor list, when there is no predecessor

    /**
     * Apply the algorithm to a weighted directed graph
     * @param graph the graph on witch to found the shortest paths
     * @param from the vertex of the graph from which the shortest paths have to be found
     * @return the result of the algorithm, see {@link BFYResult}
     */
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {

        int nVertices = graph.getNVertices();
        int[] distsTo = new int[nVertices]; // distance from the source to vertex i
        int[] preds = new int[nVertices]; // predecessor of vertex i

        Arrays.fill(distsTo, Integer.MAX_VALUE); // to simulate infinity
        Arrays.fill(preds, NO_PRED);
        distsTo[from] = 0;
        int iterationCount = 1; // iteration number (from 1 to nVertices for nVertices - 1 iterations)

        // TODO: check container and default capacity
        ArrayDeque<Integer> verticesQueue = new ArrayDeque<>(); // queue of vertices number to look at at the next
        // iteration
        boolean[] contains = new boolean[nVertices]; // to see in O(1) if a vertex is in verticesQueue
        // Add the source and sentinelle in the queue for the first iteration
        verticesQueue.addFirst(from);
        verticesQueue.addFirst(SENTINEL);
        contains[from] = true;
        boolean lastIteration = false; // true if it is the nth iteration
        while (!verticesQueue.isEmpty()) {
            int first = verticesQueue.removeLast(); // first is the current vertex
            if (first == SENTINEL) { // if we are at the end of an iteration
                if (!verticesQueue.isEmpty()) {

                    if (iterationCount++ == nVertices) { // if we're starting the nth iteration
                        lastIteration = true;
                    } else {
                        verticesQueue.addFirst(SENTINEL);
                    }
                }
            } else {
                contains[first] = false;
                for (Edge outgoingEdge : graph.getOutgoingEdges(first)) { // for each outgoing edge of the current vetex
                    int newDistance = distsTo[first] + outgoingEdge.weight();
                    int to = outgoingEdge.to();
                    if (distsTo[to] > newDistance) { // if there is an improvement
                        distsTo[to] = newDistance;
                        preds[to] = first;

                        // If the last iteration brought some amelioration, we have to look for
                        // the absorbent circuit
                        if (lastIteration) {
                            // we now use preds to look for the absorbant circuit
                            List<Integer> circuitVertices = new LinkedList<>();
                            int pred = preds[first]; // we need to store preds[i] as it is overwritten
                            int i = first;
                            while (preds[pred] != VIEWED) {
                                circuitVertices.addFirst(pred);
                                preds[i] = VIEWED;
                                i = pred;
                                pred = preds[i];
                                distsTo[pred] = i; // we use distsTo to store the successors of the vertexes found
                            }

                            while (circuitVertices.getLast() != pred) {
                                circuitVertices.removeLast();
                            }

                            int weight = 0;
                            for (int vertex : circuitVertices) { // compute the weight of the absorbant circuit
                                for (Edge outEdge : graph.getOutgoingEdges(vertex)) {
                                    if (outEdge.to() == distsTo[vertex]) {
                                        weight += outEdge.weight();
                                    }
                                }
                            }

                            return new BFYResult.NegativeCycle(circuitVertices, weight);

                        }

                        if (!contains[to]) { // add the new vertex in verticesQueue if it is not
                            contains[to] = true;
                            verticesQueue.addFirst(to);
                        }
                    }
                }
            }
        }

        return new BFYResult.ShortestPathTree(distsTo, preds);
    }
}
