package frogger.model;

import frogger.model.factories.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A Lane holds moving GameObjects (vehicles, logs, turtles, ...).
 * - Owns a single ObjectFactory to spawn objects.
 * - Updates/removes objects every tick.
 * - No longer extends GameObject; a lane is a logical row, not a drawable entity.
 */
public class Lane implements Updatable {
    public static final int LANE_HEIGHT = FroggerGame.SCREEN_HEIGHT / 16;

    private final double gridY;
    private ObjectFactory objectFactory;
    private final List<GameObject> objects = new ArrayList<>();

    public Lane(double y, ObjectFactory factory) {
        gridY = y;
        objectFactory = factory;
    }

    @Override
    public void update() {
        // Remove objects that moved off-screen
        objects.removeIf(this::isObjectOutOfScreen);

        // Update existing updatable objects
        for (GameObject object : objects) {
            if (object instanceof Updatable) {
                ((Updatable) object).update();
            }
        }

        // Spawn new objects (at most one per tick) if factory is active
        if (objectFactory != null) {
            GameObject created = objectFactory.createObject();
            if (created != null) {
                objects.add(created);
            }
        }
    }

    private boolean isObjectOutOfScreen(GameObject object) {
        if (object instanceof Movable) {
            Movable moving = (Movable) object;
            if (moving.getDirection() == Direction.RIGHT) {
                return object.getX() > FroggerGame.SCREEN_WIDTH;
            } else {
                return object.getX() + object.getWidth() < 0;
            }
        }
        return false;
    }

    public double getY() {
        return gridY;
    }

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public void clearLane() {
        objects.clear();
        if (objectFactory != null) {
            objectFactory.stop();
            objectFactory = null;
        }
    }
}
