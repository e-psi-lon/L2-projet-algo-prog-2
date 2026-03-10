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
    }
}
