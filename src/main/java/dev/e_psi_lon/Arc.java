package dev.e_psi_lon;

public record Arc(Noeud source, Noeud cible, int weight) {
    public Arc(Noeud source, Noeud cible) {
        this(source, cible, 1);
    }
}
