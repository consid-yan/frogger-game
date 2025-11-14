package frogger.model.entities;

import frogger.model.*;
import javafx.geometry.Rectangle2D;

public abstract class MovingPlatform extends GameObject implements Updatable, Movable {
    private final Direction movingDirection;
    private final double speed;
    protected boolean canStand;

    public MovingPlatform(double x, double y, int w, int h, Direction dir, double v) {
        super(x, y, w, h);
        movingDirection = dir;
        speed = v;
        canStand = true;
    }

    @Override
    public void update() {
        gridX += getDirection().getDeltaX() * getSpeed();
        hitBox = new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public Direction getDirection() {
        return movingDirection;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    public abstract boolean isSafeToStand();
}
