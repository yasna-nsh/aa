package view;

import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import model.game.Game;

import java.net.URL;
import java.util.ArrayList;

public class GameMenu extends Application {
    private final Game game;

    public GameMenu(Game game) {
        this.game = game;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL gameMenuFXML = GameMenu.class.getResource("/FXML/gameMenu.fxml");
        FXMLLoader loader = new FXMLLoader(gameMenuFXML);
        Pane rootPane = loader.load();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (GameController.getInstance().isPaused()) return;
            ArrayList<KeyCode> defaultKeyCodes = User.getCurrentUser().getDefaultKeyCodes();
            if (event.getCode() == defaultKeyCodes.get(0))
                ((GameMenuFXMLController) loader.getController()).shoot();
            else if (event.getCode() == defaultKeyCodes.get(1))
                ((GameMenuFXMLController) loader.getController()).freeze();
            else if (event.getCode() == defaultKeyCodes.get(2))
                ((GameMenuFXMLController) loader.getController()).moveBallHorizontally(2);
            else if (event.getCode() == defaultKeyCodes.get(3))
                ((GameMenuFXMLController) loader.getController()).moveBallHorizontally(-2);
        });
        primaryStage.setScene(scene);
        GameController.getInstance().setLoader(loader);
        GameController.getInstance().setCurrentScene(scene);
        GameController.getInstance().setPaused(false);
        ((GameMenuFXMLController) loader.getController()).prepareGame(game);
        primaryStage.centerOnScreen();
    }
}
