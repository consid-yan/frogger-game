package frogger.model.entities;

import frogger.model.*;
import javafx.geometry.Rectangle2D;

public class Vehicle extends GameObject implements Updatable, Movable {
    private final Direction movingDirection;
    private double speed;
    private final double speedLimit;
    private final double acceleration;

    public Vehicle(EntityType type, double x, double y, Direction dir, double v, double a, double limit) {
        super(x, y, type.getWidth(), type.getHeight());
        movingDirection = dir;
        speed = v;
        acceleration = a;
        speedLimit = limit;
        hitBox = new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void update() {
        accelerate(acceleration);
        gridX += movingDirection.getDeltaX() * speed;
        hitBox = new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    private void accelerate(double acceleration) {
        if (speed < speedLimit) {
            speed += acceleration;
        }
    }

    @Override
    public Direction getDirection() {
        return movingDirection;
    }

    @Override
    public double getSpeed() {
        return speed;
    }
}
