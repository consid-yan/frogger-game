package frogger.model.managers;

import frogger.model.GameObject;

import java.util.Collection;
import java.util.Optional;

/**
 * Centralizes collision queries between GameObjects.
 * Uses axis-aligned hit boxes already stored in each GameObject.
 */
public class CollisionManager {

    /**
     * Returns the first object in 'others' whose hitBox intersects 'subject', or empty.
     */
    public Optional<GameObject> firstCollision(GameObject subject, Collection<? extends GameObject> others) {
        for (GameObject obj : others) {
            if (intersects(subject, obj)) {
                return Optional.of(obj);
            }
        }
        return Optional.empty();
    }

    /**
     * Simple wrapper for GameObject intersection (kept here for future broad‑phase optimizations).
     */
    public boolean intersects(GameObject a, GameObject obj) {
        return a.getHitBox().intersects(obj.getHitBox());
    }
}
