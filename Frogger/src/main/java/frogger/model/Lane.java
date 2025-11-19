package frogger.model;

import frogger.model.entities.Log;
import frogger.model.entities.TurtleGroup;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * A Lane creates and holds moving GameObjects (Vehicle, Log, Turtle).
 * - Updates/removes objects every tick.
 */
public class Lane implements Updatable {
    private final List<GameObject> objects = new ArrayList<>();
    private final double startY;
    private final EntityType entityType;
    private final Direction direction;
    private final LaneConfig laneInfo;

    private int tickCounter;
    private boolean isDiving;

    public Lane(double y, EntityType type, Direction dir, LaneConfig config) {
        startY = y;
        entityType = type;
        direction = dir;
        laneInfo = config;
    }

    @Override
    public void update() {
        tickCounter ++;
        // Remove objects that moved out of the screen
        objects.removeIf(this::isObjectOutOfScreen);

        // Update existing updatable objects
        for (GameObject object : objects) {
            if (object instanceof Updatable) {
                ((Updatable) object).update();
            }
        }

        // Spawn new objects if it is the time
        if (tickCounter >= laneInfo.getGap() || objects.isEmpty()) {
            tickCounter = 0;
            isDiving = !isDiving;
            double num = Math.random() - 0.5;
            if (Math.abs(num) < 0.27) {
                switch (entityType) {
                    case LONG_LOG, SHORT_LOG -> objects.add(new Log(entityType, getSpawnX() + num * Player.FROG_WIDTH,
                            startY, direction, laneInfo.getInitSpeed()));

                    case TURTLE -> objects.add(new TurtleGroup(entityType, getSpawnX() + num * Player.FROG_WIDTH,
                            startY, direction, laneInfo.getInitSpeed(), 3, laneInfo.getGap() / 3,
                            isDiving));

                    case CAR, BUS, TRUCK -> objects.add(new Vehicle(entityType, getSpawnX(), startY, direction,
                            laneInfo.getInitSpeed() + num, laneInfo.getAcceleration(),
                            laneInfo.getMaxSpeed() + num));
                }
            }
        }
    }

    private boolean isObjectOutOfScreen(GameObject object) {
        if (object instanceof Movable) {
            if (((Movable) object).getDirection() == Direction.RIGHT) {
                return object.getX() > FroggerGame.SCREEN_WIDTH;
            } else {
                return object.getX() < -object.getWidth();
            }
        }
        return false;
    }

    private double getSpawnX() {
        if (direction == Direction.RIGHT) {
            return -entityType.getWidth();
        } else {
            return FroggerGame.SCREEN_WIDTH;
        }
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public void clearLane() {
        objects.clear();
    }
}
