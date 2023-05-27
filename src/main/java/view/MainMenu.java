package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainMenuFXML = MainMenu.class.getResource("/FXML/mainMenu.fxml");
        assert mainMenuFXML != null;
        Pane rootPane = FXMLLoader.load(mainMenuFXML);
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
