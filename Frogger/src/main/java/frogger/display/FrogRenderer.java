package frogger.display;

import frogger.model.Direction;
import frogger.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Compact frog renderer that relies on Player constants (FROG_WIDTH, FROG_COLLISION_HEIGHT)
 * and uses normalized coordinates (fractions) so there are far fewer magic numbers.
 *
 * - drawFrog(gc, Player) centers the frog visual inside the player's hitbox and rotates around
 *   the hitbox center. The visual will always fit inside the hitbox.
 * - drawFrog(gc, baseX, baseY, scale) draws a decorative frog at a top-left base position;
 *   scale is a multiplier relative to the normalized base size.
 */
public final class FrogRenderer {
    private FrogRenderer() {}

    // Base pixel size used for normalization (matches the old decorative geometry)
    private static final double BASE_PX_W = 90.0;
    private static final double BASE_PX_H = 120.0;

    // Normalized placements (fractions of base width/height)
    private static final double EYE_FRAC_X1 = 10.0 / BASE_PX_W;
    private static final double EYE_FRAC_X2 = 62.0 / BASE_PX_W;
    private static final double EYE_FRAC_Y  = 0.0 / BASE_PX_H;
    private static final double EYE_FRAC_SIZE = 20.0 / BASE_PX_W; // relative to width

    private static final double INNER_OFF_X = 16.0 / BASE_PX_W;
    private static final double INNER_OFF_Y = 15.0 / BASE_PX_H;
    private static final double INNER_W_FRAC = 60.0 / BASE_PX_W;
    private static final double INNER_H_FRAC = 80.0 / BASE_PX_H;

    // Limbs expressed as fractions of base width/height (x[], y[])
    private static final double[] LEFT_ARM_X_FRAC  = { 5.0/BASE_PX_W, -15.0/BASE_PX_W, -20.0/BASE_PX_W };
    private static final double[] LEFT_ARM_Y_FRAC  = { 35.0/BASE_PX_H, 40.0/BASE_PX_H, 25.0/BASE_PX_H };
    private static final double[] RIGHT_ARM_X_FRAC = { 85.0/BASE_PX_W, 105.0/BASE_PX_W, 110.0/BASE_PX_W };
    private static final double[] RIGHT_ARM_Y_FRAC = { 35.0/BASE_PX_H, 40.0/BASE_PX_H, 25.0/BASE_PX_H };
    private static final double[] LEFT_LEG_X_FRAC  = { 10.0/BASE_PX_W, 0.0/BASE_PX_W, -10.0/BASE_PX_W };
    private static final double[] LEFT_LEG_Y_FRAC  = { 105.0/BASE_PX_H, 130.0/BASE_PX_H, 85.0/BASE_PX_H };
    private static final double[] RIGHT_LEG_X_FRAC = { 80.0/BASE_PX_W, 90.0/BASE_PX_W, 100.0/BASE_PX_W };
    private static final double[] RIGHT_LEG_Y_FRAC = { 105.0/BASE_PX_H, 130.0/BASE_PX_H, 85.0/BASE_PX_H };

    /**
     * Decorative: draw at explicit top-left base position with an explicit scale multiplier.
     * baseX/baseY == top-left of the visual bounding box.
     * scale == multiplier relative to BASE_PX_W/BASE_PX_H.
     */
    public static void drawFrog(GraphicsContext gc, double baseX, double baseY, double scale) {
        double visualW = BASE_PX_W * scale;
        double visualH = BASE_PX_H * scale;
        drawVisual(gc, baseX, baseY, visualW, visualH);
    }

    /**
     * In-game: draw the frog aligned with the player's hitbox.
     * Ensures visual is no larger than hitbox (keeps collisions and visuals consistent).
     */
    public static void drawFrog(GraphicsContext gc, Player player) {
        double pX = player.getX();
        double pY = player.getY();

        // Compute uniform scale so visual fits into player's hitbox.
        double scaleX = Player.FROG_WIDTH / BASE_PX_W;
        double scaleY = Player.FROG_HEIGHT / BASE_PX_H;
        double scale = Math.min(scaleX, scaleY);

        double visualW = BASE_PX_W * scale;
        double visualH = BASE_PX_H * scale;

        // Center the visual inside the player's hitbox
        double drawX = pX + (Player.FROG_WIDTH - visualW) / 2.0;
        double drawY = pY + (Player.FROG_HEIGHT - visualH) / 2.0;

        if (!player.isAlive()) {
            drawDeathEffect(gc, pX, pY, Player.FROG_WIDTH, Player.FROG_HEIGHT);
            return;
        }

        gc.save();
        try {
            // Rotate around player's hitbox center so rotation matches gameplay logic
            Direction dir = player.getDirection();
            double centerX = pX + Player.FROG_WIDTH / 2.0;
            double centerY = pY + Player.FROG_HEIGHT / 2.0;

            gc.translate(centerX, centerY);
            switch (dir) {
                case UP:    gc.rotate(0);   break;
                case DOWN:  gc.rotate(180); break;
                case LEFT:  gc.rotate(-90); break;
                case RIGHT: gc.rotate(90);  break;
            }
            gc.translate(-centerX, -centerY);

            drawVisual(gc, drawX, drawY, visualW, visualH);
        } finally {
            gc.restore();
        }
    }

    private static void drawVisual(GraphicsContext gc, double x, double y, double visualW, double visualH) {
        // Eyes
        gc.setFill(Color.CRIMSON);
        double eyeW = EYE_FRAC_SIZE * visualW;
        double eyeH = EYE_FRAC_SIZE * visualH;
        gc.fillOval(x + EYE_FRAC_X1 * visualW, y + EYE_FRAC_Y * visualH, eyeW, eyeH);
        gc.fillOval(x + EYE_FRAC_X2 * visualW, y + EYE_FRAC_Y * visualH, eyeW, eyeH);

        // Body (outer)
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(x, y, visualW, visualH);

        // Inner belly
        gc.setFill(Color.YELLOWGREEN);
        gc.fillOval(x + INNER_OFF_X * visualW, y + INNER_OFF_Y * visualH,
                INNER_W_FRAC * visualW, INNER_H_FRAC * visualH);

        // Limbs
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(Math.max(1.0, 6.0 * (visualW / BASE_PX_W)));
        strokeFracs(gc, x, y, visualW, visualH, LEFT_ARM_X_FRAC, LEFT_ARM_Y_FRAC);
        strokeFracs(gc, x, y, visualW, visualH, RIGHT_ARM_X_FRAC, RIGHT_ARM_Y_FRAC);
        strokeFracs(gc, x, y, visualW, visualH, LEFT_LEG_X_FRAC, LEFT_LEG_Y_FRAC);
        strokeFracs(gc, x, y, visualW, visualH, RIGHT_LEG_X_FRAC, RIGHT_LEG_Y_FRAC);
    }

    private static void strokeFracs(GraphicsContext gc, double x, double y,
                                    double visualW, double visualH,
                                    double[] fx, double[] fy) {
        int n = Math.min(fx.length, fy.length);
        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            xs[i] = x + fx[i] * visualW;
            ys[i] = y + fy[i] * visualH;
        }
        gc.strokePolyline(xs, ys, n);
    }

    private static void drawDeathEffect(GraphicsContext gc, double pX, double pY, double w, double h) {
        gc.setFill(Color.color(1, 0, 0, 0.25));
        gc.fillOval(pX, pY, w, h);

        gc.setStroke(Color.RED);
        gc.setLineWidth(3.0);
        gc.strokeLine(pX,     pY,     pX + w, pY + h);
        gc.strokeLine(pX + w, pY,     pX,     pY + h);

        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(1.5);
        gc.strokeOval(pX, pY, w, h);
    }
}
