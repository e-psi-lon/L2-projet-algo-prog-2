package dev.e_psi_lon;

public class Arc {
    private final Noeud source;
    private final Noeud cible;
    private final int weight;

    public Arc(Noeud source, Noeud cible, int weight) {
        this.source = source;
        this.cible = cible;
        this.weight = weight;
    }

    public Arc(Noeud source, Noeud cible) {
        this(source, cible, 1);
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return " -> " + cible.toString();
    }

    public Noeud getCible() {
        return cible;
    }

    public Noeud getSource() {
        return source;
    }
}
