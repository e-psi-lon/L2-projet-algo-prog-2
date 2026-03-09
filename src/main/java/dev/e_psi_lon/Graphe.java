package dev.e_psi_lon;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Stack;

public class Graphe {
    HashMap<Integer, Noeud> hmap;

    public Graphe() {
        this.hmap = new HashMap<>();
    }

    public Graphe(int k) {
        this.hmap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            this.addNoeud(new Noeud(i));
        }
    }

    public Graphe(String file) throws IOException {
        this.hmap = new HashMap<>();
        Path path = Path.of(file);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            br.lines().forEach(line -> {
                try {
                    String[] parts = line.split(",");
                    int x = Integer.parseInt(parts[0].trim());
                    if (!hasNoeud(x)) addNoeud(new Noeud(x));
                    int y = Integer.parseInt(parts[1].trim());
                    if (!hasNoeud(y)) addNoeud(new Noeud(y));
                    this.addArc(x, y);
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Invalid line format: " + line);
                }
            });
        }

    }

    public boolean hasNoeud(int id) {
        return hmap.containsKey(id);
    }

    public void addNoeud(@NotNull Noeud noeud) {
        if (hmap.containsKey(noeud.getId())) return;
        hmap.put(noeud.getId(), noeud);
    }

    public Noeud getNoeud(int id) {
        if (hmap.containsKey(id)) {
            return hmap.get(id);
        }
        return null;
    }

    public void addArc(int x, int y) {
        Noeud noeudX = getNoeud(x);
        Noeud noeudY = getNoeud(y);
        if (noeudX == null || noeudY == null) return;
        if (noeudX.hasSuccesseur(y)) return;
        Arc arc = new Arc(noeudX, noeudY);
        noeudX.addArc(arc);
    }

    public String toString() {
        return "Graphe:\n" +
                hmap.values().stream()
                        .map(Noeud::toString)
                        .reduce("", (a, b) -> a + b + "\n");
    }

    public void parcours(String method) {
        for (Noeud noeud : hmap.values()) {
            noeud.setMark(false);
        }
        for (Noeud noeud : hmap.values()) {
            if (!noeud.isMarked()) {
                switch (method) {
                    case "profR" -> profR(noeud, 0);
                    case "profL" -> profL(noeud);
                    case "largeur" -> largeur(noeud);
                    default -> throw new IllegalArgumentException("Invalid method");
                }
            }
        }
    }

    private void profR(@NotNull Noeud noeud, int depth) {
        noeud.setMark(true);
        for (int i = 0; i < depth; i++) System.out.print("-");
        System.out.println(" " + noeud);
        for (Arc arc : noeud.getSucc()) {
            if (!arc.getCible().isMarked()) profR(arc.getCible(), depth + 1);
        }
    }

    private void profL(@NotNull Noeud noeud) {
        Stack<Noeud> stack = new Stack<>();
        noeud.setMark(true);
        stack.push(noeud);
        System.out.println(" " + noeud);
        while (!stack.isEmpty()) {
            Noeud current = stack.peek();
            if (current.isMarked()) stack.pop();
            else {
                if (!current.isMarked()) {
                    for (Arc arc : current.getSucc()) {
                        if (!arc.getCible().isMarked()) {
                            arc.getCible().setMark(true);
                            stack.push(arc.getCible());
                            System.out.println(" " + arc.getCible());
                            break;
                        }
                    }
                }
            }
            current.setMark(false);
        }

    }
    private void largeur(@NotNull Noeud noeud) {

    }
}
