package controller;

import model.User;
import view.messages.LoginMenuMessage;

public class LoginMenuController {
    private static final LoginMenuController instance = new LoginMenuController();

    private LoginMenuController() {
    }

    public static LoginMenuController getInstance() {
        return instance;
    }

    public LoginMenuMessage login(String username, String password) {
        if (username == null || username.isEmpty()) return LoginMenuMessage.EMPTY_USERNAME;
        if (password == null || password.isEmpty()) return LoginMenuMessage.EMPTY_PASSWORD;
        User user = User.getUserByUsername(username);
        if (user == null) return LoginMenuMessage.USER_NOT_FOUND;
        if (!user.getPassword().equals(password)) return LoginMenuMessage.INCORRECT_PASSWORD;
        User.setCurrentUser(user);
        return LoginMenuMessage.LOGIN_SUCCESSFUL;
    }
}
