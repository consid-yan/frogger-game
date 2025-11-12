package frogger.model.managers;

import frogger.model.*;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages level lifecycle, spawns/updates lanes, and owns home slots.
 * Also answers whether the level is finished and whether the game is won.
 * Tier-1 collision integration: uses CollisionManager for home intersection.
 */
public class LevelManager {
    public static final int TOTAL_LEVELS = 4;
    public static final int NO_HOMES = 5;

    private final Level[] levels;
    private final List<Home> homes = new ArrayList<>();
    private int currentIndex;

    public LevelManager() {
        levels = new Level[TOTAL_LEVELS];
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels[i] = new Level(0.9 + i * 0.05, 6 - i);
            levels[i].initializeLanes();
        }
        currentIndex = 0;
        initializeHomes();
    }

    public void updateCurrentLevel() {
        levels[currentIndex].update();
    }

    public void advance() {
        if (currentIndex < TOTAL_LEVELS - 1) {
            levels[currentIndex].clearLanes();
            currentIndex++;
            levels[currentIndex].initializeLanes();
            initializeHomes();
        }
    }

    public void reset() {
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels[i].clearLanes();
            levels[i].initializeLanes();
        }
        currentIndex = 0;
        initializeHomes();
    }

    public int getCurrentLevelNumber() {
        return currentIndex + 1;
    }

    public int getInitialLives() {
        return levels[currentIndex].getInitialLives();
    }

    public Vehicle[] getVehicles() {
        return levels[currentIndex].getVehicles();
    }

    public MovingPlatform[] getPlatforms() {
        return levels[currentIndex].getPlatforms();
    }

    public enum HomeEntry { NONE, SAFE_OCCUPIED, NEWLY_OCCUPIED }

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

    private void initializeHomes() {
        homes.clear();
        for (int i = 0; i < NO_HOMES; i++) {
            homes.add(new Home(i * FroggerGame.SCREEN_WIDTH / 6.0 + 105, Lane.LANE_HEIGHT));
        }
    }
}
