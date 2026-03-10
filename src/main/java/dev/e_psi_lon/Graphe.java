package dev.e_psi_lon;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Stack;
import java.util.LinkedList;

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
            br.readLine();
            br.lines().forEach(line -> {
                try {
                    String[] parts = line.split(",");
                    int x = Integer.parseInt(parts[0].trim());
                    if (!hasNoeud(x)) addNoeud(new Noeud(x));
                    int y = Integer.parseInt(parts[1].trim());
                    if (!hasNoeud(y)) addNoeud(new Noeud(y));
                    // If there is a third element, it's the weight
                    if (parts.length > 2) {
                        int weight = Integer.parseInt(parts[2].trim());
                        addArc(x, y, weight);
                        addArc(y, x, weight);
                    } else {
                        addArc(x, y);
                        addArc(y, x);
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Invalid line format: " + line);
                }
            });
        }

    }

    public boolean hasNoeud(int id) {
        return hmap.containsKey(id);
    }

    public void removeArc(int source, int cible) {
        Noeud noeudSource = getNoeud(source);
        if (noeudSource == null) return;
        noeudSource.getSucc().removeIf(arc -> arc.cible().getId() == cible);
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

    public void addArc(int source, int cible) {
        Noeud noeudSource = getNoeud(source);
        Noeud noeudCible = getNoeud(cible);
        if (noeudSource == null || noeudCible == null) return;
        if (noeudSource.hasSuccesseur(cible)) return;
        Arc arc = new Arc(noeudSource, noeudCible);
        noeudSource.addArc(arc);
    }

    public void addArc(int source, int cible, int weight) {
        Noeud noeudSource = getNoeud(source);
        Noeud noeudCible = getNoeud(cible);
        if (noeudSource == null || noeudCible == null) return;
        if (noeudSource.hasSuccesseur(cible)) return;
        Arc arc = new Arc(noeudSource, noeudCible, weight);
        noeudSource.addArc(arc);
    }

    public String toString() {
        return "Graphe:\n" +
                hmap.values().stream()
                        .map(Noeud::toString)
                        .reduce("", (a, b) -> a + b + "\n");
    }

    public void parcours(ParcoursType method) {
        for (Noeud noeud : hmap.values()) {
            noeud.setMark(false);
        }
        for (Noeud noeud : hmap.values()) {
            if (!noeud.isMarked()) {
                switch (method) {
                    case PROFONDEUR_R -> profR(noeud, 0);
                    case PROFONDEUR_L -> profL(noeud);
                    case LARGEUR -> largeur(noeud);
                }
            }
        }
    }

    private void profR(@NotNull Noeud noeud, int depth) {
        noeud.setMark(true);
        for (int i = 0; i < depth; i++) System.out.print("-");
        System.out.println(" " + noeud);
        for (Arc arc : noeud.getSucc()) {
            if (!arc.cible().isMarked()) profR(arc.cible(), depth + 1);
        }
    }

    private void profL(@NotNull Noeud noeud) {
        Stack<Noeud> stack = new Stack<>();
        noeud.setMark(true);
        stack.push(noeud);
        System.out.println(" " + noeud);
        while (!stack.isEmpty()) {
            Noeud current = stack.peek();
            boolean allMarked = current.getSucc().stream()
                    .allMatch(arc -> arc.cible().isMarked());
            if (allMarked) stack.pop();
            else {
                for (Arc arc : current.getSucc()) {
                    if (!arc.cible().isMarked()) {
                        arc.cible().setMark(true);
                        stack.push(arc.cible());
                        System.out.println(" " + arc.cible());
                        break;
                    }
                }
            }
        }
    }
    private void largeur(@NotNull Noeud noeud) {
        LinkedList<Noeud> file = new LinkedList<>();
        noeud.setMark(true);
        file.add(noeud);
        System.out.println(" " + noeud);

        while (!file.isEmpty()) {
            Noeud current = file.remove();
            for (Arc arc : current.getSucc()) {
                Noeud x = arc.cible();
                if (!x.isMarked()) {
                    x.setMark(true);
                    file.add(x);
                    System.out.println(" " + x);
                }
            }
        }
    }

    public void export() {
        StringBuilder buff = new StringBuilder("Source,Target,Weight\n");
        String sep = ",";
        for (Noeud n : hmap.values()) {
            for (Arc a : n.getSucc()) {
                if (n.getId() < a.cible().getId()) {
                    buff.append(n.getId()).append(sep).append(a.cible().getId()).append(sep).append(a.weight()).append("\n");
                }
            }
        }
        Path path = Path.of(getClass() + ".csv");
        try {
            Files.writeString(path, buff.toString());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
