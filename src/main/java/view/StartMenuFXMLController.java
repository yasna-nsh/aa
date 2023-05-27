package view;

import model.User;

public class StartMenuFXMLController {
    public void startGameAsAGuest() throws Exception {
        User.setCurrentUser(User.getGuest());
        new MainMenu().start(StartMenu.stage);
    }

    public void goToLoginMenu() throws Exception {
        new LoginMenu().start(StartMenu.stage);
    }

    public void goToRegisterMenu() throws Exception {
        new RegisterMenu(false).start(StartMenu.stage);
    }

    public void exit() {
        StartMenu.stage.close();
    }
}
