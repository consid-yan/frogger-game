package frogger.display;

import frogger.model.FroggerGame;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import ucd.comp2011j.engine.Screen;

public class SettingsScreen implements Screen {
    private static final String[] CONTROLS = {
            "Move Up",
            "Move Down",
            "Move Left",
            "Move Right",
            "Pause / Play"
    };
    private static final String[] KEYS = {"UP", "DOWN", "LEFT", "RIGHT", "P"};

    private final Canvas canvas;

    public SettingsScreen() {
        canvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas(){ return canvas; }

    @Override
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getWidth());

        // Title
        gc.setFill(Color.FLORALWHITE);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 32));
        gc.setEffect(new DropShadow(5, Color.BLACK));
        gc.fillText("Controls", 3 * canvas.getWidth() / 7, canvas.getHeight() / 10);

        // Controls and Keys
        gc.setFont(new Font("Arial", 20));
        int start = 128, gap = 48; // Set the basic configs
        for (int i = 0; i < CONTROLS.length; i++) {
            gc.fillText(CONTROLS[i], 5 * canvas.getWidth() / 28, start + i * gap);
        }
        gc.setFill(Color.GOLD);
        for (int i = 0; i < KEYS.length; i++) {
            gc.fillText(KEYS[i], 5 * canvas.getWidth() / 7, start + i * gap);
        }

        // Back to Main Menu
        gc.setFill(Color.FLORALWHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Back to the Main Menu(Press M)", canvas.getWidth() / 2, 4 * canvas.getHeight() / 5);
        gc.restore();
    }
}
