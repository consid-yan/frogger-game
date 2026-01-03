package frogger.display;

import javafx.scene.Scene;
import javafx.event.EventHandler;

import ucd.comp2011j.engine.MenuCommands;

public class MenuListener implements MenuCommands {
    // Pressed States
    private boolean newGame;
    private boolean settings;
    private boolean highScores;
    private boolean exit;
    private boolean backMenu;

    public void setListeners(Scene scene) {
        scene.setOnKeyTyped(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if ("N".equalsIgnoreCase(event.getCharacter())) {
                    newGame = true;
                } else if ("S".equalsIgnoreCase(event.getCharacter())) {
                    settings = true;
                } else if ("H".equalsIgnoreCase(event.getCharacter())) {
                    highScores = true;
                } else if ("E".equalsIgnoreCase(event.getCharacter())) {
                    exit = true;
                } else if ("M".equalsIgnoreCase(event.getCharacter())) {
                    backMenu = true;
                }
            }
        });
    }

    @Override
    public boolean hasPressedNewGame() {
        return newGame;
    }

    @Override
    public boolean hasPressedAboutScreen() {
        return settings;
    }

    @Override
    public boolean hasPressedHighScoreScreen() {
        return highScores;
    }

    @Override
    public boolean hasPressedExit() {
        return exit;
    }

    @Override
    public boolean hasPressedMenu() {
        return backMenu;
    }

    @Override
    public void resetKeyPresses() {
        newGame = false;
        settings = false;
        highScores = false;
        exit = false;
        backMenu = false;
    }
}
