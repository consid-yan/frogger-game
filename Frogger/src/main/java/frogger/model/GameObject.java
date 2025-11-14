package frogger.model;

import javafx.geometry.Rectangle2D;

public abstract class GameObject {
    protected double gridX;
    protected double gridY;
    private int width;
    private int height;
    protected Rectangle2D hitBox;

    public GameObject(double x, double y, int w, int h) {
        gridX = x;
        gridY = y;
        width = w;
        height = h;
        hitBox = new Rectangle2D(x, y, w, h);
    }

    public Rectangle2D getHitBox(){
        return hitBox;
    }

    public boolean isIntersects(GameObject other) {
        return getHitBox().intersects(other.getHitBox());
    }

    public double getX() { return gridX; }

    public double getY() { return gridY; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }
}

