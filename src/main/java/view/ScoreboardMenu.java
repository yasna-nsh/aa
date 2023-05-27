package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class ScoreboardMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL scoreboardMenuFXML = SettingsMenu.class.getResource("/FXML/scoreboardMenu.fxml");
        FXMLLoader loader = new FXMLLoader(scoreboardMenuFXML);
        Pane rootPane = loader.load();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        ((ScoreboardMenuFXMLController) loader.getController()).prepareScoreboard();
    }
}
