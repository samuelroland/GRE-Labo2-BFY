/*
 * Authors: Samuel Roland, Simon Menicot
 */
package gre.lab2.groupK;

import gre.lab2.graph.*;

import java.io.IOException;

public final class Main {
    static final int DEFAULT_SOURCE = 0; // default source vertex for the algorithm
    static final int FILE_COUNT = 4; // number of files to work on

    /**
     * Read the files representing weighted directed graphs
     * and compute the Bellman-Ford-Yens's algorithm on them
     *
     * @param args No argument is needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Labo 2 - GRE");
        for (int i = 1; i <= FILE_COUNT; i++) {
            System.out.println("Calcul des arborescences de plus court chemin dans le réseau " + i + ".");

            WeightedDigraph graph = WeightedDigraphReader.fromFile("data/reseau" + i + ".txt");

            IBellmanFordYensAlgorithm algo = new BellmanFordYensAlgorithm();

            var result = algo.compute(graph, DEFAULT_SOURCE);

            // write the result of the algorithm if we found a negative circle or we don't
            // have too much vertices
            if (result.isNegativeCycle() || graph.getNVertices() < 25)
                System.out.println(result + "\n");
            else
                System.out.println("Le réseau " + i + " possède une arborescence de plus court chemin.\n"); // default
                                                                                                            // message

        }
    }
}
