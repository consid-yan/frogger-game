package frogger.display;

import java.util.Arrays;
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

public class GameScreen implements Screen {

    public static final int LANE_HEIGHT = FroggerGame.SCREEN_HEIGHT / 16;
    public static final int LINE_HEIGHT = FroggerGame.SCREEN_WIDTH / 200;
    public static final int DEFAULT_DISPLAY_UNIT = 25;
    public static final double ARC_FACTOR = 0.20;
    public static final double CAB_PROPORTION = 0.25;
    public static final double WHEEL_FACTOR = 0.50;
    private static final double HOME_FROG_SCALE = 0.2;
    private static final double HEART_TRIANGLE_PEAK_OFFSET = 0.3;

    private static final int[] CAR_OFFSETS_X = {0, 1, 4, 7, 9, 9, 0};
    private static final int[] CAR_OFFSETS_Y = {2, 1, 0, 0, 1, 3, 3};
    private static final int[] CAR_WHEEL_OFFSET_X = {1, 6};
    private static final int CAR_WHEEL_OFFSET_Y = 2;
    private static final int CAR_X_MAX = Arrays.stream(CAR_OFFSETS_X).max().orElse(1);
    private static final int CAR_Y_MAX = Arrays.stream(CAR_OFFSETS_Y).max().orElse(1);

    private static final Font FONT_OVERLAY_PAUSED = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 24);
    private static final Font FONT_OVERLAY_GAMEOVER = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 48);
    private static final Font FONT_HUD = Font.font("Arial", FontWeight.BOLD, 20);

    private final Canvas canvas;
    private final FroggerGame displayedGame;

    public GameScreen(FroggerGame game) {
        this.displayedGame = game;
        this.canvas = new Canvas(game.getScreenWidth(), game.getScreenHeight());
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        clearAndDrawBackgroundWater(gc);
        drawRoad(gc);
        drawHomes(gc);
        drawPlatforms(gc, displayedGame.getPlatforms());
        drawVehicles(gc, displayedGame.getVehicles());
        FrogRenderer.drawFrog(gc, displayedGame.getPlayer());
        drawOverlay(gc);
        drawHUD(gc);
        gc.restore();
    }

    private void clearAndDrawBackgroundWater(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight() / 2.0);
    }

    private void drawRoad(GraphicsContext gc) {
        drawRoadSurface(gc);
        drawSoil(gc);
        drawLaneDividers(gc);
        drawDashedLaneMarkers(gc);
        drawSandStrip(gc);
    }

    private void drawRoadSurface(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, canvas.getHeight() / 2.0, canvas.getWidth(), canvas.getHeight() / 2.0);
    }

    private void drawSoil(GraphicsContext gc) {
        gc.setFill(Color.SIENNA);
        gc.fillRect(0, 14 * LANE_HEIGHT, canvas.getWidth(), 2 * LANE_HEIGHT);
    }

    private void drawLaneDividers(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillRect(0, 12 * LANE_HEIGHT, canvas.getWidth(), LINE_HEIGHT);
        gc.fillRect(0, 10 * LANE_HEIGHT, canvas.getWidth(), LINE_HEIGHT);
    }

    private void drawDashedLaneMarkers(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        for (int lane = 9; lane < 15; lane += 2) {
            double y = lane * LANE_HEIGHT;
            for (double x = 0; x < canvas.getWidth(); x += 2 * DEFAULT_DISPLAY_UNIT) {
                gc.fillRect(x, y, DEFAULT_DISPLAY_UNIT, LINE_HEIGHT);
            }
        }
    }

    private void drawSandStrip(GraphicsContext gc) {
        gc.setFill(Color.PEACHPUFF);
        gc.fillRect(0, canvas.getHeight() / 2.0, canvas.getWidth(), LINE_HEIGHT);
    }

    private void drawHomes(GraphicsContext gc) {
        gc.save();
        for (Home home : displayedGame.getHomes()) {
            gc.setFill(Color.SEAGREEN);
            int width = Player.FROG_WIDTH;
            int height = Player.FROG_HEIGHT;
            gc.fillRoundRect(home.getX(), home.getY(), width, height, ARC_FACTOR * width, ARC_FACTOR * height);
            if (home.isOccupied()) {
                FrogRenderer.drawFrog(gc, home.getX() + width / 5.0, home.getY(), HOME_FROG_SCALE);
            }
        }
        gc.restore();
    }

    private void drawPlatforms(GraphicsContext gc, List<MovingPlatform> platforms) {
        gc.save();
        for (MovingPlatform p : platforms) {
            if (p instanceof TurtleGroup tg) {
                drawTurtleGroup(gc, tg);
            } else if (p instanceof Log log) {
                drawLog(gc, log);
            }
        }
        gc.restore();
    }

    public void drawVehicles(GraphicsContext gc, List<Vehicle> vehicles) {
        gc.save();
        for (Vehicle v : vehicles) {
            switch (v.getType()) {
                case CAR -> drawCar(gc, v);
                case BUS -> drawBus(gc, v);
                case TRUCK -> drawTruck(gc, v);
                default -> { }
            }
        }
        gc.restore();
    }

    private void drawCar(GraphicsContext gc, Vehicle v) {
        double xFactor = (double) v.getWidth() / CAR_X_MAX;
        double yFactor = (double) v.getHeight() / CAR_Y_MAX;
        double[] xPoints = convertedPoints(v.getX(), CAR_OFFSETS_X, xFactor, CAR_X_MAX, v.getDirection() == Direction.RIGHT);
        double[] yPoints = convertedPoints(v.getY(), CAR_OFFSETS_Y, yFactor, CAR_Y_MAX, false);
        gc.setFill(Color.ROYALBLUE);
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
        gc.setStroke(Color.ROYALBLUE.darker());
        gc.strokePolygon(xPoints, yPoints, xPoints.length);
        double[] xWheel = convertedPoints(v.getX(), CAR_WHEEL_OFFSET_X, xFactor, CAR_X_MAX, false);
        double yWheel = v.getY() + CAR_WHEEL_OFFSET_Y * yFactor;
        double wheelD = v.getHeight() * WHEEL_FACTOR;
        gc.setFill(Color.BLACK);
        gc.fillOval(xWheel[0], yWheel, wheelD, wheelD);
        gc.fillOval(xWheel[1], yWheel, wheelD, wheelD);
    }

    private void drawBus(GraphicsContext gc, Vehicle v) {
        drawStrokeRect(gc, v.getX(), v.getY(), v.getWidth(), v.getHeight(), Color.YELLOW, Color.ORANGE);
        gc.setFill(Color.LIGHTBLUE);
        for (double startX = v.getX(); startX < v.getX() + v.getWidth(); startX += DEFAULT_DISPLAY_UNIT) {
            gc.fillRect(startX, v.getY() + DEFAULT_DISPLAY_UNIT / 5.0, DEFAULT_DISPLAY_UNIT / 2.0, DEFAULT_DISPLAY_UNIT / 2.0);
        }
        double wheelD = v.getHeight() * WHEEL_FACTOR;
        double yWheel = v.getY() + v.getHeight() - (wheelD / 2);
        gc.setFill(Color.BLACK);
        gc.fillOval(v.getX() + wheelD, yWheel, wheelD, wheelD);
        gc.fillOval(v.getX() + v.getWidth() - 2 * wheelD, yWheel, wheelD, wheelD);
    }

    private void drawTruck(GraphicsContext gc, Vehicle v) {
        double cabWidth = v.getWidth() * CAB_PROPORTION;
        double cargoWidth = v.getWidth() - cabWidth;
        drawStrokeRect(gc, v.getX(), v.getY(), cabWidth, v.getHeight(), Color.FIREBRICK, Color.BROWN);
        double windowW = cabWidth * 0.45;
        double windowH = v.getHeight() * 0.45;
        double windowX = v.getX() + cabWidth * 0.15;
        double windowY = v.getY() + v.getHeight() * 0.15;
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRoundRect(windowX, windowY, windowW, windowH, windowW * 0.3, windowH * 0.3);
        gc.setStroke(Color.LIGHTBLUE.darker());
        gc.strokeRoundRect(windowX, windowY, windowW, windowH, windowW * 0.3, windowH * 0.3);
        drawStrokeRect(gc, v.getX() + cabWidth, v.getY(), cargoWidth, v.getHeight(), Color.SILVER, Color.GRAY);
        double wheelD = v.getHeight() * WHEEL_FACTOR;
        double yWheel = v.getY() + v.getHeight() - (wheelD / 2);
        gc.setFill(Color.BLACK);
        double frontWheelX = v.getX() + wheelD * 0.8;
        gc.fillOval(frontWheelX, yWheel, wheelD, wheelD);
        double rearBaseX = v.getX() + v.getWidth() - 2.2 * wheelD;
        gc.fillOval(rearBaseX, yWheel, wheelD, wheelD);
        gc.fillOval(rearBaseX + wheelD * 0.6, yWheel, wheelD, wheelD);
    }

    private double[] convertedPoints(double origin, int[] offsets, double factor, double scale, boolean inverse) {
        double[] pts = new double[offsets.length];
        if (inverse) {
            for (int i = 0; i < offsets.length; i++) {
                pts[i] = origin + (scale - offsets[i]) * factor;
            }
        } else {
            for (int i = 0; i < offsets.length; i++) {
                pts[i] = origin + offsets[i] * factor;
            }
        }
        return pts;
    }

    private void drawStrokeRect(GraphicsContext gc, double x, double y, double width, double height,
                                Color fill, Color stroke) {
        double arcW = width * GameScreen.ARC_FACTOR;
        double arcH = height * GameScreen.ARC_FACTOR;
        gc.setFill(fill);
        gc.fillRoundRect(x, y, width, height, arcW, arcH);
        gc.setStroke(stroke);
        gc.strokeRoundRect(x, y, width, height, arcW, arcH);
    }

    private void drawTurtleGroup(GraphicsContext gc, TurtleGroup group) {
        if (group.isSafeToStand()) {
            gc.setFill(Color.GREEN);
            for (int i = 0; i < group.getSegmentCount(); i++) {
                gc.fillOval(group.getX() + i * group.getSegmentWidth(), group.getY(),
                        group.getSegmentWidth() - 1, group.getHeight() - 5);
            }
            gc.setFill(Color.LAWNGREEN);
            gc.fillOval(group.getX() - 5, group.getY() + group.getHeight() / 4.0, 5, 10);
        } else {
            gc.setFill(Color.DODGERBLUE.darker());
            gc.fillOval(group.getX(), group.getY(),
                    group.getSegmentWidth() * group.getSegmentCount(), LANE_HEIGHT);
        }
    }

    private void drawLog(GraphicsContext gc, Log log) {
        gc.setFill(Color.SIENNA);
        gc.fillRoundRect(log.getX(), log.getY(), log.getWidth(), log.getHeight(),
                ARC_FACTOR * log.getWidth(), ARC_FACTOR * log.getHeight());
        gc.setFill(Color.SADDLEBROWN);
        for (int row = 1; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gc.fillRect(log.getX() + col * log.getWidth() / 5.0 + 5,
                        log.getY() + 5 * row, log.getWidth() / 9.0, 1.2);
            }
        }
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillOval(log.getX() + log.getWidth() - log.getHeight() / 2.0 + 5,
                log.getY(), log.getHeight() / 2.0, log.getHeight());
    }

    private void drawHUD(GraphicsContext gc) {
        gc.save();
        gc.setFont(FONT_HUD);
        gc.setTextBaseline(VPos.TOP);
        drawLives(gc);
        drawLevelScore(gc);
        drawTimeBar(gc);
        gc.restore();
    }

    private void drawLives(GraphicsContext gc) {
        int lives = displayedGame.getPlayerLives();
        for (int i = 0; i < lives; i++) {
            drawHeart(gc, 10 + i * (DEFAULT_DISPLAY_UNIT - 5), 5,
                    DEFAULT_DISPLAY_UNIT - 5, Color.DARKRED);
        }
    }

    private void drawLevelScore(GraphicsContext gc) {
        gc.setFill(Color.KHAKI);
        gc.setTextAlign(TextAlignment.RIGHT);
        String status = "Lvl " + displayedGame.getCurrentLevel() + "  |  Score " + displayedGame.getPlayerScore();
        gc.fillText(status, canvas.getWidth() - 10, 5);
    }

    private void drawTimeBar(GraphicsContext gc) {
        double timeRatio = displayedGame.getGameTime() / 100.0;
        double barWidth = canvas.getWidth() * 0.25;
        double barHeight = 14;
        double x = 10;
        double y = canvas.getHeight() - barHeight - 10;
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(x, y, barWidth * timeRatio, barHeight);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Time: " + (int) Math.floor(displayedGame.getGameTime()), x, y - 18);
    }

    private void drawHeart(GraphicsContext gc, double centerX, double topY,
                           double size, Color color) {
        gc.setFill(color);
        double circleSize = size / 2.0;
        gc.fillOval(centerX - size * 0.4, topY, circleSize, circleSize);
        gc.fillOval(centerX + size * 0.4 - circleSize, topY, circleSize, circleSize);
        double[] xCoords = {centerX - size * 0.375, centerX + size * 0.375, centerX};
        double[] yCoords = {topY + circleSize * 0.75,
                topY + circleSize * 0.75,
                topY + size * HEART_TRIANGLE_PEAK_OFFSET + circleSize};
        gc.fillPolygon(xCoords, yCoords, 3);
    }

    private void drawOverlay(GraphicsContext gc) {
        if (!displayedGame.isPaused() && !displayedGame.isGameOver()) return;
        gc.save();
        gc.setFill(Color.GOLDENROD);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setEffect(new DropShadow(5, Color.BLACK));
        if (displayedGame.isPaused()) {
            gc.setFont(FONT_OVERLAY_PAUSED);
            gc.fillText("Press P to Continue", canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
        } else if (displayedGame.isGameOver()) {
            gc.setFont(FONT_OVERLAY_GAMEOVER);
            gc.fillText("Game Over", canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
        }
        gc.restore();
    }
}
