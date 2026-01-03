package frogger.model;

import frogger.model.entities.Log;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.TurtleGroup;
import frogger.model.entities.Vehicle;

/**
 * A Lane creates and holds moving GameObjects (Vehicle, Log, Turtle).
 * - Updates/removes objects every tick.
 */
public class Lane {
    static final double ACCELERATION_FACTOR = 0.01;
    static final double MAX_SPEED_FACTOR = 2;

    private final double startY;
    private final EntityType entityType;
    private final Direction direction;
    private final double baseSpeed;
    private final int spawnGap;

    private boolean isDiving;
    private int tickCounter;

    public Lane(double y, EntityType type, Direction dir, double v, int gap) {
        startY = y;
        entityType = type;
        direction = dir;
        baseSpeed = v;
        spawnGap = gap;
        tickCounter = gap;
    }

    public Vehicle spawnVehicle() {
        tickCounter++;
        if (tickCounter >= spawnGap) {
            tickCounter = 0;
            double num = Math.random() - 0.5;
            if (Math.abs(num) < 0.25) {
                return switch (entityType) {
                    case CAR, BUS, TRUCK -> new Vehicle(entityType, getSpawnX(), startY, direction,
                            baseSpeed + num, ACCELERATION_FACTOR * baseSpeed, MAX_SPEED_FACTOR * baseSpeed + num);

                    default -> throw new IllegalStateException("Unexpected value: " + entityType);
                };
            }
        }
        return null;
    }

    public MovingPlatform spawnPlatform() {
        tickCounter++;
        if (tickCounter >= spawnGap) {
            tickCounter = 0;
            double num = Math.random() - 0.5;
            if (Math.abs(num) > 0.25) {
                return switch (entityType) {
                    case LONG_LOG, SHORT_LOG -> new Log(entityType, getSpawnX(), startY, direction, baseSpeed);

                    case TURTLE -> {
                        isDiving = !isDiving;   // We should not always spawn diving / non-diving turtle
                        yield new TurtleGroup(entityType, getSpawnX() + num * Player.FROG_WIDTH,
                            startY, direction, baseSpeed, 3, spawnGap, isDiving);
                    }

                    default -> throw new IllegalStateException("Unexpected value: " + entityType);
                };
            }
        }
        return null;
    }

    private double getSpawnX() {
        if (direction == Direction.RIGHT) {
            return -entityType.getWidth();
        } else {
            return FroggerGame.SCREEN_WIDTH;
        }
    }
}
