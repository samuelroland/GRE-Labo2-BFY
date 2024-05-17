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
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {
        int SENTINEL = -1; // sentinel value in the queue
        int nVertices = graph.getNVertices();
        int[] distsTo = new int[nVertices]; // distance from the source to vertex i
        int[] preds = new int[nVertices]; // predecessor of vertex i

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
        boolean lastIteration = false;
        while (!verticesQueue.isEmpty()) {
            int first = verticesQueue.removeLast(); // TODO: or .remove() ?
            if (first == SENTINEL) {
                if (!verticesQueue.isEmpty()) {

                    if (iterationCount++ == nVertices) {
                        lastIteration = true;
                    } else {
                        verticesQueue.addFirst(SENTINEL);
                    }
                }
            } else {
                for (Edge outgoingEdge : graph.getOutgoingEdges(first)) {
                    int newDistance = distsTo[first] + outgoingEdge.weight();
                    int to = outgoingEdge.to();
                    if (distsTo[to] > newDistance) {
                        distsTo[to] = newDistance;
                        preds[to] = first;

                        // If the last iteration brought some amelioration, we have to look for
                        // the absorbent circuit
                        if (lastIteration) {
                            // List<Integer> stack = new LinkedList<Integer>();
                            // stack(first);
                            // int i = first;
                            // while (!stack.contains(preds[i])) {
                            // stack.push(preds[i]);
                            // i = preds[i];
                            // }
                            // TODO: implement boolean array to O(M) (M is number of discovery vertices)
                            //TODO: ask assistant if okay to put time complexity before spatial complexity
                            // while(stack.)
                            // List<Integer> circuitVertices =
                            // return new BFYResult.NegativeCycle();

                        }

                        if (!verticesQueue.contains(to))
                            verticesQueue.addFirst(to);
                    } else if (lastIteration) {
                        return new BFYResult.ShortestPathTree(distsTo, preds);
                    }
                }
            }
        }

        return new BFYResult.ShortestPathTree(distsTo, preds);
    }
}
