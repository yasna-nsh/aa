package model.game;

public class Target {
    private static final double radius = 60;
    private static final double hitDistanceFromSurface = 120;

    public static double getInnerRadius() {
        return radius;
    }

    public static double getOuterRadius() {
        return radius + hitDistanceFromSurface;
    }
}
