package frogger.model.entities;

import frogger.model.Direction;
import frogger.model.FroggerGame;
import javafx.geometry.Rectangle2D;

/**
 * TurtleGroup represents N turtles placed adjacent to each other and moving as one unit.
 * It extends MovingPlatform so it integrates with existing platform/collision code.
 *
 * segmentWidth: width of each turtle (typically Lane.LANE_HEIGHT)
 * segmentCount: number of turtles in the group (e.g. 3)
 * diving: if true, the whole group periodically becomes unsafe (canStand toggles)
 */
public class TurtleGroup extends MovingPlatform {
    private final int segmentWidth;
    private final int segmentCount;

    private final boolean diving;
    private final int diveIntervalTicks;
    private int tickCounter = 0;

    public TurtleGroup(double x, double y, int segmentWidth,
                       Direction dir, double v,
                       int segmentCount, boolean diving, int diveGapMillis) {
        // total width covers all adjacent turtle segments
        super(x, y, segmentWidth * Math.max(1, segmentCount), segmentWidth, dir, v);
        this.segmentWidth = segmentWidth;
        this.segmentCount = Math.max(1, segmentCount);
        this.diving = diving;

        if (diving) {
            double tickMs = FroggerGame.SECONDS_PER_UPDATE * 1000.0;
            this.diveIntervalTicks = Math.max(1, (int) Math.round(Math.max(1, diveGapMillis) / tickMs));
        } else {
            this.diveIntervalTicks = -1;
        }

        this.canStand = true;
    }

    @Override
    public void update() {
        super.update();
        if (diving) {
            tickCounter++;
            if (tickCounter >= diveIntervalTicks) {
                tickCounter = 0;
                canStand = !canStand;
            }
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

    @Override
    public Rectangle2D getHitBox() {
        return super.getHitBox();
    }
}