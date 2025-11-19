package frogger.model;

/**
 * Movable is an interface used to convert GameObject into an Object that has direction and speed.
 * - Not all GameObjects behave like a Movable item (e.g. Home).
 */
public interface Movable {
    Direction getDirection();

    double getSpeed();
}
