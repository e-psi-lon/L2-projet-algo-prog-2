package dev.e_psi_lon;

public class Arc {
    private final Noeud source;
    private final Noeud cible;

    public Arc(Noeud source, Noeud cible) {
        this.source = source;
        this.cible = cible;
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
