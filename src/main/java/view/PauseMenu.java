package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class PauseMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL pauseMenuFXML = PauseMenu.class.getResource("/FXML/pauseMenu.fxml");
        FXMLLoader loader = new FXMLLoader(pauseMenuFXML);
        Pane rootPane = loader.load();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        ((PauseMenuFXMLController) loader.getController()).preparePauseMenu();
    }
}
