package frogger.display;

import frogger.model.Direction;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class PlayerListener {
    // Current direction held by player. Null means no direction pressed.
    private Direction pressedDirection;
    private boolean pause;

    public void resetPause() { pause = false; }

    public boolean hasPressedPause() { return pause; }

    public Direction getAndClearPressedDirection() {
        Direction dir = pressedDirection;
        pressedDirection = null;
        return dir;
    }

    public void setListeners(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Toggle pause on 'P' key. Entering pause clears any pending direction.
                if (event.getCode() == KeyCode.P) {
                    pause = !pause;
                    if (pause) {
                        pressedDirection = null;
                    }
                }

                // Only process movement keys when not paused
                if (!pause) {
                    if (event.getCode() == KeyCode.UP) {
                        pressedDirection = Direction.UP;
                    } else if (event.getCode() == KeyCode.DOWN) {
                        pressedDirection = Direction.DOWN;
                    } else if (event.getCode() == KeyCode.LEFT) {
                        pressedDirection = Direction.LEFT;
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        pressedDirection = Direction.RIGHT;
                    }
                }
            }
        });

        // Movement stops when direction is consumed by game loop, not on key release.
        scene.setOnKeyReleased(null);
    }
}
