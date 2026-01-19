package it.packovery.service.model;

public class Route {

    private String polyline;
    private double distanceKm;
    private long durationMin;

    public Route() {}

    public Route(String polyline, double distanceKm, long durationMin) {
        this.polyline = polyline;
        this.distanceKm = distanceKm;
        this.durationMin = durationMin;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public long getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(long durationMin) {
        this.durationMin = durationMin;
    }
}
