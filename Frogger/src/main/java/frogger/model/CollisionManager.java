package frogger.model;

import java.util.List;

public class CollisionManager {
    public GameObject firstCollision(GameObject subject, List<? extends GameObject> others) {
        for (GameObject obj : others) {
            if (subject.isIntersects(obj)) {
                return obj;
            }
        }
        return null;
    }
}
