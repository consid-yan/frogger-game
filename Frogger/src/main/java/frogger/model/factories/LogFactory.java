package frogger.model.factories;

import frogger.model.*;
import frogger.model.entities.Log;

/**
 * Factory that spawns logs.
 */
public class LogFactory extends ObjectFactory {
    public LogFactory(double y, Direction dir, double v, int gap, int w) {
        super(y, dir, v, gap);
        width = w;
    }

    @Override
    protected GameObject spawnObject() {
        return new Log(getX(), getY(), width, Lane.LANE_HEIGHT, getDirection(), getSpeed());
    }
}
