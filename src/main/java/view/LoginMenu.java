package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class LoginMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL loginMenuFXML = LoginMenu.class.getResource("/FXML/loginMenu.fxml");
        assert loginMenuFXML != null;
        Pane rootPane = FXMLLoader.load(loginMenuFXML);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
