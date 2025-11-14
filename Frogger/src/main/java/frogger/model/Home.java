package frogger.model;

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
    }
}
