/*
 * Authors: Samuel Roland, Simon Menicot
 */
package gre.lab2.groupK;

import gre.lab2.graph.*;

import java.io.IOException;

public final class Main {
    static final int DEFAULT_SOURCE = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Labo 2 - GRE");
        for (int i = 1; i <= 4; i++) {
            System.out.println("Calcul des arborescences de plus court chemin dans le réseau " + i + ".");

            WeightedDigraph graph = WeightedDigraphReader.fromFile("data/reseau" + i + ".txt");

            IBellmanFordYensAlgorithm algo = new BellmanFordYensAlgorithm();

            var result = algo.compute(graph, DEFAULT_SOURCE);

            if (result.isNegativeCycle() || graph.getNVertices() < 25)
                System.out.println(result + "\n");
            else
                System.out.println("Le réseau " + i + " possède une arborescence de plus court chemin.\n");

        }
        // TODO
        // - Renommage du package ;
        // - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
        // - Documentation soignée comprenant :
        // - la javadoc, avec auteurs et description des implémentations ;
        // - des commentaires sur les différentes parties de vos algorithmes.
    }
}
