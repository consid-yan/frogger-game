package frogger.model.factories;

import frogger.model.*;
import frogger.model.entities.TurtleGroup;

/**
 * Factory that spawns a group of three turtles (alternating between diving and normal).
 * The 'gap' parameter controls the spawn interval between groups.
 */
public class TurtleFactory extends ObjectFactory {
    private boolean nextIsDiving;

    private static final int DIVING_GAP_MILLIS = 5000;
    private static final int GROUP_SIZE = 3;

    public TurtleFactory(double y, Direction dir, double v, int gap) {
        super(y, dir, v, gap);
        nextIsDiving = false;
    }

    @Override
    protected GameObject spawnObject() {
        boolean divingThisSpawn = nextIsDiving;
        nextIsDiving = !nextIsDiving;

        return new TurtleGroup(
                getX(),
                getY(),
                Lane.LANE_HEIGHT,
                getDirection(),
                getSpeed(),
                GROUP_SIZE,
                divingThisSpawn,
                divingThisSpawn ? DIVING_GAP_MILLIS : 0
        );
    }
}