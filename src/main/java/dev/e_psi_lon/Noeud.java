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

    public Noeud(int id, double x, double y) {
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
    public String toString(){
        StringBuilder s = new StringBuilder("| id : " + id + " arc : ");
        for(Arc a : succ){
            s.append(a.cible().getId()).append(" ");
        }
        s.append("|");
        return s.toString();
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
        double latitude = Math.toRadians(x);
        double longitude = Math.toRadians(y);
        double otherLatitude = Math.toRadians(other.getX());
        double otherLongitude = Math.toRadians(other.getY());
        double deltaLongitude = otherLongitude - longitude;
        double deltaLatitude = otherLatitude - latitude;

        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
            + Math.cos(latitude) * Math.cos(otherLatitude)
            * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);

        double distance = 2*Math.asin(Math.sqrt(a));

        return distance * 6371;
    }
}
