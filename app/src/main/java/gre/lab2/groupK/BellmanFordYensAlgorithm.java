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
    static final int SENTINEL = -1; // sentinel value in the queue used to identify end of iterations
    static final int VIEWED = -2; // to put in preds when the vertex is found
    static final int NO_PRED = -1; // used in the predecessor list as the default value to indicate that there is
                                   // (yet) no predecessor

    /**
     * Apply the algorithm to a weighted directed graph
     * 
     * @param graph the graph on which we search the shortest paths
     * @param from  the source vertex for the algorithm
     * @return the result of the algorithm, see {@link BFYResult}
     */
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {

        // Create useful data structures
        int nVertices = graph.getNVertices();
        int[] distsTo = new int[nVertices]; // array of distance from the source to vertex i
        int[] preds = new int[nVertices]; // array of predecessor of vertex i
        ArrayDeque<Integer> verticesQueue = new ArrayDeque<>(); // queue of vertices to consider at the next iteration.
                                                                // We push values at front (addFirst()) and pop values
                                                                // at back (removeLast())
        boolean[] queueContains = new boolean[nVertices]; // to know in O(1) if a vertex is in verticesQueue
        int iterationCount = 1; // iteration number (from 1 to nVertices for nVertices - 1 iterations)
        boolean lastIteration = false; // true if it is the nth iteration

        // Initialize those structures
        Arrays.fill(distsTo, Integer.MAX_VALUE); // fill the distances with MAX_VALUE to simulate infinity
        Arrays.fill(preds, NO_PRED); // define all predecessor as "no predecessor" yet
        distsTo[from] = 0; // the source has a null distance to itself
        // Add the source and sentinelle in the queue for the first iteration
        verticesQueue.addFirst(from);
        verticesQueue.addFirst(SENTINEL);
        queueContains[from] = true; // the source is in the queue as we just put it inside

        // Looping until the queue is empty or we reached N iterations
        while (!verticesQueue.isEmpty()) {
            int cVertex = verticesQueue.removeLast(); // the current vertex popped from the queue

            if (cVertex == SENTINEL) { // if we reached the end of an iteration
                if (!verticesQueue.isEmpty()) {

                    // if we're starting the N th iteration, we remember it and we don't add the
                    // SENTINEL in the queue as we will stop at next cVertex
                    if (iterationCount++ == nVertices) {
                        lastIteration = true;
                    } else {
                        verticesQueue.addFirst(SENTINEL);
                    }
                }
            } else {
                queueContains[cVertex] = false; // mark the vertice as "out of the queue"

                // for each outgoing edge of the current vertex
                for (Edge outgoingEdge : graph.getOutgoingEdges(cVertex)) {
                    int newDistance = distsTo[cVertex] + outgoingEdge.weight(); // potentiel new distance
                    int to = outgoingEdge.to();

                    if (distsTo[to] > newDistance) { // if there is a distance improvement
                        // Update distances and predecessor
                        distsTo[to] = newDistance;
                        preds[to] = cVertex;

                        // If the last iteration brought some amelioration, we have to look for
                        // the absorbent circuit
                        if (lastIteration) {
                            return searchAbsorbentCircuit(graph, preds, cVertex, distsTo);
                        }

                        // add the new vertex in verticesQueue if it is not
                        if (!queueContains[to]) {
                            queueContains[to] = true;
                            verticesQueue.addFirst(to);
                        }
                    }
                }
            }
        }

        return new BFYResult.ShortestPathTree(distsTo, preds);
    }

    private BFYResult.NegativeCycle searchAbsorbentCircuit(WeightedDigraph graph, int preds[], int cVertex,
            int distsTo[]) {
        // Note: We now recycle the preds array to store the special VIEWED value to
        // indicate that we met the vertex when going back in predecessor's path, so we
        // can know when we met the circuit entering vertex

        // Note: Like preds, we now recycle the distsTo array to store the successor, so
        // we can easily find back the correct outgoing edge when doing the sum of
        // weights

        // The final list of vertices in the absorbent circuit
        List<Integer> circuitVertices = new LinkedList<>();

        int pred = preds[cVertex]; // we need to store preds[i] as it will be overwritten after
        int i = cVertex;
        // Going back in predecessor until we find the same vertex twice (that would
        // closing the loop)
        while (preds[pred] != VIEWED) {
            circuitVertices.addFirst(pred);
            preds[i] = VIEWED;
            i = pred;
            pred = preds[i];
            distsTo[pred] = i; // we use distsTo to store the successors of the vertexes found
        }
        // Remove the vertices out of the circuit (the first vertex with improvement is
        // not necessarily inside the circuit)
        while (circuitVertices.getLast() != pred) {
            circuitVertices.removeLast();
        }

        // Afterwards, compute the weight of the absorbent circuit
        int weight = 0;
        for (int vertex : circuitVertices) {
            for (Edge outEdge : graph.getOutgoingEdges(vertex)) {
                if (outEdge.to() == distsTo[vertex]) { // this is where we are reusing distsTo as
                                                       // successor number
                    weight += outEdge.weight();
                }
            }
        }

        return new BFYResult.NegativeCycle(circuitVertices, weight);
    }
}
