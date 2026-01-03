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
import ucd.comp2011j.engine.Score;
import ucd.comp2011j.engine.ScoreKeeper;

public class HighScoresScreen implements Screen {
    private final Canvas canvas;
    private final ScoreKeeper scoreKeeper;

    public HighScoresScreen(ScoreKeeper sc) {
        scoreKeeper = sc;
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
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 32));
        gc.setEffect(new DropShadow(5, Color.BLACK));
        gc.fillText("Frogger Hall of Fame", canvas.getWidth() / 2, canvas.getHeight() / 10);

        // Scores displayed
        Score[] scores = scoreKeeper.getScores();
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.LEFT);
        int start = 96, gap = 32; // Set the basic configs
        for (int i = 0; i < scores.length; i++) {
            Score score = scores[i];
            gc.fillText(score.getName(), 3 * canvas.getWidth() / 14, start + i * gap);
            gc.fillText("" + score.getScore(), 5 * canvas.getWidth() / 7, start + i * gap);
        }

        // Back to Main Menu
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Back to the Main Menu(Press M)", canvas.getWidth() / 2, 4 * canvas.getHeight() / 5);
        gc.restore();
    }
}
