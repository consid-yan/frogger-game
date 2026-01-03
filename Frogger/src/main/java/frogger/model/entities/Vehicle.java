package frogger.model.entities;

import frogger.model.*;
import javafx.geometry.Rectangle2D;

public class Vehicle extends GameObject implements Updatable, Movable {
    private final EntityType entityType;
    private final Direction movingDirection;
    private double speed;
    private final double acceleration;
    private final double speedLimit;

    public Vehicle(EntityType type, double x, double y, Direction dir, double v, double a, double limit) {
        super(x, y, type.getWidth(), type.getHeight());
        entityType = type;
        movingDirection = dir;
        speed = v;
        acceleration = a;
        speedLimit = limit;
    }

    @Override
    public void update() {
        accelerate(acceleration);
        gridX += movingDirection.getDeltaX() * speed;
        hitBox = new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    private void accelerate(double acceleration) {
        if (speed <= speedLimit) {
            speed += acceleration;
        }
    }

    public EntityType getType() {
        return entityType;
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
