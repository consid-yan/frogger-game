package frogger.model.managers;

import frogger.display.GameScreen;
import frogger.model.*;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * A LevelManager manages the lifecycle of every Level, spawns/updates lanes, and owns home slots.
 * - It answers whether the level is finished and whether the game is won.
 */
public class LevelManager {
    public static final int TOTAL_LEVELS = 4;
    public static final int NO_HOMES = 5;

    private final Level[] levels;
    private final List<Home> homes = new ArrayList<>();
    private int currentLevel;

    public LevelManager() {
        levels = new Level[TOTAL_LEVELS];
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels[i] = new Level(0.8 + i * 0.07, 6 - i);
            levels[i].initializeLanes();
        }
        currentLevel = 0;
        initializeHomes();
    }

    public void updateCurrentLevel() {
        levels[currentLevel].update();
    }

    public void advance() {
        if (currentLevel < TOTAL_LEVELS - 1) {
            levels[currentLevel].clearLanes();
            currentLevel ++;
            levels[currentLevel].initializeLanes();
            initializeHomes();
        }
    }

    public void reset() {
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels[i].clearLanes();
            levels[i].initializeLanes();
        }
        currentLevel = 0;
        initializeHomes();
    }

    private void initializeHomes() {
        homes.clear();
        for (int i = 0; i < NO_HOMES; i++) {
            homes.add(new Home(i * FroggerGame.SCREEN_WIDTH / 6.0 + 105, GameScreen.LANE_HEIGHT));
        }
    }

    public int getCurrentLevelNumber() {
        return currentLevel + 1;
    }

    public int getInitialLives() {
        return levels[currentLevel].getInitialLives();
    }

    public Vehicle[] getVehicles() {
        return levels[currentLevel].getVehicles();
    }

    public MovingPlatform[] getPlatforms() {
        return levels[currentLevel].getPlatforms();
    }

    /**
     * Uses CollisionManager to determine if player is inside any home.
     */
    public HomeEntry handleHomeEntry(Player player, CollisionManager collisionManager) {
        Home home = (Home) collisionManager.firstCollision(player, homes).orElse(null);
        if (home == null) return HomeEntry.NONE;

        if (!home.isOccupied()) {
            home.occupied();
            return HomeEntry.NEWLY_OCCUPIED;
        }
        return HomeEntry.SAFE_OCCUPIED;
    }

    public boolean isLevelFinished() {
        for (Home home : homes) {
            if (!home.isOccupied()) return false;
        }
        return true;
    }

    public List<Home> getHomesCopy() {
        return new ArrayList<>(homes);
    }
}
