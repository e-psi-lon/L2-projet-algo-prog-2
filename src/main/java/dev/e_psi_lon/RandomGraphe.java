package dev.e_psi_lon;

import java.util.HashSet;
import java.util.Random;

public class RandomGraphe extends Graphe {
    public RandomGraphe(int n, double p) {
        super(n);
        Random rand = new Random();
        int v = 1;
        int w = -1;
        while (v < n) {
            double r = rand.nextDouble();
            w = w + 1 + (int)(Math.log(1 - r) / Math.log(1 - p));
            while (w >= v && v < n) {
                w -= v;
                v++;
            }
            if (v < n) {
                addArc(v, w);
                addArc(w, v);
            }
        }
    }


    public RandomGraphe(int n, int m) {
        super(n);
        Random rand = new Random();
        int max = n * (n - 1) / 2;
        HashSet<Integer> used = new HashSet<>();
        while (used.size() < m) {
            int r = rand.nextInt(max);
            if (used.contains(r)) continue;
            used.add(r);
            int[] arc = bijection(r);
            addArc(arc[0], arc[1]);
            addArc(arc[1], arc[0]);
        }
    }

    private int[] bijection(int i) {
        int v = (int)(1 + Math.floor((-1 + Math.sqrt(1 + 8*i)) / 2));
        int w = i - (v*(v-1))/2;
        return new int[]{v, w};
    }
}