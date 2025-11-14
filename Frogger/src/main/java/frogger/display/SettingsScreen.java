package frogger.display;

import frogger.model.FroggerGame;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import ucd.comp2011j.engine.Screen;

public class SettingsScreen implements Screen {
    private final Canvas displayingCanvas;
    private static final String[] CONTROLS = {
            "Move Up",
            "Move Down",
            "Move Left",
            "Move Right",
            "Pause/Play"
    };
    private static final String[] KEYS = {"UP", "DOWN", "LEFT", "RIGHT", "P",};

    public SettingsScreen() {
        displayingCanvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas(){ return displayingCanvas; }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, displayingCanvas.getWidth(), displayingCanvas.getHeight());
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, displayingCanvas.getWidth(), displayingCanvas.getHeight());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Frogger Controls", FroggerGame.SCREEN_WIDTH / 2.0, 64);

        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        int start = 128;
        int gap = 48;

        gc.setFill(Color.FLORALWHITE);
        gc.setFont(new Font("Arial", 32));
        gc.fillText("Controls", 3 * FroggerGame.SCREEN_WIDTH / 7.0, FroggerGame.SCREEN_HEIGHT / 10.0);

        gc.setFont(new Font("Arial", 20));
        for (int i = 0; i < CONTROLS.length; i++) {
            gc.fillText(CONTROLS[i], FroggerGame.SCREEN_WIDTH / 7.0, start + i * gap);
        }

        gc.setFill(Color.GOLD);
        for (int i = 0; i < KEYS.length; i++) {
            gc.fillText(KEYS[i], 5 * FroggerGame.SCREEN_WIDTH / 7.0, start + i * gap);
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.setFill(Color.FLORALWHITE);
        gc.fillText("Back to the Main Menu(Press M)", FroggerGame.SCREEN_WIDTH / 2.0,
                4 * FroggerGame.SCREEN_HEIGHT / 5.0);
    }
}
