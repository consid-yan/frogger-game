package frogger.model.entities;

import frogger.model.Direction;

public class Log extends MovingPlatform {
    public Log(double x, double y, int w, int h, Direction dir, double v) {
        super(x, y, w, h, dir, v);
    }

    @Override
    public boolean isSafeToStand() {
        return true;
    }
}
