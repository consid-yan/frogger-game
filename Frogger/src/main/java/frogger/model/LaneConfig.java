package frogger.model;

public record LaneConfig(
        int startY,
        EntityType entityType,
        Direction direction,
        double initialSpeed,
        double acceleration,
        double maxSpeed,
        int gapTicks,
        double spawnProbability
) {}
