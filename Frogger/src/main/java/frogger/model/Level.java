package frogger.model;

import frogger.display.GameScreen;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Level composes lanes and exposes aggregated access to entities.
 */
public class Level implements Updatable {
    private static final int ROAD_START_LANE = 6;
    public enum EntityType {
        LONG_LOG(120, 25), SHORT_LOG(80, 25),
        TURTLE(25, 30),
        CAR(45, 20), BUS(90, 30), TRUCK(120, 30);

        private final int width;
        private final int height;

        EntityType(int w, int h) {
            width = w;
            height = h;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private final List<Lane> lanes = new ArrayList<>();
    private final double baseSpeed;
    private final int initialLives;

    public Level(double bSpeed, int initLives) {
        baseSpeed = bSpeed;
        initialLives = initLives;
    }

    public void initializeLanes() {
        lanes.clear();
        lanes.add(addedLane(2, EntityType.SHORT_LOG, Direction.RIGHT, ));
        lanes.add(addedLane(3, EntityType.TURTLE, Direction.LEFT, ));
        lanes.add(addedLane(4, EntityType.LONG_LOG, Direction.RIGHT, ));
        lanes.add(addedLane(5, EntityType.SHORT_LOG, Direction.LEFT, 1.75 * baseSpeed, 0,
                1.75 * baseSpeed, 200, 0.5));
        lanes.add(addedLane(6, EntityType.TURTLE, Direction.RIGHT, ));
        lanes.add(addedLane(7, EntityType.SHORT_LOG, Direction.LEFT, 1.75 * baseSpeed, 0,
                1.75 * baseSpeed, 200, 0.5));

        lanes.add(addedLane(8, EntityType.CAR, Direction.RIGHT, ));
        lanes.add(addedLane(9, EntityType.TRUCK, Direction.RIGHT, ));
        lanes.add(addedLane(10, EntityType.BUS, Direction.LEFT, ));
        lanes.add(addedLane(11, EntityType.CAR, Direction.LEFT, ));
        lanes.add(addedLane(12, EntityType.CAR, Direction.RIGHT, ));
        lanes.add(addedLane(13, EntityType.BUS, Direction.RIGHT, ))

        for (int startRow = 2; startRow < 8; startRow += 3) {

            // Short Log
            addLogLane(startRow, Direction.RIGHT,
                    baseSpeed, 200, 80);

            // Turtle Group
            addTurtleLane(startRow + 1, Direction.LEFT,
                    1.75 * baseSpeed, 200);

            // long log
            addLogLane(startRow + 2, Direction.RIGHT,
                    1.25 * baseSpeed, 250, 120);
        }
        for (int j = 0; j < 3; j++) {
            int row = 8 + j;
            Direction direction = Direction.RIGHT;
            double speed = vehicleSpeedMul(j) * baseSpeed;
            int gap = vehicleGap(j);
            int width = vehicleWidth(j);
            double limit = vehicleSpeedLimit(j) * baseSpeed;
            addVehicleLane(row, Direction.RIGHT, speed, gap, width, limit);
        }
        for (int j = 0; j < 3; j++) {
            int row = 11 + j;
            double speed = vehicleSpeedMul(j) * baseSpeed;
            int gap = vehicleGap(j);
            int width = vehicleWidth(j);
            double limit = vehicleSpeedLimit(j) * baseSpeed;
            addVehicleLane(row, Direction.LEFT, speed, gap, width, limit);
        }
    }

    private Lane addedLane(int row, EntityType type, Direction dir, double initSpeed, double a,
                           double limit, int gap, double probability) {
        return new Lane(new LaneConfig(row * GameScreen.LANE_HEIGHT, type, dir, initSpeed, a,
                limit, gap, probability));
    }

    // Vehicle parameters

    private double vehicleSpeedMul(int j) {
        return 1.5 * j + 0.5;     // 0.5, 2.0, 3.5
    }

    private int vehicleGap(int j) {
        return 300 - (j * 75);    // 250, 225, 200
    }

    private int vehicleWidth(int j) {
        return 45 * (3 - j);      // 135, 90, 45
    }

    private double vehicleSpeedLimit(int j) {
        return 3 * (j + 0.5);     // 1.5, 4.5, 7.5
    }

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

    public Vehicle[] getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = ROAD_START_LANE; i < lanes.size(); i++) {
            for (GameObject obj : lanes.get(i).getObjects()) {
                if (obj instanceof Vehicle) {
                    vehicles.add((Vehicle) obj);
                }
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    public MovingPlatform[] getPlatforms() {
        List<MovingPlatform> platforms = new ArrayList<>();
        for (int i = 0; i < ROAD_START_LANE; i++) {
            for (GameObject obj : lanes.get(i).getObjects()) {
                if (obj instanceof MovingPlatform) {
                    platforms.add((MovingPlatform) obj);
                }
            }
        }
        return platforms.toArray(new MovingPlatform[0]);
    }
}