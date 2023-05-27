package view;

import controller.ProfileMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.messages.ProfileMenuMessage;
import view.transitions.MessageTransition;

import java.net.URL;

public class ChangeUsernameMenu extends Application {
    public TextField newUsername;
    public PasswordField password;
    public Label messageLabel;
    private MessageTransition messageTransition;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL changeUsernameMenuFXML = ChangeUsernameMenu.class.getResource("/FXML/changeUsernameMenu.fxml");
        assert changeUsernameMenuFXML != null;
        Pane rootPane = FXMLLoader.load(changeUsernameMenuFXML);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void changeUsername() throws Exception {
        ProfileMenuMessage result = ProfileMenuController.getInstance().changeUsername(newUsername.getText(), password.getText());
        if (result == ProfileMenuMessage.CHANGE_USERNAME_SUCCESSFUL)
            new ProfileMenu(result.getMessage()).start(StartMenu.stage);
        else printError(result.getMessage());
    }

    public void cancel() throws Exception {
        new ProfileMenu("").start(StartMenu.stage);
    }

    private void printError(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }
}
