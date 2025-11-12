package frogger.model.managers;

import frogger.display.PlayerListener;
import frogger.model.Direction;
import frogger.model.FroggerGame;
import frogger.model.Lane;
import frogger.model.Player;
import frogger.model.entities.MovingPlatform;
import javafx.geometry.Rectangle2D;

import java.util.Arrays;

/**
 * Handles player input/control, movement, collisions (isHit),
 * water logic (isFall), resets, and move-with-platform behavior.
 * Tier-1 collision integration: delegates rectangle intersection to CollisionManager.
 */
public class PlayerManager {
    private final PlayerListener input;
    private final Player player;

    public PlayerManager(PlayerListener input) {
        this.input = input;
        this.player = new Player();
    }

    public static class Result {
        public boolean lostLife;
        public boolean enteredHome;
        public boolean newlyOccupiedHome;
    }

    /**
     * Update player based on input and environment. Uses CollisionManager for all intersection checks.
     */
    public Result update(LevelManager levelManager, CollisionManager collisionManager) {
        Result result = new Result();

        // Input and movement
        Direction dir = input.getAndClearPressedDirection();
        if (dir != null) {
            player.move(dir);
        }

        LevelManager.HomeEntry entry = levelManager.handleHomeEntry(player, collisionManager);
        if (entry != LevelManager.HomeEntry.NONE) {
            result.enteredHome = true;
            result.newlyOccupiedHome = (entry == LevelManager.HomeEntry.NEWLY_OCCUPIED);
            if (result.newlyOccupiedHome) {
                player.resetDestroyed(); // return to start after occupying a new home
            }
            return result;
        }

        boolean inWater = isInWater(player);
        boolean inRoad = isInRoad(player);

        if (inRoad) {
            boolean hitVehicle = collisionManager
                    .firstCollision(player, Arrays.asList(levelManager.getVehicles()))
                    .isPresent();
            if (hitVehicle) {
                killAndReset();
                result.lostLife = true;
            }
            return result;
        }

        if (inWater) {
            MovingPlatform platform = (MovingPlatform) collisionManager
                    .firstCollision(player, Arrays.asList(levelManager.getPlatforms()))
                    .orElse(null);

            if (platform == null) {
                killAndReset();
                result.lostLife = true;
            } else {
                player.moveWithPlatform(platform);
                Rectangle2D afterMove = new Rectangle2D(player.getX(), player.getY(),
                        Player.FROG_WIDTH, Player.FROG_COLLISION_HEIGHT);
                if (!FroggerGame.SCREEN_BOUNDS.contains(afterMove)) {
                    killAndReset();
                    result.lostLife = true;
                }
            }
        }
        return result;
    }

    public boolean hasPressedPause() {
        return input.hasPressedPause();
    }

    public void resetPause() {
        input.resetPause();
    }

    public void resetPlayer() {
        player.resetDestroyed();
    }

    public void killAndReset() {
        player.die();
        player.resetDestroyed();
    }

    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    public Player getPlayerClone() {
        return player.clone();
    }

    private boolean isInWater(Player p) {
        double yMid = p.getY() + Player.FROG_COLLISION_HEIGHT / 2.0;
        double waterTop = 2 * Lane.LANE_HEIGHT;
        double waterBottom = 8 * Lane.LANE_HEIGHT;
        return yMid >= waterTop && yMid < waterBottom;
    }

    private boolean isInRoad(Player p) {
        double yMid = p.getY() + Player.FROG_COLLISION_HEIGHT / 2.0;
        double roadTop = 8 * Lane.LANE_HEIGHT;
        double roadBottom = 14 * Lane.LANE_HEIGHT;
        return yMid >= roadTop && yMid < roadBottom;
    }
}
