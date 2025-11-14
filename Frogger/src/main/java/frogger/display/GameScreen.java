package frogger.display;

import java.util.List;

import frogger.model.*;
import frogger.model.entities.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ucd.comp2011j.engine.Screen;

/**
 * GameScreen simplified: delegates frog rendering to FrogRenderer.
 * Removed the duplicated polygon-based frog and death code in favour of the shared renderer.
 */
public class GameScreen implements Screen {
    public static final int LANE_HEIGHT = FroggerGame.SCREEN_HEIGHT / 16;

    private final Canvas displayingCanvas;
    private final FroggerGame displayedGame;

    public GameScreen(FroggerGame game) {
        displayedGame = game;
        displayingCanvas = new Canvas(displayedGame.getScreenWidth(), displayedGame.getScreenHeight());
    }

    @Override
    public Canvas getCanvas() {
        return displayingCanvas;
    }

    private void drawRoad(GraphicsContext gc) {
        gc.save();
        try {
            // Road surface (lower half)
            gc.setFill(Color.GRAY);
            gc.fillRect(0, displayedGame.getScreenHeight() / 2.0,
                    displayedGame.getScreenWidth(),
                    displayedGame.getScreenHeight() / 2.0);

            // Bottom start area pavement
            gc.setFill(Color.SIENNA);
            gc.fillRect(0, 14 * LANE_HEIGHT, displayedGame.getScreenWidth(), 2 * LANE_HEIGHT);

            // Lane divider lines
            gc.setFill(Color.ORANGE);
            gc.fillRect(0, 12 * LANE_HEIGHT, displayedGame.getScreenWidth(), 3);
            gc.fillRect(0, 10 * LANE_HEIGHT, displayedGame.getScreenWidth(), 3);

            // Dashed lane marks
            gc.setFill(Color.WHITE);
            for (int lane = 9; lane < 15; lane += 2) {
                double y = lane * LANE_HEIGHT;
                for (int x = 0; x < displayedGame.getScreenWidth(); x += 50) {
                    gc.fillRect(x, y, 25, 3);
                }
            }

            // Small highlight bar separating water and road
            gc.setFill(Color.PEACHPUFF);
            gc.fillRect(0, displayedGame.getScreenHeight() / 2.0,
                    displayedGame.getScreenWidth(),
                    Player.FROG_WIDTH / 4.0);
        } finally {
            gc.restore();
        }
    }

    private void drawHomes(GraphicsContext gc) {
        gc.save();
        try {
            List<Home> homes = displayedGame.getHomes();
            for (Home home : homes) {
                gc.setFill(Color.SEAGREEN);
                gc.fillRoundRect(home.getX(), home.getY(), Player.FROG_WIDTH, Player.FROG_WIDTH, 8, 8);
                if (home.isOccupied()) {
                    gc.setFill(Color.DARKGREEN);
                    gc.fillOval(home.getX() + 4, home.getY() + 4,
                            Player.FROG_WIDTH - 8, Player.FROG_WIDTH - 8);
                }
            }
        } finally {
            gc.restore();
        }
    }

    private void drawVehicles(GraphicsContext gc, Vehicle[] vehicles) {
        gc.save();
        try {
            for (Vehicle v : vehicles) {
                if (v == null) continue;

                double x = v.getX();
                double y = v.getY();
                double w = v.getWidth();
                double h = LANE_HEIGHT; // use lane height so cars fit the lane

                // Car body: occupy bottom ~60% of the lane and be slightly rounded
                double bodyH = h * 0.60;
                double bodyY = y + (h - bodyH); // align body to bottom of lane
                double arc = Math.max(6.0, bodyH * 0.3);

                gc.setFill(Color.web("#b22222")); // main car color (change if you want)
                gc.fillRoundRect(x, bodyY, w, bodyH, arc, arc);

                // Outline the body for a bit of depth
                gc.setStroke(Color.color(0.2, 0, 0));
                gc.setLineWidth(Math.max(1.0, bodyH * 0.06));
                gc.strokeRoundRect(x, bodyY, w, bodyH, arc, arc);

                // Wheels (two), positioned under the body
                double wheelSize = bodyH * 0.5;
                double wheelY = bodyY + bodyH - (wheelSize * 0.6); // slightly tucked under body
                gc.setFill(Color.BLACK);
                gc.fillOval(x + w * 0.12, wheelY, wheelSize, wheelSize);      // front wheel
                gc.fillOval(x + w * 0.72, wheelY, wheelSize, wheelSize);      // rear wheel
            }
        } finally {
            gc.restore();
        }
    }

    private void drawPlatforms(GraphicsContext gc, MovingPlatform[] platforms) {
        gc.save();
        try {
            for (MovingPlatform p : platforms) {
                if (p == null) continue;

                if (p instanceof TurtleGroup) {
                    drawTurtleGroup(gc, (TurtleGroup) p);
                } else {
                    gc.setFill(Color.SIENNA);
                    gc.fillRoundRect(p.getX(), p.getY(), p.getWidth(), p.getHeight(), 8, 8);
                    gc.setFill(Color.SADDLEBROWN);
                    for (int i = 1; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            gc.fillRect(p.getX() + j * p.getWidth() / 5.0 + 5, p.getY() + 5 * i,
                                    p.getWidth() / 9.0, 1.2);
                        }
                    }
                }
            }
        } finally {
            gc.restore();
        }
    }

    private void drawTurtleGroup(GraphicsContext gc, TurtleGroup group) {
        gc.save();
        try {
            int count = group.getSegmentCount();
            int segW = group.getSegmentWidth();
            double baseX = group.getX();
            double y = group.getY();

            Color shellColor = group.isSafeToStand() ? Color.SEAGREEN : Color.DARKSLATEGRAY;
            Color rimColor = group.isSafeToStand() ? Color.DARKGREEN : Color.BLACK;

            for (int i = 0; i < count; i++) {
                double x = baseX + i * segW;
                gc.setFill(shellColor);
                gc.fillOval(x + 4, y + 4, segW, Player.FROG_HEIGHT);
                gc.setStroke(rimColor);
                gc.setLineWidth(2);
                gc.strokeOval(x + 4, y + 4, segW, Player.FROG_WIDTH);
            }
        } finally {
            gc.restore();
        }
    }

    private void drawHUD(GraphicsContext gc, FroggerGame game) {
        gc.save();
        try {
            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.KHAKI);
            gc.setTextBaseline(VPos.TOP);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));

            // Lives (left)
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("Lives: " + game.getPlayerLives(), 0, 0);

            // Level & Time (center)
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("Level " + game.getCurrentLevel() + " | Time: " + (int) Math.ceil(game.getGameTime()),
                    displayedGame.getScreenWidth() / 2.0, 0);

            // Score (right)
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText("Score: " + game.getPlayerScore(), displayedGame.getScreenWidth(), 0);
        } finally {
            gc.restore();
        }
    }

    private void drawOverlay(GraphicsContext gc) {
        gc.setFill(Color.GOLDENROD);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setEffect(new DropShadow(5, Color.BLACK));

        boolean paused = displayedGame.isPaused();
        boolean playerAlive = displayedGame.isPlayerAlive();
        boolean levelFinished = displayedGame.isLevelFinished();
        int lives = displayedGame.getPlayerLives();

        if ((paused || !playerAlive) && lives > 0 && !levelFinished) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 24));
            gc.fillText("Press P to Continue", displayedGame.getScreenWidth() / 2.0,
                    displayedGame.getScreenHeight() / 2.0);
        } else if (!playerAlive && lives == 0) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 48));
            gc.fillText("Game Over", displayedGame.getScreenWidth() / 2.0,
                    displayedGame.getScreenHeight() / 2.0);
        }
    }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0, 0, displayedGame.getScreenWidth(), displayedGame.getScreenHeight());

        // Background (water area top half)
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, displayedGame.getScreenWidth(), displayedGame.getScreenHeight());

        // Static environment
        drawRoad(gc);
        drawHomes(gc);

        // Dynamic entities
        drawPlatforms(gc, displayedGame.getPlatforms());
        drawVehicles(gc, displayedGame.getVehicles());

        // Frog
        FrogRenderer.drawFrog(gc, displayedGame.getPlayer());

        // HUD + overlay
        drawHUD(gc, displayedGame);
        drawOverlay(gc);
        gc.restore();
    }
}
