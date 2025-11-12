package frogger.display;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import frogger.model.FroggerGame;

import ucd.comp2011j.engine.GameManager;
import ucd.comp2011j.engine.ScoreKeeper;

public class ApplicationStart extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Frogger");
        StackPane root = new StackPane();
        Scene scene = new Scene(root, FroggerGame.SCREEN_WIDTH, FroggerGame.SCREEN_HEIGHT);
        primaryStage.setScene(scene);

        PlayerListener playerListener = new PlayerListener();
        playerListener.setListeners(scene);
        MenuListener menuListener = new MenuListener();
        menuListener.setListeners(scene);
        FroggerGame game = new FroggerGame(playerListener);
        MenuScreen menuScreen = new MenuScreen();
        GameScreen gameScreen = new GameScreen(game);
        ScoreKeeper scoreKeeper = new ScoreKeeper("scores.txt");
        GameManager gManager = new GameManager(game, root, menuListener, menuScreen,new SettingsScreen(),
                new HighScoresScreen(scoreKeeper), gameScreen, scoreKeeper);
        primaryStage.show();
        gManager.run();
    }
}
