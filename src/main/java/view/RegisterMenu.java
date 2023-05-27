package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class RegisterMenu extends Application {
    private final boolean registerSuccessful;

    public RegisterMenu(boolean registerSuccessful) {
        this.registerSuccessful = registerSuccessful;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL registerMenuFXML = RegisterMenu.class.getResource("/FXML/registerMenu.fxml");
        FXMLLoader loader = new FXMLLoader(registerMenuFXML);
        Pane rootPane = loader.load();
        if (registerSuccessful)
            ((RegisterMenuFXMLController) loader.getController()).printMessage("account was created successfully");
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
