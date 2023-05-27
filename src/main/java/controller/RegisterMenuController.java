package controller;

import model.User;
import view.messages.RegisterMenuMessage;

public class RegisterMenuController {
    private static final RegisterMenuController instance = new RegisterMenuController();

    private RegisterMenuController() {
    }

    public static RegisterMenuController getInstance() {
        return instance;
    }

    public RegisterMenuMessage registerFirstStep(String username, String password, String passwordConfirmation) {
        if (username == null || username.isEmpty()) return RegisterMenuMessage.EMPTY_USERNAME;
        if (password == null || password.isEmpty()) return RegisterMenuMessage.EMPTY_PASSWORD;
        if (!password.equals(passwordConfirmation)) return RegisterMenuMessage.PASSWORDS_DONT_MATCH;
        User user = User.getUserByUsername(username);
        if (user != null) return RegisterMenuMessage.TAKEN_USERNAME;
        return RegisterMenuMessage.SUCCESSFUL_REGISTER;
    }
}
