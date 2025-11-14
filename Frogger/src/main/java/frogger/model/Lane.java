package frogger.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A Lane holds moving GameObjects (vehicles, logs, turtles).
 * - Updates/removes objects every tick.
 * - While Factory is the producer of the objects on lane, Lane itself holds and manages them.
 */
public class Lane implements Updatable {
    private ObjectFactory objectFactory;
    private final List<GameObject> objects = new ArrayList<>();

    public Lane(LaneConfig config) {
        objectFactory = new ObjectFactory(config);
    }

    @Override
    public void update() {
        // Remove objects that moved out of the screen
        objects.removeIf(this::isObjectOutOfScreen);

        // Update existing updatable objects
        for (GameObject object : objects) {
            if (object instanceof Updatable) {
                ((Updatable) object).update();
            }
        }

        // Spawn new objects if factory is active
        if (objectFactory != null) {
            if (objectFactory.isReachedTick()) {
                GameObject created = objectFactory.spawnObject();
                if (created != null) {
                    objects.add(created);
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

    public List<GameObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public void clearLane() {
        objects.clear();
        if (objectFactory != null) {
            objectFactory = null;
        }
    }
}
