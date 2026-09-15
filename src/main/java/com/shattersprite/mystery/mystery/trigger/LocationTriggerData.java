package com.shattersprite.mystery.mystery.trigger;

/**
 * Data for location-based triggers
 */
public class LocationTriggerData {

    private String world;
    private double x;
    private double y;
    private double z;
    private double radius;

    public LocationTriggerData() {
        this.radius = 5.0;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
