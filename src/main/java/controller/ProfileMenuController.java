package controller;

import model.User;
import view.messages.ProfileMenuMessage;

public class ProfileMenuController {
    private static final ProfileMenuController instance = new ProfileMenuController();

    private ProfileMenuController() {
    }

    public static ProfileMenuController getInstance() {
        return instance;
    }

    public ProfileMenuMessage changeUsername(String newUsername, String password) {
        if (newUsername == null || newUsername.isEmpty()) return ProfileMenuMessage.EMPTY_NEW_USERNAME;
        if (password == null || password.isEmpty()) return ProfileMenuMessage.EMPTY_PASSWORD;
        if (!User.getCurrentUser().getPassword().equals(password)) return ProfileMenuMessage.INCORRECT_PASSWORD;
        if (User.getUserByUsername(newUsername) != null) return ProfileMenuMessage.TAKEN_USERNAME;
        User.getCurrentUser().setUsername(newUsername);
        User.getCurrentUser().updateHighScoresUsername();
        DataHandler.getInstance().updateDatabase();
        return ProfileMenuMessage.CHANGE_USERNAME_SUCCESSFUL;
    }

    public ProfileMenuMessage changePassword(String newPassword, String password) {
        if (newPassword == null || newPassword.isEmpty()) return ProfileMenuMessage.EMOTY_NEW_PASSWORD;
        if (password == null || password.isEmpty()) return ProfileMenuMessage.EMPTY_PASSWORD;
        if (!User.getCurrentUser().getPassword().equals(password)) return ProfileMenuMessage.INCORRECT_PASSWORD;
        User.getCurrentUser().setPassword(newPassword);
        DataHandler.getInstance().updateDatabase();
        return ProfileMenuMessage.CHANGE_PASSWORD_SUCCESSFUL;
    }
}
