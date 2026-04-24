package dev.e_psi_lon;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Graphe {
    HashMap<Integer, Noeud> hmap;
    private boolean isGeo = false;

    public Graphe() {
        this.hmap = new HashMap<>();
    }

    public Graphe(int k) {
        this.hmap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            this.addNoeud(new Noeud(i));
        }
    }


    public Graphe(String cheminFichier) throws IOException {
    this.hmap = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {

        // 1. Read type
        String premiereLigne = br.readLine().trim();
        this.isGeo = premiereLigne.equalsIgnoreCase("GEO");

        String ligne;

        // 2. Reads nodes (id, x, y)
        while ((ligne = br.readLine()) != null) {
            try {
                String[] parties = ligne.split(";");

                if (parties.length < 3) {
                    System.err.println("Ligne invalide : " + ligne);
                    continue;
                }

                int id = Integer.parseInt(parties[0].trim());
                double x = Double.parseDouble(parties[1].trim());
                double y = Double.parseDouble(parties[2].trim());

                if (!hasNoeud(id)) {
                    addNoeud(new Noeud(id, x, y));
                }

            } catch (Exception e) {
                System.err.println("Erreur parsing ligne : " + ligne);
            }
        }
    }
    }

    public boolean geo(){
        return isGeo;
    }

    public void setGeo(boolean value){
        isGeo = value;
    }

    public void construireGrapheComplet() {
        for (Noeud a : hmap.values()) {
            for (Noeud b : hmap.values()) {
                if (a != b && !a.hasSuccesseur(b.getId())) {

                    double dist;

                    if (isGeo) {
                        dist = a.haversineDistance(b);
                    } else {
                        dist = a.distance(b);
                    }

                    addArc(a.getId(), b.getId(), dist);
                }
            }
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
        Arc arc = new Arc(noeudSource, noeudCible, noeudSource.distance(noeudCible));
        noeudSource.addArc(arc);
    }

    public void addArc(int source, int cible, double weight) {
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

    public static @NotNull Graphe kruskal(@NotNull Graphe graphe) {
        ArrayList<Arc> arcs = new ArrayList<>();
        graphe.hmap.values().forEach(noeud -> noeud.getSucc().forEach(arc -> {
            if (arc.source().getId() < arc.cible().getId()) arcs.add(arc);
        }));
        arcs.sort(Comparator.comparingDouble(Arc::weight));

        Set<Integer> nodes = graphe.hmap.keySet();

        HashMap<Integer, Integer> parent = new HashMap<>();
        for (int id : nodes) parent.put(id, id);

        Graphe mst = new Graphe();
        int edgesAdded = 0;
        for (Arc arc : arcs) {
            if (edgesAdded == nodes.size() - 1) break;

            int x = arc.source().getId();
            while (!parent.get(x).equals(x)) {
                parent.put(x, parent.get(parent.get(x)));
                x = parent.get(x);
            }

            int y = arc.cible().getId();
            while (!parent.get(y).equals(y)) {
                parent.put(y, parent.get(parent.get(y)));
                y = parent.get(y);
            }

            if (x != y) {
                parent.put(x, y);
                int srcId = arc.source().getId();
                int tgtId = arc.cible().getId();
                Noeud originalSrc = graphe.getNoeud(srcId);
                Noeud originalTgt = graphe.getNoeud(tgtId);

                if (!mst.hasNoeud(srcId))
                    mst.addNoeud(new Noeud(srcId, originalSrc.getX(), originalSrc.getY()));

                if (!mst.hasNoeud(tgtId))
                    mst.addNoeud(new Noeud(tgtId, originalTgt.getX(), originalTgt.getY()));
                mst.addArc(srcId, tgtId, arc.weight());
                mst.addArc(tgtId, srcId, arc.weight());
                edgesAdded++;
            }
        }
        return mst;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        for (Arc arc : hmap.values().stream().flatMap(noeud -> noeud.getSucc().stream()).toList()) {
            totalWeight += arc.weight();
        }
        return totalWeight;
    }

    public void glouton() {
        double dist;
        long beginningTime = System.nanoTime();
        if (hmap.isEmpty()) return;
        Integer[] keys = hmap.keySet().toArray(new Integer[0]);
        int randomKey = keys[(int) (Math.random() * keys.length)];
        Noeud currentNode = hmap.get(randomKey);
        Noeud beginning = currentNode;
        currentNode.setMark(true);
        for (int i : hmap.keySet()) {
            double minDist = Double.MAX_VALUE;
            int newNode = -1;
            for (int j : hmap.keySet()) {
                if (!hmap.get(j).isMarked()) {
                    if(isGeo) {
                        dist = currentNode.haversineDistance(hmap.get(j));
                    }
                    else {
                        dist = currentNode.distance(hmap.get(j));
                    }
                    if (dist < minDist) {
                        minDist = dist;
                        newNode = j;
                    }
                }
            }
            if (newNode != -1) {
                addArc(currentNode.getId(), hmap.get(newNode).getId(), minDist);
                currentNode = hmap.get(newNode);
                currentNode.setMark(true);
            } else {
                break;
            }
        }
        double distRetour;
        if(isGeo) {
            distRetour = currentNode.haversineDistance(beginning);
        }
        else {
            distRetour = currentNode.distance(beginning);
        }
        addArc(currentNode.getId(), beginning.getId(), distRetour);
        long end = System.nanoTime();
        double temps = end - beginningTime;
        System.out.println("Estimated time: "+temps/1_000_000+" ms");
    }

    public void reset() {
        for (Noeud n : hmap.values()) {
            n.setMark(false);
            n.getSucc().clear();
        }
    }

    //MST

    public Graphe mst() {
        long beginningTime = System.nanoTime();
        Graphe mst = Graphe.kruskal(this); // Build the first minimal spanning tree

        for (Noeud n : mst.hmap.values()) {
            n.setMark(false);
        }

        List<Noeud> parcours = new ArrayList<>();
        Noeud start = mst.hmap.values().iterator().next();
        mst.profondeurMST(start, parcours); // depth

        // Removing duplicates
        List<Noeud> cycle = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (Noeud n : parcours) {
            if (!visited.contains(n.getId())) {
                cycle.add(n);
                visited.add(n.getId());
            }
        }

        cycle.add(cycle.getFirst()); // Close the cycle

        // Building resulting graph
        Graphe result = new Graphe();

        // Add nodes
        for (Noeud n : cycle) {
            result.addNoeud(new Noeud(n.getId(), n.getX(), n.getY()));
        }

        // Add arcs
        for (int i = 0; i < cycle.size() - 1; i++) {
            Noeud a = cycle.get(i);
            Noeud b = cycle.get(i + 1);

            double dist;

            if (this.isGeo) {
                dist = a.haversineDistance(b);
            }
            else {
                dist = a.distance(b);
            }

            result.addArc(a.getId(), b.getId(), dist);
        }

        long end = System.nanoTime();
        double temps = end - beginningTime;
        System.out.println("Estimated time: "+temps/1_000_000+" ms");

        return result;
    }

    private void profondeurMST(@NotNull Noeud noeud, @NotNull List<Noeud> parcours) {
        noeud.setMark(true);
        parcours.add(noeud);

        for (Arc arc : noeud.getSucc()) {
            if (!arc.cible().isMarked()) {
                profondeurMST(arc.cible(), parcours);
                parcours.add(noeud);
            }
        }
    }

    public Graphe mm() {
        long beginningTime = System.nanoTime();

        Graphe mst = Graphe.kruskal(this);

        List<Integer> oddVertices = getOddDegreeVertices(mst);

        List<int[]> matching = new ArrayList<>();
        List<Integer> remaining = new ArrayList<>(oddVertices);

        while (remaining.size() >= 2) {
            int uId = remaining.removeFirst();
            Noeud u = this.getNoeud(uId);

            double bestDist = Double.MAX_VALUE;
            int bestIndex = -1;

            for (int i = 0; i < remaining.size(); i++) {
                Noeud v = this.getNoeud(remaining.get(i));
                double dist = this.isGeo ? u.haversineDistance(v) : u.distance(v);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestIndex = i;
                }
            }

            int vId = remaining.remove(bestIndex);
            matching.add(new int[]{uId, vId});
        }

        Map<Integer, List<Integer>> multi = new HashMap<>();
        for (Integer id : this.hmap.keySet()) {
            multi.put(id, new ArrayList<>());
        }

        for (Noeud n : mst.hmap.values()) {
            for (Arc a : n.getSucc()) {
                if (n.getId() < a.cible().getId()) {
                    multi.get(n.getId()).add(a.cible().getId());
                    multi.get(a.cible().getId()).add(n.getId());
                }
            }
        }

        for (int[] pair : matching) {
            multi.get(pair[0]).add(pair[1]);
            multi.get(pair[1]).add(pair[0]);
        }

        int startId = mst.hmap.keySet().iterator().next();
        List<Integer> eulerCircuit = hierholzer(multi, startId);

        Set<Integer> visited = new LinkedHashSet<>(eulerCircuit);
        List<Integer> tour = new ArrayList<>(visited);
        tour.add(tour.getFirst());

        Graphe result = new Graphe();
        result.setGeo(this.isGeo);

        for (int id : tour) {
            Noeud original = this.getNoeud(id);
            if (!result.hasNoeud(id))
                result.addNoeud(new Noeud(id, original.getX(), original.getY()));
        }

        for (int i = 0; i < tour.size() - 1; i++) {
            Noeud a = this.getNoeud(tour.get(i));
            Noeud b = this.getNoeud(tour.get(i + 1));
            double dist = this.isGeo ? a.haversineDistance(b) : a.distance(b);
            result.addArc(a.getId(), b.getId(), dist);
        }

        long end = System.nanoTime();
        System.out.println("Estimated time: " + (end - beginningTime) / 1_000_000.0 + " ms");

        return result;
    }

    private static @NotNull List<Integer> getOddDegreeVertices(@NotNull Graphe mst) {
        Map<Integer, Integer> degree = new HashMap<>();
        for (Noeud n : mst.hmap.values()) {
            degree.put(n.getId(), 0);
        }
        for (Noeud n : mst.hmap.values()) {
            for (Arc a : n.getSucc()) {
                degree.put(n.getId(), degree.get(n.getId()) + 1);
            }
        }

        List<Integer> oddVertices = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : degree.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                oddVertices.add(entry.getKey());
            }
        }
        return oddVertices;
    }

    private @NotNull List<Integer> hierholzer(Map<Integer, List<Integer>> graph, int start) {
        Deque<Integer> stack = new ArrayDeque<>();
        List<Integer> circuit = new ArrayList<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (graph.get(v).isEmpty()) {
                circuit.add(v);
                stack.pop();
            } else {
                int u = graph.get(v).removeFirst();
                graph.get(u).remove(Integer.valueOf(v)); // remove reverse edge too
                stack.push(u);
            }
        }

        Collections.reverse(circuit); // Hierholzer builds it backwards
        return circuit;
    }
}
