package frogger.display;

import frogger.model.Direction;
import frogger.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class FrogRenderer {
    private FrogRenderer() {}

    private static final double BASE_PX_W = 90.0, BASE_PX_H = 120.0;

    private static final double EYE_X1 = 10.0 / BASE_PX_W, EYE_X2 = 62.0 / BASE_PX_W, EYE_Y = 0.0
            , EYE_SIZE = 20.0 / BASE_PX_W;
    private static final double BELLY_OFF_X = 16.0 / BASE_PX_W, BELLY_OFF_Y = 15.0 / BASE_PX_H
            , BELLY_W = 60.0 / BASE_PX_W, BELLY_H = 80.0 / BASE_PX_H;

    private static final double[] L_ARM_X = { 5.0 / BASE_PX_W, -15.0 / BASE_PX_W, -20.0 / BASE_PX_W };
    private static final double[] L_ARM_Y = { 35.0 / BASE_PX_H, 40.0 / BASE_PX_H, 25.0 / BASE_PX_H };
    private static final double[] R_ARM_X = { 85.0 / BASE_PX_W, 105.0 / BASE_PX_W, 110.0 / BASE_PX_W };
    private static final double[] R_ARM_Y = { 35.0 / BASE_PX_H, 40.0 / BASE_PX_H, 25.0 / BASE_PX_H };
    private static final double[] L_LEG_X = { 10.0 / BASE_PX_W, 0.0 / BASE_PX_W, -10.0 / BASE_PX_W };
    private static final double[] L_LEG_Y = { 105.0 / BASE_PX_H, 130.0 / BASE_PX_H, 85.0 / BASE_PX_H };
    private static final double[] R_LEG_X = { 80.0 / BASE_PX_W, 90.0 / BASE_PX_W, 100.0 / BASE_PX_W };
    private static final double[] R_LEG_Y = { 105.0 / BASE_PX_H, 130.0 / BASE_PX_H, 85.0 / BASE_PX_H };

    public static void drawFrog(GraphicsContext gc, double baseX, double baseY, double scale) {
        double w = BASE_PX_W * scale, h = BASE_PX_H * scale;
        drawVisual(gc, baseX, baseY, w, h);
    }

    public static void drawFrog(GraphicsContext gc, Player player) {
        double pX = player.getX(), pY = player.getY();
        double w = Player.FROG_WIDTH, h = Player.FROG_HEIGHT;
        if (!player.isAlive()) { drawDeathEffect(gc, pX, pY, w, h); return; }
        double scale = Math.min(w / BASE_PX_W, h / BASE_PX_H);
        double vW = BASE_PX_W * scale, vH = BASE_PX_H * scale;
        double drawX = pX + (w - vW) / 2.0, drawY = pY + (h - vH) / 2.0;
        double angle = angleFor(player.getDirection());
        double cx = pX + w / 2.0, cy = pY + h / 2.0;
        gc.save();
        try {
            gc.translate(cx, cy);
            gc.rotate(angle);
            gc.translate(-cx, -cy);
            drawVisual(gc, drawX, drawY, vW, vH);
        } finally {
            gc.restore();
        }
    }

    private static void drawVisual(GraphicsContext gc, double x, double y, double w, double h) {
        drawLimbs(gc, x, y, w, h);
        drawEyes(gc, x, y, w, h);
        drawBody(gc, x, y, w, h);
        drawBelly(gc, x, y, w, h);
    }

    private static void drawEyes(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setFill(Color.CRIMSON);
        double eyeW = EYE_SIZE * w, eyeH = EYE_SIZE * h;
        gc.fillOval(x + EYE_X1 * w, y + EYE_Y * h, eyeW, eyeH);
        gc.fillOval(x + EYE_X2 * w, y + EYE_Y * h, eyeW, eyeH);
    }

    private static void drawBody(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(x, y, w, h);
    }

    private static void drawBelly(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setFill(Color.YELLOWGREEN);
        gc.fillOval(x + BELLY_OFF_X * w, y + BELLY_OFF_Y * h, BELLY_W * w, BELLY_H * h);
    }

    private static void drawLimbs(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(Math.max(1.0, 6.0 * (w / BASE_PX_W)));
        strokeFracs(gc, x, y, w, h, L_ARM_X, L_ARM_Y);
        strokeFracs(gc, x, y, w, h, R_ARM_X, R_ARM_Y);
        strokeFracs(gc, x, y, w, h, L_LEG_X, L_LEG_Y);
        strokeFracs(gc, x, y, w, h, R_LEG_X, R_LEG_Y);
    }

    private static void strokeFracs(GraphicsContext gc, double x, double y, double w, double h, double[] fx, double[] fy) {
        int n = Math.min(fx.length, fy.length);
        double[] xs = new double[n], ys = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = x + fx[i] * w;
            ys[i] = y + fy[i] * h;
        }
        gc.strokePolyline(xs, ys, n);
    }

    private static void drawDeathEffect(GraphicsContext gc, double pX, double pY, double w, double h) {
        gc.setFill(Color.color(1, 0, 0, 0.25));
        gc.fillOval(pX, pY, w, h);
        gc.setStroke(Color.RED);
        gc.setLineWidth(3.0);
        gc.strokeLine(pX, pY, pX + w, pY + h);
        gc.strokeLine(pX + w, pY, pX, pY + h);
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(1.5);
        gc.strokeOval(pX, pY, w, h);
    }

    private static double angleFor(Direction dir) {
        return switch (dir) {
            case DOWN -> 180;
            case LEFT -> -90;
            case RIGHT -> 90;
            default -> 0;
        };
    }
}
