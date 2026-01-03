package frogger.model;

import frogger.model.entities.MovingPlatform;
import javafx.geometry.Rectangle2D;

public class Player extends GameObject implements Cloneable {
    public static final int FROG_WIDTH = 32;
    public static final int FROG_HEIGHT = 24;
    private static final double START_X = 400 - FROG_WIDTH;
    private static final double START_Y = 450;

    private boolean isAlive;
    private Direction currentDirection;

    public Player() {
        super(START_X, START_Y, FROG_WIDTH, FROG_HEIGHT);
        isAlive = true;
        currentDirection = Direction.UP;
    }

    @Override
    public Player clone() {
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported");
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;
    }

    // We would like to know player's direction since we need to draw it
    public Direction getDirection() {
        if (currentDirection == null) {
            return Direction.UP;
        }else {
            return currentDirection;
        }
    }

    public void resetDestroyed() {
        isAlive = true;
        currentDirection = Direction.UP;
        gridX = START_X;
        gridY = START_Y;
        hitBox = new Rectangle2D(gridX, gridY, FROG_WIDTH, FROG_HEIGHT);
    }

    // Move the player if it is still in the screen after movement
    public void move(Direction dir) {
        currentDirection = dir;
        double newX = gridX + dir.getDeltaX() * FROG_WIDTH;
        double newY = gridY + dir.getDeltaY() * FROG_WIDTH;
        Rectangle2D newBox = new Rectangle2D(newX, newY, FROG_WIDTH, FROG_HEIGHT);
        if (FroggerGame.SCREEN_BOUNDS.contains(newBox)) {
            gridX = newX;
            gridY = newY;
            hitBox = newBox;
        }
    }

    // If player stands on a MovingPlatform, then it needs to move like the platform
    public void moveWithPlatform(MovingPlatform platform) {
        gridX += platform.getDirection().getDeltaX() * platform.getSpeed();
        hitBox = new Rectangle2D(getX(), getY(), FROG_WIDTH, FROG_HEIGHT);
    }
}