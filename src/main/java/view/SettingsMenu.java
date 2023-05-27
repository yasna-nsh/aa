package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class SettingsMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL settingsMenuFXML = SettingsMenu.class.getResource("/FXML/settingsMenu.fxml");
        FXMLLoader loader = new FXMLLoader(settingsMenuFXML);
        Pane rootPane = loader.load();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
