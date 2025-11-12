package frogger.model.factories;

import frogger.model.*;
import frogger.model.entities.Vehicle;

/**
 * Factory that spawns vehicles.
 */
public class VehicleFactory extends ObjectFactory {
    private final double speedLimit;

    public VehicleFactory(double y, Direction dir, double v, int gap, int w, double limit) {
        super(y, dir, v, gap);
        width = w;
        speedLimit = limit;
    }

    @Override
    protected GameObject spawnObject() {
        return new Vehicle(getX(), getY(), width, Lane.LANE_HEIGHT, getDirection(), getSpeed(), speedLimit);
    }
}
