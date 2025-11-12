package frogger.model;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);
    private final int deltaX;
    private final int deltaY;

    Direction(int dX, int dY) {
        deltaX = dX;
        deltaY = dY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
