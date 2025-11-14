package frogger.model;

import frogger.model.entities.Log;
import frogger.model.entities.TurtleGroup;
import frogger.model.entities.Vehicle;

public class ObjectFactory {
    private final LaneConfig laneInfo;
    private int tickCounter;

    public ObjectFactory(LaneConfig laneConfig) {
        laneInfo = laneConfig;
    }

    public boolean isReachedTick() {
        tickCounter ++;
        if (tickCounter >= laneInfo.gapTicks()) {
            tickCounter = 0;
            return true;
        }
        return false;
    }

    public GameObject spawnObject() {
        if (isSpawn()) {
            return switch (laneInfo.entityType()) {
                case SHORT_LOG -> new Log(EntityType.SHORT_LOG, getSpawnX(), getY(), getDirection(), getInitSpeed());
                case LONG_LOG -> new Log(EntityType.LONG_LOG, getSpawnX(), getY(), getDirection(), getInitSpeed());
                case TURTLE -> new TurtleGroup(EntityType.TURTLE, getSpawnX(), getY(), getDirection(),
                        getInitSpeed(), 3, laneInfo.gapTicks());
                case CAR -> new Vehicle(EntityType.CAR, getSpawnX(), getY(), getDirection(), getInitSpeed(),
                        getAcceleration(), getMaxSpeed());
                case BUS -> new Vehicle(EntityType.BUS, getSpawnX(), getY(), getDirection(), getInitSpeed(),
                        getAcceleration(), getMaxSpeed());
                case TRUCK -> new Vehicle(EntityType.TRUCK, getSpawnX(), getY(), getDirection(), getInitSpeed(),
                        getAcceleration(), getMaxSpeed());
            };
        } else {
            return null;
        }
    }

    private boolean isSpawn() {
        return Math.random() <= laneInfo.spawnProbability();
    }

    private double getSpawnX() {
        if (laneInfo.direction() == Direction.RIGHT) {
            return - laneInfo.entityType().getWidth();
        } else {
            return FroggerGame.SCREEN_WIDTH;
        }
    }

    public double getY() {
        return laneInfo.startY();
    }

    public Direction getDirection() {
        return laneInfo.direction();
    }

    public double getInitSpeed() {
        return laneInfo.initialSpeed();
    }

    public double getAcceleration() {
        return laneInfo.acceleration();
    }

    public double getMaxSpeed() {
        return laneInfo.maxSpeed();
    }
}
