package view;

import controller.ProfileMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import view.messages.ProfileMenuMessage;
import view.transitions.MessageTransition;

import java.net.URL;

public class ChangePasswordMenu extends Application {
    public PasswordField newPassword;
    public PasswordField oldPassword;
    public Label messageLabel;
    private MessageTransition messageTransition;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL changePasswordMenuFXML = ChangePasswordMenu.class.getResource("/FXML/changePasswordMenu.fxml");
        assert changePasswordMenuFXML != null;
        Pane rootPane = FXMLLoader.load(changePasswordMenuFXML);
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), rootPane);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void changePassword() throws Exception {
        ProfileMenuMessage result = ProfileMenuController.getInstance().changePassword(newPassword.getText(), oldPassword.getText());
        if (result == ProfileMenuMessage.CHANGE_PASSWORD_SUCCESSFUL)
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
