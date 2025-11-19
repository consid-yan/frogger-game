package frogger.model;

import frogger.display.GameScreen;
import frogger.display.PlayerListener;
import frogger.model.entities.MovingPlatform;
import frogger.model.entities.Vehicle;
import javafx.geometry.Rectangle2D;
import ucd.comp2011j.engine.Game;

import java.util.ArrayList;
import java.util.List;

public class FroggerGame implements Game {
    // Game configs
    public static final int SCREEN_WIDTH = 768;
    public static final int SCREEN_HEIGHT = 512;
    public static final Rectangle2D SCREEN_BOUNDS = new Rectangle2D(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    private static final double SECONDS_PER_UPDATE = 0.017;
    private static final double BASE_TIME = 30;
    private static final double BONUS_TIME = 30;
    private static final int TOTAL_LEVELS = 4;
    private static final int NO_HOMES = 5;
    private static final double HOME_START_X = 105;

    // Basic game state
    private final PlayerListener input;
    private int currentLevel;
    private boolean paused;
    private boolean deathPaused;
    private double timer;
    private int score;
    private int lives;
    private int furthestRowReached;

    // Game Objects
    private final CollisionManager collisionManager = new CollisionManager();
    private final List<Home> homes = new ArrayList<>();
    private final Level[] levels = new Level[TOTAL_LEVELS];
    private Player player;

    public FroggerGame(PlayerListener playerListener) {
        input = playerListener;
    }

    private void initializeHomes() {
        homes.clear();
        for (int i = 0; i < NO_HOMES; i++) {
            homes.add(new Home(i * FroggerGame.SCREEN_WIDTH / 6.0 + HOME_START_X, GameScreen.LANE_HEIGHT));
        }
    }

    private void updateProgressTracker() {
        furthestRowReached = currentRow(player);
    }

    private int currentRow(Player player) {
        return (int) Math.floor(player.getY() / GameScreen.LANE_HEIGHT);
    }

    @Override
    public int getPlayerScore() {
        return score;
    }

    @Override
    public void updateGame() {
        if (!paused) {
            timer -= SECONDS_PER_UPDATE;
            if (timer <= 0) loseLife();
            levels[currentLevel].update();

            moveDetect(input.getAndClearPressedDirection());

            if (player.getY() > SCREEN_HEIGHT / 2.0) {
                if (collisionManager.firstCollision(player, getVehicles()) != null) {
                    loseLife();
                }
            } else {
                MovingPlatform platform = (MovingPlatform) collisionManager.firstCollision(player, getPlatforms());
                if (platform == null || !platform.isSafeToStand()) {
                    loseLife();
                } else {
                    player.moveWithPlatform(platform);
                    if (!SCREEN_BOUNDS.contains(player.getHitBox())) {
                        loseLife();
                    }
                }
            }

            if (isLevelFinished() && !isFinalLevel()) {
                moveToNextLevel();
            }
        }
    }

    private void moveDetect(Direction direction) {
        if (direction != null) {
            player.move(direction);
            if (direction == Direction.UP) {
                int rowAfterMove = currentRow(player);
                if (rowAfterMove < furthestRowReached) {
                    score += 150;
                    updateProgressTracker();
                }

                Home enteredHome = (Home) collisionManager.firstCollision(player, homes);
                if (enteredHome != null) {
                    enteredHome.occupied();
                    timer += BONUS_TIME;
                    score += 500;
                    resetDestroyedPlayer();
                    player.die();
                }
            }
        }
    }

    private void loseLife() {
        player.die();
        lives = Math.max(0, lives - 1);
        timer += BASE_TIME;
        paused = true;
        deathPaused = true;
    }

    private boolean isFinalLevel() {
        return currentLevel == TOTAL_LEVELS - 1;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void checkForPause() {
        if (input.hasPressedPause()) {
            if (deathPaused) {
                resetDestroyedPlayer();
                deathPaused = false;
                paused = false;
            } else {
                paused = !paused;
            }
            input.resetPause();
        }
    }

    @Override
    public void startNewGame() {
        player = new Player();
        paused = true;
        deathPaused = false;
        score = 0;
        timer = BASE_TIME;
        initializeLevels();
        lives = levels[currentLevel].getInitialLives();
        initializeHomes();
        resetDestroyedPlayer();
    }

    public void initializeLevels() {
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels[i] = new Level(0.9 + i * 0.05, 185 - 10 * i, 5 - (i % 2));
            levels[i].initializeLanes();
        }
        currentLevel = 0;
    }

    @Override
    public boolean isLevelFinished() {
        for (Home home : homes) {
            if (!home.isOccupied()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isPlayerAlive() {
        return player.isAlive();
    }

    @Override
    public void resetDestroyedPlayer() {
        player.resetDestroyed();
        updateProgressTracker();
    }

    @Override
    public void moveToNextLevel() {
        paused = true;
        score += 10000 + 250 * (int)Math.floor(timer);
        deathPaused = false;
        levelAdvance();
        lives = levels[currentLevel].getInitialLives();
        resetDestroyedPlayer();
        timer = BASE_TIME;
    }

    private void levelAdvance() {
        if (!isFinalLevel()) {
            levels[currentLevel].clearLanes();
            currentLevel++;
            initializeHomes();
        }
    }

    @Override
    public boolean isGameOver() {
        return lives == 0 || (isLevelFinished() && isFinalLevel());
    }

    // Accessors
    public int getScreenWidth() { return SCREEN_WIDTH; }

    public int getScreenHeight() { return SCREEN_HEIGHT; }

    public double getGameTime() { return timer; }

    public String getCurrentLevel() { return switch (currentLevel) {
        case 0 -> "EASY";
        case 1 -> "NORMAL";
        case 2 -> "HARD";
        case 3 -> "EXPERT";
        default -> "UNKNOWN";
    }; }

    public int getPlayerLives() { return lives; }

    public List<Home> getHomes() {
        return new ArrayList<>(homes);
    }

    public Player getPlayer() {
        return player.clone();
    }

    public List<Vehicle> getVehicles() {
        return levels[currentLevel].getVehicles();
    }

    public List<MovingPlatform> getPlatforms() {
        return levels[currentLevel].getPlatforms();
    }
}