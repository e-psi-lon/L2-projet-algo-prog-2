package dev.e_psi_lon;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Graphe graphe = new Graphe("graphe.csv");

        System.out.println("Parcours en profondeur (right) :");
        graphe.parcours(ParcoursType.PROFONDEUR_R);

        System.out.println("Parcours en profondeur (left) :");
        graphe.parcours(ParcoursType.PROFONDEUR_L);

        System.out.println("Parcours en largeur :");
        graphe.parcours(ParcoursType.LARGEUR);

        graphe.export();

        System.out.println("\nGraphe Gilbert G(n,p)");
        Graphe gilbert = new RandomGraphe(15, 0.3);
        gilbert.export();

        System.out.println("\nGraphe Erdos-Renyi G(n,m)");
        Graphe erdos = new RandomGraphe(15, 32);
        erdos.export();

        System.out.println("\nRing Graph");
        Graphe ring = new SmallWorld(10, 2);
        ring.export();

        System.out.println("\nSmall World");
        Graphe small = new SmallWorld(10, 2, 0.2);
        small.export();

        System.out.println("\nScale Free (Barabasi-Albert)");
        Graphe scale = new ScaleFree(25, 1);
        scale.export();

        System.out.println("\nGraphes générés et exportés !");
    }
}