package frogger.model;

import frogger.display.PlayerListener;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;
import frogger.model.managers.CollisionManager;
import frogger.model.managers.LevelManager;
import frogger.model.managers.PlayerManager;
import javafx.geometry.Rectangle2D;
import ucd.comp2011j.engine.Game;

import java.util.List;

/**
 * Orchestrator: holds basic state (time, lives, score) and implements Game interface.
 * Wires LevelManager + PlayerManager and injects CollisionManager (Tier-1).
 */
public class FroggerGame implements Game {
    public static final int SCREEN_WIDTH = 768;
    public static final int SCREEN_HEIGHT = 512;
    public static final Rectangle2D SCREEN_BOUNDS = new Rectangle2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    public static final double SECONDS_PER_UPDATE = 0.017;
    private static final double BONUS_TIME_SECONDS = 30;

    // Basic game state
    private boolean paused;
    private double timer;
    private int score;
    private int lives;

    // Managers
    private final LevelManager levelManager;
    private final PlayerManager playerManager;
    private final CollisionManager collisionManager;

    public FroggerGame(PlayerListener playerListener) {
        this.levelManager = new LevelManager();
        this.playerManager = new PlayerManager(playerListener);
        this.collisionManager = new CollisionManager();
    }

    @Override
    public int getPlayerScore() {
        return score;
    }

    @Override
    public void updateGame() {
        if (paused) return;

        timer -= SECONDS_PER_UPDATE;
        if (timer <= 0) {
            loseLife();
        }

        levelManager.updateCurrentLevel();

        PlayerManager.Result result = playerManager.update(levelManager, collisionManager);
        if (result.lostLife) {
            loseLife();
        }
        if (result.newlyOccupiedHome) {
            timer += BONUS_TIME_SECONDS;
            score += 500;
        }

        if (levelManager.isLevelFinished() && !isFinalLevel()) {
            moveToNextLevel();
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void checkForPause() {
        if (playerManager.hasPressedPause()) {
            paused = !paused;
            playerManager.resetPause();
        }
    }

    @Override
    public void startNewGame() {
        paused = true;
        score = 0;
        timer = 30;
        levelManager.reset();
        lives = levelManager.getInitialLives();
        playerManager.resetPlayer();
    }

    @Override
    public boolean isLevelFinished() {
        return levelManager.isLevelFinished();
    }

    @Override
    public boolean isPlayerAlive() {
        return playerManager.isPlayerAlive();
    }

    @Override
    public void resetDestroyedPlayer() {
        playerManager.resetPlayer();
    }

    @Override
    public void moveToNextLevel() {
        paused = true;
        levelManager.advance();
        lives = levelManager.getInitialLives();
        playerManager.resetPlayer();
        timer += BONUS_TIME_SECONDS;
    }

    @Override
    public boolean isGameOver() {
        return lives == 0 || (levelManager.isLevelFinished() && isFinalLevel());
    }

    private boolean isFinalLevel() {
        return levelManager.getCurrentLevelNumber() == LevelManager.TOTAL_LEVELS;
    }

    // Accessors
    public int getScreenWidth() { return SCREEN_WIDTH; }
    public int getScreenHeight() { return SCREEN_HEIGHT; }
    public double getGameTime() { return timer; }
    public int getCurrentLevel() { return levelManager.getCurrentLevelNumber(); }
    public int getPlayerLives() { return lives; }

    public List<Home> getHomes() {
        return levelManager.getHomesCopy();
    }

    public Player getPlayer() {
        return playerManager.getPlayerClone();
    }

    public Vehicle[] getVehicles() {
        return levelManager.getVehicles();
    }

    public MovingPlatform[] getPlatforms() {
        return levelManager.getPlatforms();
    }

    private void loseLife() {
        lives = Math.max(0, lives - 1);
        timer += BONUS_TIME_SECONDS;
    }
}