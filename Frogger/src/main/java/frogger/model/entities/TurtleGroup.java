package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.EntityType;

public class TurtleGroup extends MovingPlatform {
    private final int segmentWidth;
    private final int segmentCount;
    private final int diveIntervalTicks;
    private final boolean diving;

    private int tickCounter = 0;

    public TurtleGroup(EntityType type, double x, double y, Direction dir, double v,
                       int count, int diveGap, boolean canDive) {
        super(x, y, type.getWidth() * count, type.getHeight(), dir, v);
        segmentWidth = type.getWidth();
        segmentCount = count;
        diveIntervalTicks = diveGap;
        diving = canDive;
    }

    @Override
    public void update() {
        super.update();
        tickCounter++;
        if (diving && tickCounter >= diveIntervalTicks) {
            tickCounter = 0;
            canStand = !canStand;
        }
    }

    @Override
    public boolean isSafeToStand() {
        return canStand;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public int getSegmentWidth() {
        return segmentWidth;
    }
}