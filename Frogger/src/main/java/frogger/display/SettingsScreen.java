package frogger.display;

import frogger.model.FroggerGame;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import ucd.comp2011j.engine.Screen;

public class SettingsScreen implements Screen {
    private Canvas displayingCanvas;

    public SettingsScreen() {
        displayingCanvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas(){ return displayingCanvas; }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Frogger Controls", FroggerGame.SCREEN_WIDTH / 2, 64);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.CENTER);
        int start = 128;
        int gap = 48;

        gc.setFont(new Font("Arial", 32));
        gc.setFill(Color.FLORALWHITE);
        gc.fillText("Controls", 3 * FroggerGame.SCREEN_WIDTH / 7, FroggerGame.SCREEN_HEIGHT / 10);

        gc.setFont(new Font("Arial", 20));
        gc.fillText("Move Up", 1 * FroggerGame.SCREEN_WIDTH / 7, start + 0 * gap);
        gc.fillText("Move Down", 1 * FroggerGame.SCREEN_WIDTH / 7, start + 1 * gap);
        gc.fillText("Move Left", 1 * FroggerGame.SCREEN_WIDTH / 7, start + 2 * gap);
        gc.fillText("Move Right", 1 * FroggerGame.SCREEN_WIDTH / 7, start + 3 * gap);
        gc.fillText("Pause/Play", 1 * FroggerGame.SCREEN_WIDTH / 7, start + 4 * gap);

        gc.setFill(Color.GOLD);
        gc.fillText("UP", 5 * FroggerGame.SCREEN_WIDTH / 7, start + 0 * gap);
        gc.fillText("DOWN", 5 * FroggerGame.SCREEN_WIDTH / 7, start + 1 * gap);
        gc.fillText("LEFT", 5 * FroggerGame.SCREEN_WIDTH / 7, start + 2 * gap);
        gc.fillText("RIGHT", 5 * FroggerGame.SCREEN_WIDTH / 7, start + 3 * gap);
        gc.fillText("P", 5 * FroggerGame.SCREEN_WIDTH / 7, start + 4 * gap);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.setFill(Color.FLORALWHITE);
        gc.fillText("Back to the Main Menu(Press M)", FroggerGame.SCREEN_WIDTH / 2, 8 * FroggerGame.SCREEN_HEIGHT / 10);
    }

    private double calcCenterX(String t, int size) {
        Font font = new Font("Arial", size);
        Text internal = new Text(t);
        internal.setFont(font);
        double textWidth = internal.getLayoutBounds().getWidth();
        return FroggerGame.SCREEN_WIDTH / 2 - textWidth;
    }
}
