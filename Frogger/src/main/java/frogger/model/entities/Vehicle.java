package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.GameObject;
import frogger.model.Movable;
import frogger.model.Updatable;
import javafx.geometry.Rectangle2D;

public class Vehicle extends GameObject implements Updatable, Movable {
    private final Direction movingDirection;
    private double speed;
    private final double speedLimit;

    public Vehicle(double x, double y, int w, int h, Direction dir, double v, double limit) {
        super(x, y, w, h);
        movingDirection = dir;
        speed = v;
        speedLimit = limit;
    }

    @Override
    public void update() {
        if (speed < speedLimit) {
            speed += speed * 0.01;
        }
        gridX += movingDirection.getDeltaX() * speed;
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
}
