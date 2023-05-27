package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class CustomizeMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL customizeLevelMenuFXML = CustomizeMenu.class.getResource("/FXML/customizeMenu.fxml");
        assert customizeLevelMenuFXML != null;
        Pane rootPane = FXMLLoader.load(customizeLevelMenuFXML);
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
