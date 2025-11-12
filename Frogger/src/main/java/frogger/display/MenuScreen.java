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
    private Canvas displayingCanvas;

    public MenuScreen() {
        displayingCanvas = new Canvas(FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
    }

    @Override
    public Canvas getCanvas(){ return displayingCanvas; }

    private void drawFrog(GraphicsContext gc){
        gc.setFill(Color.CRIMSON);
        gc.fillOval(520, 240, 20, 20);
        gc.fillOval(572, 240, 20, 20);                  //Draw its eyes

        gc.setFill(Color.DARKGREEN);
        gc.fillOval(510, 240, 90, 120);
        gc.setFill(Color.YELLOWGREEN);
        gc.fillOval(526, 255, 60, 80);                  //Draw its body

        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(6.0);
        double[] xPointsLeftArm = { 515.0, 495.0, 490.0 };
        double[] yPointsLeftArm = { 275.0, 280.0, 265.0 };
        gc.strokePolyline(xPointsLeftArm, yPointsLeftArm, 3);       //Draw its left arm
        double[] xPointsRightArm = { 595.0, 615.0, 620.0 };
        double[] yPointsRightArm = { 275.0, 280.0, 265.0 };
        gc.strokePolyline(xPointsRightArm, yPointsRightArm, 3);     //Draw its right arm
        double[] xPointsLeftLeg = { 520.0, 510.0, 500.0 };
        double[] yPointsLeftLeg = { 345.0, 370.0, 325.0 };
        gc.strokePolyline(xPointsLeftLeg, yPointsLeftLeg, 3);       //Draw its left leg
        double[] xPointsRightLeg = { 590.0, 600.0, 610.0 };
        double[] yPointsRightLeg = { 345.0, 370.0, 325.0 };
        gc.strokePolyline(xPointsRightLeg, yPointsRightLeg, 3);     //Draw its right leg
    }

    private void drawOutlinedText(GraphicsContext gc, String text, double x, double y,
                                           Color outlineColor, Color textColor,
                                           int deltaX, int deltaY) {
        gc.setFill(outlineColor);
        gc.fillText(text, x + deltaX, y + deltaY);
        gc.setFill(textColor);
        gc.fillText(text, x, y);
    }

    @Override
    public void paint() {
        GraphicsContext gc = displayingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        //Title
        gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 60));
        drawOutlinedText(gc, "FROGGER", FroggerGame.SCREEN_WIDTH/2, FroggerGame.SCREEN_HEIGHT / 6,
                Color.OLIVE, Color.YELLOW, 7, 2);

        //Options
        gc.setFont(new Font("Arial", 24));
        gc.setFill(Color.FLORALWHITE);
        gc.setEffect(new DropShadow(5,Color.BLACK));
        gc.fillText("New Game(press N)", FroggerGame.SCREEN_WIDTH/3, 4 * FroggerGame.SCREEN_HEIGHT / 9);
        gc.fillText("Settings(press S)", FroggerGame.SCREEN_WIDTH/3, 5 * FroggerGame.SCREEN_HEIGHT / 9);
        gc.fillText("High scores(press H)", FroggerGame.SCREEN_WIDTH/3, 6 * FroggerGame.SCREEN_HEIGHT / 9);
        gc.fillText("Exit Game(press E)", FroggerGame.SCREEN_WIDTH/3, 7 * FroggerGame.SCREEN_HEIGHT / 9);

        //Decoration
        gc.setEffect(new DropShadow(2,Color.BLACK));
        drawFrog(gc);
        gc.setEffect(null);
    }
}
