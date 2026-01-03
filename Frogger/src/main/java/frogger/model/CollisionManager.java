package frogger.model;

import java.util.List;

/**
 * Simple collision detection utility for the Frogger game.
 */
public class CollisionManager {
    /**
     * This method finds the first collision between a subject and any object in a list.
     *
     * @param subject The GameObject to check collisions for
     * @param others List of potential colliders (can be any subclass of GameObject)
     * @return The first intersecting GameObject, or null if no collision found
     */
    public GameObject firstCollision(GameObject subject, List<? extends GameObject> others) {
        for (GameObject obj : others) {
            if (subject.isIntersects(obj)) {
                return obj; // Early exit on first collision found
            }
        }
        return null; // No collision - callers must handle null case
    }
}
