package frogger.model;

public class LaneConfig {
    private final double initSpeed;
    private final double acceleration;
    private final double maxSpeed;
    private final int gapTicks;

    public LaneConfig(double baseSpeed, int gap) {
        initSpeed = baseSpeed;
        acceleration = 0.01 * baseSpeed;
        maxSpeed = 2 * baseSpeed;
        gapTicks = gap;
    }

    public double getInitSpeed() {
        return initSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public int getGap() {
        return gapTicks;
    }
}
