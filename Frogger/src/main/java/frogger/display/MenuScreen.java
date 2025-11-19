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
    private static final String[] OPTIONS = {
            "New Game (press N)",
            "Settings (press S)",
            "High scores (press H)",
            "Exit Game (press E)"
    };
    private static final double FROG_START_X = 510;
    private static final double FROG_START_Y = 240;

    private final Canvas canvas;

    public MenuScreen() { canvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT); }

    @Override
    public Canvas getCanvas() { return canvas; }

    @Override
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.save();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getWidth());

        // Title
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 60));
        gc.setFill(Color.OLIVE);
        gc.fillText("FROGGER", canvas.getWidth() / 2.0 + 7, canvas.getHeight() / 6.0 + 2);
        gc.setFill(Color.YELLOW);
        gc.fillText("FROGGER", canvas.getWidth() / 2.0, canvas.getHeight() / 6.0);

        // Options
        gc.setFont(new Font("Arial", 24));
        gc.setFill(Color.FLORALWHITE);
        gc.setEffect(new DropShadow(5, Color.BLACK));
        for (int i = 0; i < OPTIONS.length; i++) {
            gc.fillText(OPTIONS[i], canvas.getWidth() / 3.0, (4 + i) * canvas.getHeight() / 9.0);
        }

        // Decoration (Draw frog via FrogRenderer)
        gc.setEffect(new DropShadow(2, Color.BLACK));
        FrogRenderer.drawFrog(gc, FROG_START_X, FROG_START_Y, 1);
        gc.restore();
    }
}
