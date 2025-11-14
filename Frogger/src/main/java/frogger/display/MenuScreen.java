package frogger.display;

import frogger.model.FroggerGame;
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


public class MenuScreen implements Screen {
    private final Canvas displayingCanvas;
    private static final String[] OPTIONS = {
            "New Game (press N)",
            "Settings (press S)",
            "High scores (press H)",
            "Exit Game (press E)"
    };

    // keep the same look as before but via the shared renderer
    private static final double FROG_BASE_X = 510;
    private static final double FROG_BASE_Y = 240;

    public MenuScreen() { displayingCanvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT); }

    @Override
    public Canvas getCanvas() { return displayingCanvas; }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, displayingCanvas.getWidth(), displayingCanvas.getHeight());
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, displayingCanvas.getWidth(), displayingCanvas.getHeight());

        // Title
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 60));
        gc.setFill(Color.OLIVE);
        gc.fillText("FROGGER", FroggerGame.SCREEN_WIDTH / 2.0 + 7, FroggerGame.SCREEN_HEIGHT / 6.0 + 2);
        gc.setFill(Color.YELLOW);
        gc.fillText("FROGGER", FroggerGame.SCREEN_WIDTH / 2.0, FroggerGame.SCREEN_HEIGHT / 6.0);

        // Options
        gc.setFont(new Font("Arial", 24));
        gc.setFill(Color.FLORALWHITE);
        gc.setEffect(new DropShadow(5, Color.BLACK));
        for (int i = 0; i < OPTIONS.length; i++) {
            gc.fillText(OPTIONS[i], FroggerGame.SCREEN_WIDTH / 3.0, (4 + i) * FroggerGame.SCREEN_HEIGHT / 9.0);
        }

        // Decoration (draw frog via FrogRenderer)
        gc.setEffect(new DropShadow(2, Color.BLACK));
        FrogRenderer.drawFrog(gc, FROG_BASE_X, FROG_BASE_Y, 1);
        gc.setEffect(null);
    }
}
