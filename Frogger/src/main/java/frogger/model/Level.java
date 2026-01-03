package frogger.model;

import frogger.display.GameScreen;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Level implements Updatable {
    private static final int ROW_LANE_START = 2;
    private static final int ROAD_START_LANE = 6; // Notice: Lane number increases from up to down

    private final List<Lane> lanes = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<MovingPlatform> platforms = new ArrayList<>();

    private final double baseSpeed;
    private final int baseGap;
    private final int initialLives; // Lives the player will get at the start of every Level

    public Level(double bSpeed, int gap, int initLives) {
        baseSpeed = bSpeed;
        baseGap = gap;
        initialLives = initLives;
    }

    /**
     * Creates 6 river lanes (alternating logs and turtles) followed by 6 road lanes
     * (with varying vehicle types and directions). Lane positions are calculated
     * using GameScreen.LANE_HEIGHT for uniform vertical spacing.
     */
    public void initializeLanes() {
        // River
        for (int i = 0; i < 2; i++) {
            lanes.add(new Lane((ROW_LANE_START + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.SHORT_LOG,
                    Direction.RIGHT, baseSpeed * 0.6, baseGap));

            lanes.add(new Lane((ROW_LANE_START + 1 + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.TURTLE,
                    Direction.LEFT, baseSpeed * 0.9,  baseGap));

            lanes.add(new Lane((ROW_LANE_START + 2 + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.LONG_LOG,
                    Direction.RIGHT, baseSpeed * 0.85,  baseGap));
        }

        // Road
        for (int i = 0; i < 6; i++) {
            EntityType type = switch (i) {
                case 0, 5 -> EntityType.TRUCK;
                case 1, 4 -> EntityType.BUS;
                default -> EntityType.CAR;
            };
            Direction dir = (i == 0 || i == 3 || i == 4) ? Direction.RIGHT : Direction.LEFT;

            lanes.add(new Lane((ROW_LANE_START + ROAD_START_LANE + i) * GameScreen.LANE_HEIGHT, type, dir, baseSpeed, baseGap));
        }
    }

    /**
     * Spawns new entities from lanes and updates existing ones. The update cycle:
     * 1. Spawn: First 6 lanes create platforms, remaining lanes create vehicles
     * 2. Update: All entities move based on their speed/direction
     * 3. Cleanup: Remove entities that have moved off-screen for memory management
     */
    @Override
    public void update() {
        for (int i = 0; i < ROAD_START_LANE; i++) {
            MovingPlatform platform = lanes.get(i).spawnPlatform();
            if (platform != null) {
                platforms.add(platform);
            }
        }

        for (int i = ROAD_START_LANE; i < lanes.size(); i++) {
            Vehicle vehicle = lanes.get(i).spawnVehicle();
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
        // Update and Remove
        vehicles.forEach(Vehicle::update);
        vehicles.removeIf(this::isObjectOutOfScreen);

        platforms.forEach(MovingPlatform::update);
        platforms.removeIf(this::isObjectOutOfScreen);
    }

    public int getInitialLives() {
        return initialLives;
    }

    public void clearLanes() {
        vehicles.clear();
        platforms.clear();
        lanes.clear();
    }

    private boolean isObjectOutOfScreen(GameObject object) {
        if (object instanceof Movable) {
            if (((Movable) object).getDirection() == Direction.RIGHT) {
                return object.getX() > FroggerGame.SCREEN_WIDTH;
            } else {
                return object.getX() < -object.getWidth();
            }
        }
        return false;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<MovingPlatform> getPlatforms() {
        return platforms;
    }

    public double getRoadStartRow() {
        return ROW_LANE_START + ROAD_START_LANE;
    }
}