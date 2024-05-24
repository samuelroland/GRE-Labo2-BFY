package gre.lab2.groupK;

import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraph.Edge;

public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
    static final int SENTINEL = -1; // sentinel value in the queue
    static final int VIEWED = -2; // to put in preds when the vertex is found

    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {

        int nVertices = graph.getNVertices();
        int[] distsTo = new int[nVertices]; // distance from the source to vertex i
        int[] preds = new int[nVertices]; // predecessor of vertex i
        boolean[] contains = new boolean[nVertices];

        Arrays.fill(distsTo, Integer.MAX_VALUE);
        Arrays.fill(preds, -1);
        distsTo[from] = 0;
        int iterationCount = 1; // iteration number (from 1 to nVertices for nVertices - 1 iterations)

        // TODO: check container and default capacity
        ArrayDeque<Integer> verticesQueue = new ArrayDeque<>(); // queue of vertices number to look at at the next
                                                                // iteration
        // Add the source and sentinelle in the queue for the first iteration
        verticesQueue.addFirst(from);
        verticesQueue.addFirst(SENTINEL);
        contains[from] = true;
        boolean lastIteration = false;
        while (!verticesQueue.isEmpty()) {
            int first = verticesQueue.removeLast(); // TODO: or .remove() ?
            if (first == SENTINEL) {
                if (!verticesQueue.isEmpty()) {

                    if (iterationCount++ == nVertices) { // TODO: iter++ or ++iter ?
                        lastIteration = true;
                    } else {
                        verticesQueue.addFirst(SENTINEL);
                    }
                }
            } else {
                contains[first] = false;
                for (Edge outgoingEdge : graph.getOutgoingEdges(first)) {
                    int newDistance = distsTo[first] + outgoingEdge.weight();
                    int to = outgoingEdge.to();
                    if (distsTo[to] > newDistance) {
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
                            // TODO: implement boolean array to O(N) (M is number of discovery vertices)
                            // TODO: ask assistant if okay to put time complexity before spatial complexity
                            // TODO: add weight
                            while (circuitVertices.getLast() != pred) {
                                circuitVertices.removeLast();
                            }

                            int weight = 0;
                            for (int vertex : circuitVertices) {
                                for (Edge outEdge : graph.getOutgoingEdges(vertex)) {
                                    if (outEdge.to() == distsTo[vertex]) {
                                        weight += outEdge.weight();
                                    }
                                }
                            }

                            return new BFYResult.NegativeCycle(circuitVertices, weight);

                        }

                        if (!contains[to]) {
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
