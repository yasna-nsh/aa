package view;

import controller.DataHandler;
import javafx.scene.control.Label;
import model.User;
import view.transitions.MessageTransition;

public class ProfileMenuFXMLController {
    public Label messageLabel;
    private MessageTransition messageTransition;

    public void changeUsername() throws Exception {
        new ChangeUsernameMenu().start(StartMenu.stage);
    }

    public void changePassword() throws Exception {
        new ChangePasswordMenu().start(StartMenu.stage);
    }

    public void changeAvatar() throws Exception {
        new AvatarMenu(User.getCurrentUser().getUsername(), User.getCurrentUser().getPassword()).start(StartMenu.stage);
    }

    public void logout() throws Exception {
        User.setCurrentUser(null);
        User.resetGuest();
        new StartMenu().start(StartMenu.stage);
    }

    public void deleteAccount() throws Exception {
        User.getAllUsers().remove(User.getCurrentUser());
        DataHandler.getInstance().updateDatabase();
        logout();
    }

    public void printMessage(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }

    public void goBack() throws Exception {
        new MainMenu().start(StartMenu.stage);
    }
}
