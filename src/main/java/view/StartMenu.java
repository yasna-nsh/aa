package view;

import controller.DataHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class StartMenu extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        DataHandler.getInstance().updateInAppData();
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        URL startMenuFXML = StartMenu.class.getResource("/FXML/startMenu.fxml");
        assert startMenuFXML != null;
        Pane rootPane = FXMLLoader.load(startMenuFXML);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image(StartMenu.class.getResource("/images/icon.png").toExternalForm()));
        primaryStage.show();
    }
}
