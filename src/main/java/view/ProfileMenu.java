package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class ProfileMenu extends Application {
    private String message;

    public ProfileMenu(String message) {
        this.message = message;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL profileMenuFXML = ProfileMenu.class.getResource("/FXML/profileMenu.fxml");
        FXMLLoader loader = new FXMLLoader(profileMenuFXML);
        Pane rootPane = loader.load();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        if (message == null || message.isEmpty()) message = "hello " + User.getCurrentUser().getUsername();
        ((ProfileMenuFXMLController) loader.getController()).printMessage(message);
    }
}
