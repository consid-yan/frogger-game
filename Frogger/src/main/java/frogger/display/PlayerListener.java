package frogger.display;

import frogger.model.Direction;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class PlayerListener {
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
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.P) {
                    pause = !pause;
                    if (pause) {
                        pressedDirection = null;
                    }
                }

                if (!pause) {
                    if (e.getCode() == KeyCode.UP) {
                        pressedDirection = Direction.UP;
                    } else if (e.getCode() == KeyCode.DOWN) {
                        pressedDirection = Direction.DOWN;
                    } else if (e.getCode() == KeyCode.LEFT) {
                        pressedDirection = Direction.LEFT;
                    } else if (e.getCode() == KeyCode.RIGHT) {
                        pressedDirection = Direction.RIGHT;
                    }
                }
            }
        });

        scene.setOnKeyReleased(null);
    }
}
