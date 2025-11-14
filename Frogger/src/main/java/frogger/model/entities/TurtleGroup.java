package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.EntityType;

/**
 * TurtleGroup represents N turtles placed adjacent to each other and moving as one unit.
 * -segmentWidth: width of each turtle (typically Lane.LANE_HEIGHT)
 * -segmentCount: number of turtles in the group (e.g. 3)
 */
public class TurtleGroup extends MovingPlatform {
    private final int segmentWidth;
    private final int segmentCount;
    private final int diveIntervalTicks;

    private int tickCounter = 0;

    public TurtleGroup(EntityType type, double x, double y, Direction dir, double v,
                       int segmentCount, int diveGap) {
        super(x, y, type.getWidth() * segmentCount, type.getHeight(), dir, v);
        segmentWidth = type.getWidth();
        this.segmentCount = segmentCount;
        diveIntervalTicks = diveGap;
    }

    @Override
    public void update() {
        super.update();
        tickCounter++;
        if (tickCounter >= diveIntervalTicks) {
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