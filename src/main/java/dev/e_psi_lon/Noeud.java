package dev.e_psi_lon;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class Noeud {
    private final int id;
    private boolean mark;
    ArrayList<Arc> succ;
    private double x;
    private double y;

    public Noeud(int id) {
        this.id = id;
        this.succ = new ArrayList<>();
        setPosition(0, 0);
    }

    public Noeud(int id, int x, int y) {
        this.id = id;
        this.succ = new ArrayList<>();
        setPosition(x, y);
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

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distance(@NotNull Noeud other) {
        double distanceX = x - other.getX();
        double distanceY = y - other.getY();
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    double haversineDistance(@NotNull Noeud other) {
        double longitude = Math.toRadians(x);
        double latitude = Math.toRadians(y);
        double otherLongitude = Math.toRadians(other.getX());
        double otherLatitude = Math.toRadians(other.getY());
        double deltaLongitude = 1 - Math.cos(otherLongitude - longitude) / 2;
        double deltaLatitude = 1 - Math.cos(otherLatitude - latitude) / 2;
        double distance = Math.asin(
                Math.sqrt(deltaLatitude + Math.cos(latitude) * Math.cos(otherLatitude) * deltaLongitude)
        ) * 2;
        return distance * 6371;
    }
}
