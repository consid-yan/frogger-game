package frogger.model;

import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;
import frogger.model.factories.LogFactory;
import frogger.model.factories.TurtleFactory;
import frogger.model.factories.VehicleFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Level composes lanes and exposes aggregated access to entities.
 * Vehicles are unchanged.
 * Platforms (logs, turtle groups) use speed inversely proportional to their length:
 *   v ∝ 1 / L  => v = baseSpeed * (referenceLength / L)
 * where referenceLength is one lane height so a 1-segment platform moves at baseSpeed.
 */
public class Level implements Updatable {
    private final List<Lane> lanes = new ArrayList<>();
    private final double baseSpeed;
    private final int initialLives;

    public Level(double bSpeed, int initLives) {
        baseSpeed = bSpeed;
        initialLives = initLives;
    }

    public void initializeLanes() {
        lanes.clear();

        // Water lanes (rows 2..7): pattern [log(w=40)], [turtle group (3 segments)], [log(w=80)]
        for (int startRow = 2; startRow < 8; startRow += 3) {
            // short log (faster, shorter length)
            addLogLane(startRow, Direction.RIGHT,
                    platformSpeedForLength(40), /*gap*/150, /*width*/40);

            // turtle group: 3 segments of Lane.LANE_HEIGHT length each
            int turtleGroupLength = 3 * Lane.LANE_HEIGHT;
            addTurtleLane(startRow + 1, Direction.RIGHT,
                    platformSpeedForLength(turtleGroupLength), /*gap*/200);

            // long log (slower, longer length)
            addLogLane(startRow + 2, Direction.RIGHT,
                    platformSpeedForLength(80), /*gap*/125, /*width*/80);
        }

        for (int blockStart = 8; blockStart < 14; blockStart += 3) {
            for (int j = 0; j < 3; j++) {
                int row = blockStart + j;
                double speed = vehicleSpeedMul(j) * baseSpeed;
                int gap = vehicleGap(j);
                int width = vehicleWidth(j);
                double extra = vehicleSpeedLimit(j) * baseSpeed;

                addVehicleLane(row, Direction.RIGHT, speed, gap, width, extra);
            }
        }
    }

    // Inverse proportional speed: v = baseSpeed * (referenceLength / L)
    private double platformSpeedForLength(int lengthPixels) {
        if (lengthPixels <= 0) return 0.0;
        return baseSpeed * Math.pow(100 / lengthPixels, 0.5);
    }

    // ---- Lane creators ----

    private void addLogLane(int row, Direction dir, double speed, int gapTicks, int width) {
        double y = row * Lane.LANE_HEIGHT;
        lanes.add(new Lane(y, new LogFactory(y, dir, speed, gapTicks, width)));
    }

    private void addTurtleLane(int row, Direction dir, double speed, int gapTicks) {
        double y = row * Lane.LANE_HEIGHT;
        lanes.add(new Lane(y, new TurtleFactory(y, dir, speed, gapTicks)));
    }

    private void addVehicleLane(int row, Direction dir, double speed, int gapTicks, int width, double extraSpeed) {
        double y = row * Lane.LANE_HEIGHT;
        lanes.add(new Lane(y, new VehicleFactory(y, dir, speed, gapTicks, width, extraSpeed)));
    }

    // ---- Vehicle parameters (unchanged) ----

    private double vehicleSpeedMul(int j) {
        return 1.5 * j + 0.5;     // 0.5, 2.0, 3.5
    }

    private int vehicleGap(int j) {
        return 250 - (j * 25);    // 250, 225, 200
    }

    private int vehicleWidth(int j) {
        return 45 * (3 - j);      // 135, 90, 45
    }

    private double vehicleSpeedLimit(int j) {
        return 3 * (j + 0.5);     // 1.5, 4.5, 7.5
    }

    // ---- Updatable ----

    @Override
    public void update() {
        for (Lane lane : lanes) {
            lane.update();
        }
    }

    public int getInitialLives() {
        return initialLives;
    }

    public void clearLanes() {
        for (Lane lane : lanes) {
            lane.clearLane();
        }
        lanes.clear();
    }

    // ---- Aggregations ----

    public Vehicle[] getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Lane lane : lanes) {
            for (GameObject obj : lane.getObjects()) {
                if (obj instanceof Vehicle) {
                    vehicles.add((Vehicle) obj);
                }
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    public MovingPlatform[] getPlatforms() {
        List<MovingPlatform> platforms = new ArrayList<>();
        for (Lane lane : lanes) {
            for (GameObject obj : lane.getObjects()) {
                if (obj instanceof MovingPlatform) {
                    platforms.add((MovingPlatform) obj);
                }
            }
        }
        return platforms.toArray(new MovingPlatform[0]);
    }
}