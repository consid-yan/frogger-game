package frogger.model;

import javafx.geometry.Rectangle2D;

public class Home extends GameObject {
    private boolean isOccupied;

    public Home(double x, double y) {
        super(x, y, Player.FROG_WIDTH, Player.FROG_WIDTH);
        isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupied() {
        isOccupied = true;
        hitBox = new Rectangle2D(getX(), getY(), 0, 0); // Once the home has been occupied, make the hitbox a point
    }
}
