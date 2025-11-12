package frogger.display;

import frogger.model.FroggerGame;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import ucd.comp2011j.engine.Screen;
import ucd.comp2011j.engine.Score;
import ucd.comp2011j.engine.ScoreKeeper;

public class HighScoresScreen implements Screen {
    private Canvas displayingCanvas;
    private ScoreKeeper scoreKeeper;

    public HighScoresScreen(ScoreKeeper sc) {
        this.scoreKeeper = sc;
        displayingCanvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas(){ return displayingCanvas; }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();
        gc.clearRect(0,0, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0,0,FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.setFill(Color.FLORALWHITE);
        gc.fillText("Frogger Hall of Fame", FroggerGame.SCREEN_WIDTH / 2, FroggerGame.SCREEN_HEIGHT / 10);

        Score[] scores = scoreKeeper.getScores();
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.LEFT);
        for (int i = 0; i < scores.length; i++) {
            Score score = scores[i];
            gc.fillText(score.getName(), 2 * FroggerGame.SCREEN_WIDTH / 6, 96 + i * 32);
            gc.fillText("" + score.getScore(), 4 * FroggerGame.SCREEN_WIDTH / 6, 96 + i * 32);
        }
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Arial", 28));
        gc.fillText("Back to the Main Menu(Press M)", FroggerGame.SCREEN_WIDTH / 2, 8 * FroggerGame.SCREEN_HEIGHT / 10);
    }
}
