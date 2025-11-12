package frogger.model.factories;

import frogger.model.Direction;
import frogger.model.FroggerGame;
import frogger.model.GameObject;

/**
 * Base factory for objects that appear on lanes.
 *
 * Factories are driven by the game update loop: each update the lane calls
 * createObject(). The factory maintains a simple tick-counter ("gap") and
 * will return a new GameObject only when the counter reaches the configured gap.
 */
public abstract class ObjectFactory {
    private final double gridY;
    protected int width;
    private final Direction direction;
    private final double speed;

    // gap measured in update ticks; the factory will produce an object every gap ticks.
    private final int gapTicks;
    private int tickCounter;

    /**
     * @param y    vertical position (grid) for created objects
     * @param dir  direction objects will travel
     * @param v    speed of created objects
     * @param gap  gap in update ticks between spawns (must be >= 1)
     */
    public ObjectFactory(double y, Direction dir, double v, int gap) {
        this.gridY = y;
        this.direction = dir;
        this.speed = v;
        this.gapTicks = Math.max(1, gap);
        // initialize so that the first createObject() call will spawn immediately
        this.tickCounter = this.gapTicks;
    }

    /**
     * Called each update by the Lane. Returns a new GameObject when the internal
     * timer indicates it is time to spawn, otherwise returns null.
     */
    public GameObject createObject() {
        tickCounter++;
        if (tickCounter >= gapTicks) {
            tickCounter = 0;
            return spawnObject();
        }
        return null;
    }

    /**
     * Concrete factories implement this to actually construct a new GameObject.
     * This method is called by createObject() only when it's time to spawn.
     */
    protected abstract GameObject spawnObject();

    /**
     * Optional stop hook (no-op here). Concrete factories that need to release
     * resources can override.
     */
    public void stop() {
        // no-op by default
    }

    public double getX() {
        if (direction == Direction.RIGHT) {
            return - width;
        } else {
            return FroggerGame.SCREEN_WIDTH;
        }
    }

    public double getY() {
        return gridY;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
}
