package model.game;

public class Ball {
    private static double radius = 10;
    private double attachedDegree;
    private final boolean isInTemplate;

    public Ball() {
        this.isInTemplate = false;
        attachedDegree = -1;
    }

    public Ball(double attachedDegree) {
        this.isInTemplate = true;
        this.attachedDegree = attachedDegree;
    }

    public static double getRadius() {
        return radius;
    }

    public double getAttachedDegree() {
        return attachedDegree;
    }

    public void setAttachedDegree(double attachedDegree) {
        this.attachedDegree = attachedDegree;
    }

    public boolean isInTemplate() {
        return isInTemplate;
    }
}
