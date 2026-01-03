package frogger.display;

import frogger.model.FroggerGame;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ucd.comp2011j.engine.Screen;

public class GameScreen implements Screen {

    public static final int LANE_HEIGHT = FroggerGame.SCREEN_HEIGHT / 16;
    public static final int LINE_HEIGHT = FroggerGame.SCREEN_WIDTH / 200;

    private final Canvas canvas;
    private final FroggerGame displayedGame;

    public GameScreen(FroggerGame game) {
        displayedGame = game;
        canvas = new Canvas(game.getScreenWidth(), game.getScreenHeight());
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 1. Background
        drawBackground(gc);

        // 2. Dynamic Elements
        GameRenderer.drawEntities(gc, displayedGame);

        // 3. UI Overlay
        GameRenderer.drawUI(gc, displayedGame);
    }

    private void drawBackground(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.clearRect(0, 0, width, height);

        // Water
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, width, height / 2.0);

        // Road
        gc.setFill(Color.GRAY);
        gc.fillRect(0, height / 2.0, width, height / 2.0);

        // Median & Bank
        gc.setFill(Color.PEACHPUFF);
        gc.fillRect(0, height / 2.0, width, LINE_HEIGHT); // Median
        gc.setFill(Color.SIENNA);
        gc.fillRect(0, 14 * LANE_HEIGHT, width, 2 * LANE_HEIGHT); // Start Bank

        // Lane Dividers
        gc.setFill(Color.WHITE);
        for (int i = 9; i < 14; i += 1) {
            drawDashedLine(gc, i * LANE_HEIGHT, width);
        }
        gc.setFill(Color.ORANGE);
        gc.fillRect(0, 11 * LANE_HEIGHT, width, LINE_HEIGHT);
    }

    private void drawDashedLine(GraphicsContext gc, double y, double width) {
        for (double x = 0; x < width; x += 50) {
            gc.fillRect(x, y, 25, LINE_HEIGHT);
        }
    }
}
