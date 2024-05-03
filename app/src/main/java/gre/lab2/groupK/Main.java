/*
 * Authors: Samuel Roland, Simon Menicot
 */
package gre.lab2.groupK;

import gre.lab2.graph.*;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Labo 2 - GRE");

        final String[] FILE_INDEXES = {"1","2","3","4"};
        for (var i : FILE_INDEXES){
            System.out.println("Calcul des arborescences de plus court chemin dans le réseau "+ i + ".");

            WeightedDigraph graph = WeightedDigraphReader.fromFile("data/reseau" + i + ".txt");
        }
        // TODO
        // - Renommage du package ;
        // - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
        // - Documentation soignée comprenant :
        // - la javadoc, avec auteurs et description des implémentations ;
        // - des commentaires sur les différentes parties de vos algorithmes.
    }
}
