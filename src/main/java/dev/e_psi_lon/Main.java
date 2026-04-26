package dev.e_psi_lon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        /*
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

        Noeud a = new Noeud(0, 16.47, 96.1);
        Noeud b = new Noeud(1, 16.47,94.44);

        System.out.println(a.haversineDistance(b));
        */

        /*Graphe g = new Graphe("100_nodes_positive.csv");

        System.out.println("\nGlouton :");

        g.glouton();
        System.out.println("Poids = " +g.getTotalWeight());*/

        /*System.out.println("\nMST :");

        Graphe g2 = new Graphe("52-points (avec-points).csv");
        g2.construireGrapheComplet();
        g2 = g2.mst();
        System.out.println("Poids = " + g2.getTotalWeight());*/

        /*Graphe g3 = new Graphe("100_nodes_positive.csv");
        g3.construireGrapheComplet();
        g3 = g3.mm();
        System.out.println("Poids = " + g3.getTotalWeight());*/

    int repetitions = 1;

/*//GLOUTON
    double minG = Double.MAX_VALUE;
    double maxG = Double.MIN_VALUE;
    long totalTimeG = 0;

    for (int i = 0; i < repetitions; i++) {

        Graphe g = new Graphe("1000_noeuds.csv");

        long startG = System.nanoTime();

        g.glouton();

        long endG = System.nanoTime();

        double poidsG = g.getTotalWeight();

        if (poidsG < minG) minG = poidsG;
        if (poidsG > maxG) maxG = poidsG;

        totalTimeG += (endG - startG);
    }

    System.out.println("===== Glouton =====");
    System.out.println("MIN G = " + minG);
    System.out.println("MAX G = " + maxG);
    System.out.println("Temps moyen G (ms) = " + (totalTimeG / (double) repetitions / 1_000_000)+"\n");*/


    //mst
    long totalTimeS = 0;
    double poidsS = 0;

    for (int i = 0; i < repetitions; i++) {

        Graphe s = new Graphe("1000_noeuds.csv");

        s.construireGrapheComplet();

        long startS = System.nanoTime();

        s = s.mst();

        long endS = System.nanoTime();

        poidsS = s.getTotalWeight();

        totalTimeS += (endS - startS);
    }

    System.out.println("\n===== MST =====");
    System.out.println("Poids total : "+ poidsS);
    System.out.println("Temps moyen S (ms) = " + (totalTimeS / (double) repetitions / 1_000_000)+"\n");


//MM
    long totalTimeM = 0;
    double poidsM = 0;

    for (int i = 0; i < repetitions; i++) {

        Graphe m = new Graphe("1000_noeuds.csv");

        m.construireGrapheComplet();

        long startM = System.nanoTime();

        m = m.mm();

        long endM = System.nanoTime();

        poidsM = m.getTotalWeight();
        totalTimeM += (endM - startM);
    }

    System.out.println("\n===== MM =====");
    System.out.println("Poids total : "+ poidsM);
    System.out.println("Temps moyen M (ms) = " + (totalTimeM / (double) repetitions / 1_000_000));
    }
}