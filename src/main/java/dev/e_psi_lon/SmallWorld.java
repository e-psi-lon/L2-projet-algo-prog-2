package dev.e_psi_lon;

import java.util.LinkedList;
import java.util.Random;

public class SmallWorld extends Graphe {

    private final Random rand = new Random();


    public SmallWorld(int n, int d) {
        super(n);  

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= d; j++) {
                int voisin = (i + j) % n;
                addArc(i, voisin);
                addArc(voisin, i); 
            }
        }
    }


    public SmallWorld(int n, int d, double p) {
        this(n, d);  

        for (int v : hmap.keySet()) {
            Noeud noeudV = getNoeud(v);

            
            LinkedList<Arc> succCopy = new LinkedList<>(noeudV.getSucc());

            for (Arc arc : succCopy) {
                if (rand.nextDouble() < p) {
                    int r;
                    do {
                        r = rand.nextInt(hmap.size());
                    } while (r == v || noeudV.hasSuccesseur(r));

                    addArc(v, r);
                    addArc(r, v);
                }
            }
        }
    }
}