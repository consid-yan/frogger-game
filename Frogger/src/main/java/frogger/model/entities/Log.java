package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.EntityType;

public class Log extends MovingPlatform {
    public Log(EntityType type, double x, double y, Direction dir, double v) {
        super(x, y, type.getWidth(), type.getHeight(), dir, v);
    }

    @Override
    public boolean isSafeToStand() {
        return true;
    }
}
