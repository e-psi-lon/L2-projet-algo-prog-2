package dev.e_psi_lon;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

    int repetitions = 1;

//GLOUTON
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
    System.out.println("Temps moyen G (ms) = " + (totalTimeG / (double) repetitions / 1_000_000)+"\n");

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

        Graphe m = new Graphe("14_noeuds.csv");

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