package frogger.display;

import java.awt.Point;
import java.util.List;
import java.util.function.ToDoubleFunction;

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

public class GameScreen implements Screen {

    // Frog polygon definitions
    private static final Point[] FROG_BODY_SHAPE = new Point[]{
            new Point(2, 0), new Point(6, 0),
            new Point(7, 1), new Point(8, 3), new Point(7, 4),
            new Point(7, 6), new Point(8, 7), new Point(6, 8),
            new Point(4, 7),
            new Point(2, 8), new Point(0, 7), new Point(1, 6),
            new Point(1, 4), new Point(0, 3), new Point(1, 1)
    };
    private static final Point[] FROG_LEFT_EYE = new Point[]{
            new Point(1, 0), new Point(3, 0), new Point(3, -1), new Point(2, -1)
    };
    private static final Point[] FROG_RIGHT_EYE = new Point[]{
            new Point(5, 0), new Point(7, 0), new Point(6, -1), new Point(5, -1)
    };

    private final Canvas displayingCanvas;
    private FroggerGame displayedGame;

    public GameScreen(FroggerGame game) {
        displayedGame = game;
        displayingCanvas = new Canvas(displayedGame.getScreenWidth(), displayedGame.getScreenHeight());
    }

    @Override
    public Canvas getCanvas() {
        return displayingCanvas;
    }

    // ---------------- Utility drawing helpers ----------------

    private double[] adjustCoordinates(Point[] shape, ToDoubleFunction<Point> coordExtractor,
                                       int pivotValue, double scale) {
        double[] adjustedCoords = new double[shape.length];
        for (int i = 0; i < shape.length; i++) {
            adjustedCoords[i] = pivotValue + coordExtractor.applyAsDouble(shape[i]) * scale;
        }
        return adjustedCoords;
    }

    private void drawScaledPolygon(GraphicsContext gc, Point[] shape, Color color,
                                   int pivotX, int pivotY, double scale) {
        double[] xPoints = adjustCoordinates(shape, Point::getX, pivotX, scale);
        double[] yPoints = adjustCoordinates(shape, Point::getY, pivotY, scale);
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, shape.length);
    }

    // ---------------- Background & static elements ----------------

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
            gc.fillRect(0, 14 * Lane.LANE_HEIGHT, displayedGame.getScreenWidth(), 2 * Lane.LANE_HEIGHT);

            // Lane divider lines
            gc.setFill(Color.ORANGE);
            gc.fillRect(0, 12 * Lane.LANE_HEIGHT, displayedGame.getScreenWidth(), 3);
            gc.fillRect(0, 10 * Lane.LANE_HEIGHT, displayedGame.getScreenWidth(), 3);

            // Dashed lane marks
            gc.setFill(Color.WHITE);
            for (int lane = 9; lane < 15; lane += 2) {
                double y = lane * Lane.LANE_HEIGHT;
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
                gc.setFill(Color.GREEN);
                gc.fillRect(home.getX(), home.getY(), Player.FROG_WIDTH, Player.FROG_WIDTH);
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

    private void drawFrog(GraphicsContext gc, Player player) {
        if (!player.isAlive()) return;
        gc.save();
        try {
            double pX = player.getX();
            double pY = player.getY();
            Direction dir = player.getDirection();

            double centerX = pX + Player.FROG_WIDTH / 2.0;
            double centerY = pY + Player.FROG_WIDTH / 2.0;
            gc.translate(centerX, centerY);
            switch (dir) {
                case UP: gc.rotate(0); break;
                case DOWN: gc.rotate(180); break;
                case LEFT: gc.rotate(-90); break;
                case RIGHT: gc.rotate(90); break;
            }
            gc.translate(-centerX, -centerY);

            drawScaledPolygon(gc, FROG_BODY_SHAPE, Color.GREEN, (int) pX, (int) pY, Player.FROG_SCALE);
            drawScaledPolygon(gc, FROG_LEFT_EYE, Color.CRIMSON, (int) pX, (int) pY, Player.FROG_SCALE);
            drawScaledPolygon(gc, FROG_RIGHT_EYE, Color.CRIMSON, (int) pX, (int) pY, Player.FROG_SCALE);
        } finally {
            gc.restore();
        }
    }

    private void drawVehicles(GraphicsContext gc, Vehicle[] vehicles) {
        gc.save();
        try {
            gc.setFill(Color.FIREBRICK);
            for (Vehicle v : vehicles) {
                if (v == null) continue;
                // Basic representation: a rectangle; could add headlights or direction arrow
                gc.fillRect(v.getX(), v.getY(), v.getWidth(), Lane.LANE_HEIGHT);
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
                    // Assume log or other platform
                    boolean left = p.getDirection() == Direction.LEFT;
                    gc.setFill(left ? Color.SADDLEBROWN : Color.BURLYWOOD);
                    gc.fillRoundRect(p.getX(), p.getY(),
                            p.getWidth(), Lane.LANE_HEIGHT,
                            8, 8);
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

            // Choose color based on standability (diving state)
            Color shellColor = group.isSafeToStand() ? Color.SEAGREEN : Color.DARKSLATEGRAY;
            Color rimColor = group.isSafeToStand() ? Color.DARKGREEN : Color.BLACK;

            for (int i = 0; i < count; i++) {
                double x = baseX + i * segW;
                gc.setFill(shellColor);
                gc.fillOval(x + 4, y + 4, segW - 8, Lane.LANE_HEIGHT - 8);
                gc.setStroke(rimColor);
                gc.setLineWidth(2);
                gc.strokeOval(x + 4, y + 4, segW - 8, Lane.LANE_HEIGHT - 8);
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
        gc.save();
        try {
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
        } finally {
            gc.setEffect(null);
            gc.restore();
        }
    }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();
        gc.save();
        try {
            gc.setGlobalAlpha(1.0);
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
            drawFrog(gc, displayedGame.getPlayer());

            // HUD + overlay
            drawHUD(gc, displayedGame);
            drawOverlay(gc);
        } finally {
            gc.restore();
        }
    }
}
