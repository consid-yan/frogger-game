package frogger.model.managers;

import frogger.display.GameScreen;
import frogger.display.PlayerListener;
import frogger.model.Direction;
import frogger.model.FroggerGame;
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

    private int furthestRowReached;

    public PlayerManager(PlayerListener in) {
        input = in;
        player = new Player();
        initProgressTracker();
    }

    private void initProgressTracker() {
        furthestRowReached = currentRow(player); // starting row
    }

    public static class Result {
        public boolean lostLife;
        public boolean enteredHome;
        public boolean newlyOccupiedHome;
        public int forwardPoints;
    }

    /**
     * Update player based on input and environment. Uses CollisionManager for all intersection checks.
     */
    public Result update(LevelManager levelManager, CollisionManager collisionManager) {
        Result result = new Result();

        // Input and movement
        Direction dir = input.getAndClearPressedDirection();
        if (dir != null) {
            int beforeRow = currentRow(player);
            player.move(dir);
            int afterRow = currentRow(player);

            // Forward progress: moving UP reduces row index.
            if (dir == Direction.UP && afterRow < beforeRow) {
                // Award only if surpassing previous furthest (smallest) row.
                if (afterRow < furthestRowReached) {
                    // Number of new rows surpassed (usually 1, but guard for larger jumps)
                    int rowsAdvanced = furthestRowReached - afterRow;
                    result.forwardPoints = rowsAdvanced * 150;
                    furthestRowReached = afterRow;
                }
            }
        }

        LevelManager.HomeEntry entry = levelManager.handleHomeEntry(player, collisionManager);
        if (entry != LevelManager.HomeEntry.NONE) {
            result.enteredHome = true;
            result.newlyOccupiedHome = (entry == LevelManager.HomeEntry.NEWLY_OCCUPIED);
            if (result.newlyOccupiedHome) {
                // Reset frog and progress tracker for next frog
                player.resetDestroyed();
                player.die();
            }
            return result;
        }

        if (player.getY() > FroggerGame.SCREEN_HEIGHT / 2.0) {
            boolean hitVehicle = collisionManager
                    .firstCollision(player, Arrays.asList(levelManager.getVehicles()))
                    .isPresent();
            if (hitVehicle) {
                player.die();
                result.lostLife = true;
            }
            return result;
        }
        else {
            MovingPlatform platform = (MovingPlatform) collisionManager
                    .firstCollision(player, Arrays.asList(levelManager.getPlatforms()))
                    .orElse(null);

            if (platform == null || !platform.isSafeToStand()) {
                player.die();
                result.lostLife = true;
            } else {
                player.moveWithPlatform(platform);
                Rectangle2D afterMove = new Rectangle2D(player.getX(), player.getY(),
                        Player.FROG_WIDTH, Player.FROG_WIDTH);
                if (!FroggerGame.SCREEN_BOUNDS.contains(afterMove)) {
                    player.die();
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
        initProgressTracker();
    }

    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    public Player getPlayerClone() {
        return player.clone();
    }

    private int currentRow(Player player) {
        return (int) Math.floor(player.getY() / GameScreen.LANE_HEIGHT);
    }
}
