package frogger.model;

import frogger.display.GameScreen;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Level implements Updatable {
    private static final int ROAD_START_LANE = 6; // Notice: Lane number increases from up to down
    private static final int DIFFERENTIATING_FACTOR = 2;

    private final List<Lane> lanes = new ArrayList<>();
    private final LaneConfig roadConfig;
    private final LaneConfig riverConfig;
    private final int initialLives; // Lives the player will get at the start of every Level

    public Level(double baseSpeed, int baseGap, int initLives) {
        roadConfig = new LaneConfig(DIFFERENTIATING_FACTOR * baseSpeed, baseGap / DIFFERENTIATING_FACTOR);
        riverConfig = new LaneConfig(baseSpeed, baseGap);
        initialLives = initLives;
    }

    public void initializeLanes() {
        // River
        for (int i = 0; i < 2; i++) {
            lanes.add(new Lane((2 + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.SHORT_LOG, Direction.RIGHT, riverConfig));
            lanes.add(new Lane((3 + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.TURTLE, Direction.LEFT, riverConfig));
            lanes.add(new Lane((4 + 3 * i) * GameScreen.LANE_HEIGHT, EntityType.LONG_LOG, Direction.RIGHT, riverConfig));
        }

        // Road
        for (int i = 0; i < 2; i++) {
            lanes.add(new Lane((8 + 4 * i) * GameScreen.LANE_HEIGHT, EntityType.TRUCK, Direction.LEFT, roadConfig));
            lanes.add(new Lane((9 + 4 * i) * GameScreen.LANE_HEIGHT, EntityType.CAR, Direction.LEFT, roadConfig));
        }
        lanes.add(new Lane(10 * GameScreen.LANE_HEIGHT, EntityType.CAR, Direction.RIGHT, roadConfig));
        lanes.add(new Lane(11 * GameScreen.LANE_HEIGHT, EntityType.BUS, Direction.RIGHT, roadConfig));
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

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = ROAD_START_LANE; i < lanes.size(); i++) {
            for (GameObject obj : lanes.get(i).getObjects()) {
                if (obj instanceof Vehicle) {
                    vehicles.add((Vehicle) obj);
                }
            }
        }
        return vehicles;
    }

    public List<MovingPlatform> getPlatforms() {
        List<MovingPlatform> platforms = new ArrayList<>();
        for (int i = 0; i < ROAD_START_LANE; i++) {
            for (GameObject obj : lanes.get(i).getObjects()) {
                if (obj instanceof MovingPlatform) {
                    platforms.add((MovingPlatform) obj);
                }
            }
        }
        return platforms;
    }
}