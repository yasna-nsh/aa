package view;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.messages.RegisterMenuMessage;
import view.transitions.MessageTransition;

public class RegisterMenuFXMLController {
    public TextField username;
    public PasswordField password;
    public PasswordField passwordConfirmation;
    public Label messageLabel;
    private MessageTransition messageTransition;

    public void register() throws Exception {
        RegisterMenuMessage result = controller.RegisterMenuController.getInstance().registerFirstStep(username.getText(), password.getText(), passwordConfirmation.getText());
        if (result == RegisterMenuMessage.SUCCESSFUL_REGISTER)
            new AvatarMenu(username.getText(), password.getText()).start(StartMenu.stage);
        else printMessage(result.getMessage());
    }

    public void back() throws Exception {
        new StartMenu().start(StartMenu.stage);
    }

    public void printMessage(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }
}
