package frogger.display;

import frogger.model.*;
import frogger.model.entities.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * GameRenderer is a static utility class responsible for rendering all non-static visual elements
 * including the vector-based frog model, entity rendering, and HUD overlay system.
 */
public final class GameRenderer {
    // Layout & Shape Constants
    private static final double CORNER_RADIUS = 10;
    private static final int DEFAULT_DISPLAY_UNIT = 25;
    private static final double HEART_TRIANGLE_PEAK_OFFSET = 0.3;

    // Fonts
    private static final Font FONT_HUD = Font.font("Arial", FontWeight.BOLD, 20);
    private static final Font FONT_OVERLAY_PAUSED = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 24);
    private static final Font FONT_OVERLAY_GAMEOVER = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 48);

    // Original Frog Shape Data (90x120 Grid)
    // These arrays define the frog's vector graphics using a custom coordinate system.
    private static final double[] L_EYE = { 10, 0, 20 };
    private static final double[] R_EYE = { 62, 0, 20 };
    private static final double[] BELLY = { 16, 15, 60, 80 };
    private static final double[] L_ARM = { 5, 35,  -15, 40,  -20, 25 };
    private static final double[] R_ARM = { 85, 35, 105, 40, 110, 25 };
    private static final double[] L_LEG = { 10, 105,  0, 130, -10, 85 };
    private static final double[] R_LEG = { 80, 105, 90, 130, 100, 85 };
    private static final double DESIGN_W = 90;
    private static final double DESIGN_H = 120;

    private GameRenderer() {}

    /**
     * Main rendering entry point. Draws all game entities.
     */
    public static void drawEntities(GraphicsContext gc, FroggerGame game) {
        game.getHomes().forEach(h -> drawHome(gc, h));
        game.getPlatforms().forEach(p -> drawPlatform(gc, p));
        game.getVehicles().forEach(v -> drawVehicle(gc, v));
        drawFrog(gc, game.getPlayer());
    }

    /**
     * Draws the player frog with rotation and scaling applied. The frog's original
     * 90x120 design is scaled proportionally to the player's current dimensions,
     * then rotated to face the movement direction.
     */
    public static void drawFrog(GraphicsContext gc, Player player) {
        double w = player.getWidth();
        double h = player.getHeight();
        double cx = player.getX() + w / 2.0;
        double cy = player.getY() + h / 2.0;

        gc.save();
        gc.translate(cx, cy);
        gc.rotate(switch (player.getDirection()) {
            case DOWN -> 180;
            case LEFT -> -90;
            case RIGHT -> 90;
            default -> 0;
        });

        // Calculate uniform scale factor to preserve aspect ratio
        double scale = Math.min(w / DESIGN_W, h / DESIGN_H);
        double drawW = DESIGN_W * scale;
        double drawH = DESIGN_H * scale;

        // Center the drawing locally within the scaled bounds
        gc.translate(-drawW / 2, -drawH / 2);

        drawFrogParts(gc, scale);

        // Apply death effect if needed
        if (!player.isAlive()) {
            drawDeathEffect(gc, 0, 0, w, w);
        }
        gc.restore();
    }

    /**
     * Draws a decorative frog at an arbitrary position (used for menus). Unlike
     * drawFrog(), this method bypasses player state and directly renders at the specified
     * coordinates using the same vector graphics system.
     */
    public static void drawFrogAt(GraphicsContext gc, double x, double y, double w, double h) {
        gc.save();
        gc.translate(x, y);
        double scale = Math.min(w / DESIGN_W, h / DESIGN_H);
        drawFrogParts(gc, scale);
        gc.restore();
    }

    private static void drawFrogParts(GraphicsContext gc, double scale) {
        drawEyes(gc, scale);
        drawLimbs(gc, scale);
        drawBody(gc, scale);
    }

    private static void drawEyes(GraphicsContext gc, double scale) {
        gc.setFill(Color.CRIMSON);
        gc.fillOval(L_EYE[0] * scale, L_EYE[1] * scale, L_EYE[2] * scale, L_EYE[2] * scale);
        gc.fillOval(R_EYE[0] * scale, R_EYE[1] * scale, R_EYE[2] * scale, R_EYE[2] * scale);
    }

    private static void drawBody(GraphicsContext gc, double scale) {
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(0, 0, DESIGN_W * scale, DESIGN_H * scale);

        gc.setFill(Color.YELLOWGREEN);
        gc.fillOval(BELLY[0] * scale, BELLY[1] * scale, BELLY[2] * scale, BELLY[3] * scale);
    }

    private static void drawLimbs(GraphicsContext gc, double scale) {
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(6 * scale);

        strokePoly(gc, L_ARM, scale);
        strokePoly(gc, R_ARM, scale);
        strokePoly(gc, L_LEG, scale);
        strokePoly(gc, R_LEG, scale);
    }

    /**
     * Converts our flat coordinate arrays into the format required by JavaFX's
     * strokePolyline(). Expects pts to contain alternating x,y coordinate pairs.
     */
    private static void strokePoly(GraphicsContext gc, double[] pts, double scale) {
        int n = pts.length / 2;
        double[] xs = new double[n];
        double[] ys = new double[n];

        for (int i = 0; i < n; i++) {
            xs[i] = pts[i * 2] * scale;
            ys[i] = pts[i * 2 + 1] * scale;
        }
        gc.strokePolyline(xs, ys, n);
    }

    private static void drawDeathEffect(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setFill(Color.color(1, 0, 0, 0.25));
        gc.fillOval(x, y, w, h);
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(x, y, x + w, y + h);
        gc.strokeLine(x + w, y, x, y + h);
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(1.5);
        gc.strokeOval(x, y, w, h);
    }

    private static void drawHome(GraphicsContext gc, Home home) {
        gc.setFill(Color.SEAGREEN);
        gc.fillRoundRect(home.getX(), home.getY(), Player.FROG_WIDTH, Player.FROG_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);
        if (home.isOccupied()) {
            // Draw a miniaturized static frog (20% scale) in occupied homes
            gc.save();
            gc.translate(home.getX() + Player.FROG_WIDTH / 5.0, home.getY());
            drawFrogParts(gc, 0.2);
            gc.restore();
        }
    }

    private static void drawPlatform(GraphicsContext gc, MovingPlatform p) {
        if (p instanceof Log log) {
            gc.setFill(Color.SIENNA);
            gc.fillRoundRect(log.getX(), log.getY(), log.getWidth(), log.getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            // Add wood grain line for visual texture
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(log.getX(), log.getY() + 5, log.getWidth() - 20, 2);
        } else if (p instanceof TurtleGroup tg) {
            // Visual safety indicator: green when safe to stand, dark blue when diving
            gc.setFill(tg.isSafeToStand() ? Color.GREEN : Color.DARKBLUE);
            for (int i = 0; i < tg.getSegmentCount(); i++) {
                gc.fillOval(tg.getX() + i * tg.getSegmentWidth(), tg.getY(), tg.getSegmentWidth() - 2, tg.getHeight());
            }
        }
    }

    private static void drawVehicle(GraphicsContext gc, Vehicle v) {
        Color bodyColor = switch (v.getType()) {
            case CAR -> Color.ROYALBLUE;
            case BUS -> Color.YELLOW;
            case TRUCK -> Color.FIREBRICK;
            default -> Color.DARKGRAY;
        };

        // Draw wheels positioned relative to vehicle width
        gc.setFill(Color.BLACK);
        gc.fillOval(v.getX() + 5, v.getY() - 2, 10, v.getHeight() + 4);
        gc.fillOval(v.getX() + v.getWidth() - 15, v.getY() - 2, 10, v.getHeight() + 4);

        // Draw main body
        gc.setFill(bodyColor);
        gc.fillRoundRect(v.getX(), v.getY(), v.getWidth(), v.getHeight(), 8, 8);

        // Add windshield
        gc.setFill(Color.color(1, 1, 1, 0.3));
        gc.fillRect(v.getX() + 10, v.getY() + 5, v.getWidth() - 20, v.getHeight() - 10);
    }

    /**
     * Draws the complete HUD including lives, score, time bar, and pause/game over
     * overlays.
     */
    public static void drawUI(GraphicsContext gc, FroggerGame game) {
        gc.save();
        gc.setFont(FONT_HUD);
        gc.setTextBaseline(VPos.TOP);

        // 1. Lives represented as heart shapes
        int lives = game.getPlayerLives();
        for (int i = 0; i < lives; i++) {
            drawHeart(gc, 10 + i * (DEFAULT_DISPLAY_UNIT - 5), 5, DEFAULT_DISPLAY_UNIT - 5, Color.DARKRED);
        }

        // 2. Level and score displayed in top-right
        gc.setFill(Color.KHAKI);
        gc.setTextAlign(TextAlignment.RIGHT);
        String status = "Lvl " + game.getCurrentLevel() + "  |  Score " + game.getPlayerScore();
        gc.fillText(status, gc.getCanvas().getWidth() - 10, 5);

        // 3. Time bar showing remaining game time
        drawTimeBar(gc, game);

        // 4. Overlay
        drawOverlay(gc, game);

        gc.restore();
    }

    private static void drawTimeBar(GraphicsContext gc, FroggerGame game) {
        // Calculate remaining time as ratio (gameTime ranges 0-100)
        double timeRatio = game.getGameTime() / 100;
        double barWidth = gc.getCanvas().getWidth() * 0.25;
        double barHeight = 14;
        double x = 10;
        double y = gc.getCanvas().getHeight() - barHeight - 10;

        // Draw filled portion of time bar
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(x, y, barWidth * timeRatio, barHeight);

        // Draw time label above bar
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Time: " + (int) Math.ceil(game.getGameTime()), x, y - 18);
    }

    /**
     * Draws a heart shape using two circles and a triangle. The xCoords/yCoords
     * define the triangle peak position using a configurable offset ratio.
     */
    private static void drawHeart(GraphicsContext gc, double centerX, double topY, double size, Color color) {
        gc.setFill(color);
        double circleSize = size / 2;
        // Left and right circles form the top lobes
        gc.fillOval(centerX - size * 0.4, topY, circleSize, circleSize);
        gc.fillOval(centerX + size * 0.4 - circleSize, topY, circleSize, circleSize);
        // Bottom triangle forms the point
        double[] xCoords = {centerX - size * 0.375, centerX + size * 0.375, centerX};
        double[] yCoords = {topY + circleSize * 0.75,
                topY + circleSize * 0.75,
                topY + size * HEART_TRIANGLE_PEAK_OFFSET + circleSize};
        gc.fillPolygon(xCoords, yCoords, 3);
    }

    private static void drawOverlay(GraphicsContext gc, FroggerGame game) {
        if (game.isPaused() || game.isGameOver()) {
            gc.save();
            gc.setFill(Color.GOLDENROD);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setEffect(new DropShadow(5, Color.BLACK)); // Add text shadow for readability

            if (game.isPaused()) {
                gc.setFont(FONT_OVERLAY_PAUSED);
                gc.fillText("Press P to Continue", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
            } else if (game.isGameOver()) {
                gc.setFont(FONT_OVERLAY_GAMEOVER);
                gc.fillText("Game Over", gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);
            }
            gc.restore();
        }
    }
}
