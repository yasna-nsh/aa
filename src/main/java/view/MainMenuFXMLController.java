package view;

import javafx.scene.control.Label;
import model.User;
import model.game.Game;
import view.transitions.MessageTransition;

public class MainMenuFXMLController {
    public Label messageLabel;
    private MessageTransition messageTransition;

    public void startNewGame() throws Exception {
        new CustomizeMenu().start(StartMenu.stage);
    }

    public void continueGame() throws Exception {
        if (User.getCurrentUser().getSavedGame() != null)
            new GameMenu(new Game(User.getCurrentUser().getSavedGame())).start(StartMenu.stage);
        else printError("you don't have any saved games");
    }

    public void showProfileMenu() throws Exception {
        if (User.getCurrentUser() == User.getGuest()) printError("login to access your profile");
        else new ProfileMenu("").start(StartMenu.stage);
    }

    public void showScoreboard() throws Exception {
        new ScoreboardMenu().start(StartMenu.stage);
    }

    public void showSettings() throws Exception {
        new SettingsMenu().start(StartMenu.stage);
    }

    public void exit() throws Exception {
        User.setCurrentUser(null);
        User.resetGuest();
        new StartMenu().start(StartMenu.stage);
    }

    private void printError(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }
}
