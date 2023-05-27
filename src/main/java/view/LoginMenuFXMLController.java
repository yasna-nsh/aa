package view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.messages.LoginMenuMessage;
import view.transitions.MessageTransition;

public class LoginMenuFXMLController {
    public TextField username;
    public PasswordField password;
    public Label messageLabel;
    private MessageTransition messageTransition;

    public void login() throws Exception {
        LoginMenuMessage result = controller.LoginMenuController.getInstance().login(username.getText(), password.getText());
        if (result == LoginMenuMessage.LOGIN_SUCCESSFUL) new MainMenu().start(StartMenu.stage);
        else printError(result.getMessage());
    }

    public void back() throws Exception {
        new StartMenu().start(StartMenu.stage);
    }

    private void printError(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }
}
