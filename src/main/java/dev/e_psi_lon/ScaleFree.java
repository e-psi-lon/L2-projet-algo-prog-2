package dev.e_psi_lon;

import java.util.Random;

public class ScaleFree extends Graphe {

    private Random rand = new Random();

    public ScaleFree(int n, int d) {

        super(n);

        int[] M = new int[2 * n * d];

        for (int v = 0; v < n; v++) {

            for (int i = 0; i < d; i++) {

                int index = v * d + i;

                M[2 * index] = v;

                int r = rand.nextInt(Math.max(1, 2 * index + 1));

                M[2 * index + 1] = M[r];
            }
        }

        for (int i = 0; i < n * d; i++) {

            int v = M[2 * i];
            int w = M[2 * i + 1];

            if (v != w) {

                addArc(v, w);
                addArc(w, v);
            }
        }
    }
}