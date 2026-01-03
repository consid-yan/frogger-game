package frogger.model;

/**
 * Updatable is an interface used to convert GameObject into an Object that can update().
 * - Not all GameObjects behave like an item that can update itself without parameters (e.g. Player).
 */
public interface Updatable {
    void update();
}