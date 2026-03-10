package dev.e_psi_lon;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Kruskal {
    private final ArrayList<Arc> arcs;
    private final Set<Integer> hmap;

    public Kruskal(@NotNull Graphe graphe) {
        this.arcs = new ArrayList<>();
        graphe.hmap.values().forEach(noeud -> noeud.getSucc().forEach(arc -> {
            if (arc.source().getId() < arc.cible().getId()) arcs.add(arc);
        }));
        arcs.sort(Comparator.comparingInt(Arc::weight));
        this.hmap = new HashSet<>(graphe.hmap.keySet());
    }

    private int find(@NotNull HashMap<Integer, Integer> parent, int id) {
        if (!parent.get(id).equals(id)) {
            parent.put(id, find(parent, parent.get(id)));
        }
        return parent.get(id);
    }

    private boolean union(@NotNull HashMap<Integer, Integer> parent, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX == rootY) return false;
        parent.put(rootX, rootY);
        return true;
    }

    public ArrayList<Arc> execute() {
        HashMap<Integer, Integer> parent = new HashMap<>();
        for (int id : hmap) {
            parent.put(id, id);
        }
        ArrayList<Arc> mst = new ArrayList<>();
        for (Arc arc : arcs) {
            if (mst.size() == hmap.size() - 1) break;
            if (union(parent, arc.source().getId(), arc.cible().getId())) {
                mst.add(arc);
            }
        }
        return mst;
    }

    public Graphe executeGraphe() {
        Graphe mst = new Graphe();
        for (Arc arc : execute()) {
            int x = arc.source().getId();
            int y = arc.cible().getId();
            int w = arc.weight();
            if (!mst.hasNoeud(x)) mst.addNoeud(new Noeud(x));
            if (!mst.hasNoeud(y)) mst.addNoeud(new Noeud(y));
            mst.addArc(x, y, w);
            mst.addArc(y, x, w);
        }
        return mst;
    }

}
