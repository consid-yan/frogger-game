package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.EntityType;

public class Log extends MovingPlatform {
    EntityType entityType;

    public Log(EntityType type, double x, double y, Direction dir, double v) {
        super(x, y, type.getWidth(), type.getHeight(), dir, v);
    }

    public EntityType getType() {
        return entityType;
    }

    @Override
    public boolean isSafeToStand() {
        return true;
    }
}
