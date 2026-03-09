package dev.e_psi_lon;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Graphe graphe = new Graphe("graphe.csv");
        System.out.println("Parcours en profondeur (right) :");
        graphe.parcours("profR");
        System.out.println("Parcours en profondeur (left) :");
        graphe.parcours("profL");
        System.out.println("Parcours en largeur :");
    }
}
