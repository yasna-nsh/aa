package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class AvatarMenu extends Application {
    private final String username;
    private final String password;

    public AvatarMenu(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL avatarMenuUrl = AvatarMenu.class.getResource("/FXML/avatarMenu.fxml");
        assert avatarMenuUrl != null;
        FXMLLoader loader = new FXMLLoader(avatarMenuUrl);
        Pane rootPane = loader.load();
        ((AvatarMenuFXMLController) loader.getController()).setUsernameAndPassword(username, password);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}
