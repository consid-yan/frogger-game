package frogger.model;

public enum EntityType {
    LONG_LOG(120, 25), SHORT_LOG(80, 25),
    TURTLE(25, 30),
    CAR(45, 20), BUS(90, 30), TRUCK(120, 30);

    private final int width;
    private final int height;

    EntityType(int w, int h) {
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
