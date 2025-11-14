package frogger.model.managers;

import frogger.model.GameObject;
import java.util.Collection;
import java.util.Optional;

public class CollisionManager {

    /**
     * Returns the first object in 'others' whose hitBox intersects 'subject', or empty.
     */
    public Optional<GameObject> firstCollision(GameObject subject, Collection<? extends GameObject> others) {
        for (GameObject obj : others) {
            if (subject.isIntersects(obj)) {
                return Optional.of(obj);
            }
        }
        return Optional.empty();
    }
}
