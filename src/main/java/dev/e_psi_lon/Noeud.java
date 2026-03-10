package dev.e_psi_lon;

import java.util.ArrayList;

public class Noeud {
    private final int id;
    private boolean mark;
    ArrayList<Arc> succ;

    public Noeud(int id) {
        this.id = id;
        this.succ = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Arc> getSucc() {
        return succ;
    }

    public boolean hasSuccesseur(int j) {
        for (Arc arc : succ) {
            if (arc.cible().getId() == j) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Noeud(" + id + ")";
    }

    public void addArc(Arc arc) {
        if (succ.contains(arc)) return;
        succ.add(arc);
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isMarked() {
        return mark;
    }
}
