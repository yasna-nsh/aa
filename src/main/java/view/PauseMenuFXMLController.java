package view;

import controller.DataHandler;
import controller.GameController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.User;
import model.game.Game;
import view.transitions.MessageTransition;

public class PauseMenuFXMLController {
    public Label messageLabel;
    public ToggleGroup musicGroup;
    public ToggleGroup colorGroup;
    public ToggleGroup musicMuteGroup;
    public ToggleGroup soundMuteGroup;
    public VBox musicBox;
    public VBox twoChoiceBox;
    public HBox colorBox;
    public HBox musicMuteBox;
    public HBox soundMuteBox;
    public VBox controlsInfoBox;
    private MessageTransition messageTransition;

    public void preparePauseMenu() {
        musicGroup = new ToggleGroup();
        musicBox.getChildren().addAll(CommonActions.initializeMusic(musicGroup));
        for (Node child : musicBox.getChildren()) {
            if (child instanceof ToggleButton && User.getCurrentUser().getDefaultMusic().equals(((ToggleButton) child).getText()))
                ((ToggleButton) child).setSelected(true);
        }
        musicMuteGroup.selectToggle((Toggle) musicMuteBox.getChildren().get(User.getCurrentUser().isMusicMutedDefault() ? 2 : 1));
        soundMuteGroup.selectToggle((Toggle) soundMuteBox.getChildren().get(User.getCurrentUser().isSoundMutedDefault() ? 2 : 1));
        colorGroup.selectToggle((Toggle) colorBox.getChildren().get(User.getCurrentUser().isColorfulDefault() ? 1 : 2));
        forceAlwaysSelected(musicGroup, colorGroup, musicMuteGroup, soundMuteGroup);
        String[] names = {"shoot", "freeze", "right", "left"};
        Label controlLabel;
        for (int i = 0; i < names.length; i++) {
            controlLabel = new Label(names[i] + ": " + User.getCurrentUser().getDefaultKeyCodes().get(i).getName());
            controlLabel.getStyleClass().add("keyLabel");
            controlsInfoBox.getChildren().add(controlLabel);
        }
    }

    private void forceAlwaysSelected(ToggleGroup... groups) {
        for (ToggleGroup group : groups)
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) oldValue.setSelected(true);
            });
    }

    public void continueGame() {
        User.getCurrentUser().setDefaultMusic(((ToggleButton) musicGroup.getSelectedToggle()).getText());
        User.getCurrentUser().setMusicMutedDefault(((ToggleButton) musicMuteGroup.getSelectedToggle()).getText().equals("off"));
        User.getCurrentUser().setSoundMutedDefault(((ToggleButton) soundMuteGroup.getSelectedToggle()).getText().equals("off"));
        User.getCurrentUser().setColorfulDefault(((ToggleButton) colorGroup.getSelectedToggle()).getText().equals("on"));
        DataHandler.getInstance().updateDatabase();
        ((GameMenuFXMLController) GameController.getInstance().getLoader().getController()).considerKeyInput();
        ((GameMenuFXMLController) GameController.getInstance().getLoader().getController()).playAllActions();
        CommonActions.setColor(User.getCurrentUser().isColorfulDefault(), (Pane) GameController.getInstance().getCurrentScene().getRoot());
        StartMenu.stage.setScene(GameController.getInstance().getCurrentScene());
        StartMenu.stage.centerOnScreen();
    }

    public void saveGame() {
        User.getCurrentUser().setSavedGame(new Game(GameController.getInstance().getCurrentGame()));
        DataHandler.getInstance().updateDatabase();
        printMessage("save game complete");
    }

    public void restart() throws Exception {
        ((GameMenuFXMLController) GameController.getInstance().getLoader().getController()).restartGame();
    }

    public void quit() throws Exception {
        ((GameMenuFXMLController) GameController.getInstance().getLoader().getController()).quitGame();
    }

    private void printMessage(String message) {
        if (messageTransition != null) messageTransition.stop();
        messageTransition = new MessageTransition(4, messageLabel, message);
        messageTransition.play();
    }
}
