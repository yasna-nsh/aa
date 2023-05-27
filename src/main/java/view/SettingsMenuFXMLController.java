package view;

import controller.DataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.User;
import model.game.Difficulty;
import model.game.GameTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsMenuFXMLController {
    public ToggleGroup musicGroup;
    public ToggleGroup difficultyGroup;
    public ToggleGroup templateGroup;
    public ToggleGroup colorGroup;
    public ToggleGroup musicMuteGroup;
    public ToggleGroup soundMuteGroup;
    public VBox mainPane;
    public HBox controlsPane;
    public VBox musicBox;
    public VBox twoChoiceBox;
    public HBox colorBox;
    public HBox musicMuteBox;
    public HBox soundMuteBox;
    public VBox difficultyBox;
    public VBox templateBox;
    public VBox ballCountBox;
    public Label ballCountLabel;
    private Slider ballSlider;
    private ArrayList<String> controlKeysName;
    private ArrayList<KeyCode> controlKeysCode;
    private ArrayList<VBox> controlKeysView;
    int selectedControlIndex;

    @FXML
    public void initialize() {
        musicGroup = new ToggleGroup();
        musicBox.getChildren().addAll(CommonActions.initializeMusic(musicGroup));
        difficultyGroup = new ToggleGroup();
        difficultyBox.getChildren().addAll(CommonActions.initializeOptions(difficultyGroup, User.getCurrentUser().getDefaultDifficulty().getName(), Difficulty.getStringValues()));
        templateGroup = new ToggleGroup();
        templateBox.getChildren().addAll(CommonActions.initializeOptions(templateGroup, User.getCurrentUser().getDefaultGameTemplate().getTemplateName(), GameTemplate.getStringValues()));
        ballSlider = CommonActions.showBallCount(ballCountLabel);
        ballCountBox.getChildren().add(ballSlider);
        forceAlwaysSelected(musicGroup, difficultyGroup, templateGroup, colorGroup, musicMuteGroup, soundMuteGroup);
        selectedControlIndex = -1;
        controlKeysName = new ArrayList<>();
        controlKeysCode = new ArrayList<>();
        controlKeysView = generateControls();
        controlsPane.getChildren().addAll(controlKeysView);
        mainPane.setOnKeyPressed(event -> {
            if (selectedControlIndex == -1) return;
            chooseKey(event.getCode());
            unselectAllControlButtons();
        });
        musicMuteGroup.selectToggle((Toggle) musicMuteBox.getChildren().get(User.getCurrentUser().isMusicMutedDefault() ? 2 : 1));
        soundMuteGroup.selectToggle((Toggle) soundMuteBox.getChildren().get(User.getCurrentUser().isSoundMutedDefault() ? 2 : 1));
        colorGroup.selectToggle((Toggle) colorBox.getChildren().get(User.getCurrentUser().isColorfulDefault() ? 1 : 2));
    }

    private void selectControlButton(int index) {
        selectedControlIndex = index;
        Button button;
        for (int i = 0; i < controlKeysView.size(); i++) {
            button = (Button) controlKeysView.get(i).getChildren().get(0);
            button.getStyleClass().clear();
            if (i == index) {
                button.setText("change " + controlKeysName.get(i) + " key");
                button.getStyleClass().add("selected");
            } else {
                button.setText(controlKeysName.get(i));
                button.getStyleClass().add("option");
            }
        }
    }

    private void unselectAllControlButtons() {
        selectedControlIndex = -1;
        Button button;
        for (int i = 0; i < controlKeysView.size(); i++) {
            button = (Button) controlKeysView.get(i).getChildren().get(0);
            button.setText(controlKeysName.get(i));
            button.getStyleClass().clear();
            button.getStyleClass().add("option");
        }
    }

    private void forceAlwaysSelected(ToggleGroup... groups) {
        for (ToggleGroup group : groups)
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) oldValue.setSelected(true);
            });
    }

    private ArrayList<VBox> generateControls() {
        ArrayList<VBox> result = new ArrayList<>();
        String[] names = {"shoot", "freeze", "right", "left"};
        for (int i = 0; i < names.length; i++)
            result.add(generateControl(names[i], User.getCurrentUser().getDefaultKeyCodes().get(i), i));
        controlKeysName.addAll(Arrays.asList(names));
        controlKeysCode.addAll(User.getCurrentUser().getDefaultKeyCodes());
        return result;
    }

    private VBox generateControl(String name, KeyCode defaultKey, int index) {
        Button button = new Button(name);
        button.getStyleClass().add("option");
        button.setFocusTraversable(false);
        button.setOnMouseClicked(event -> {
            if (selectedControlIndex == index) unselectAllControlButtons();
            else selectControlButton(index);
        });
        Label label = new Label(defaultKey.getName());
        label.getStyleClass().add("keyLabel");
        VBox result = new VBox(button, label);
        result.getStyleClass().add("menuBox");
        return result;
    }

    private void chooseKey(KeyCode keyCode) {
        for (int i = 0; i < controlKeysCode.size(); i++)
            if (selectedControlIndex != i && controlKeysCode.get(i) == keyCode) {
                controlKeysCode.set(i, controlKeysCode.get(selectedControlIndex));
                updateKeyLabel(i);
                break;
            }
        controlKeysCode.set(selectedControlIndex, keyCode);
        updateKeyLabel(selectedControlIndex);
        selectedControlIndex = -1;
    }

    private void updateKeyLabel(int i) {
        Label label = (Label) controlKeysView.get(i).getChildren().get(1);
        label.setText(controlKeysCode.get(i).getName());
    }

    public void saveSettings() throws Exception {
        User.getCurrentUser().setDefaultDifficulty(Difficulty.getDifficultyByName(((ToggleButton) difficultyGroup.getSelectedToggle()).getText()));
        User.getCurrentUser().setDefaultGameTemplate(GameTemplate.getTemplateByName(((ToggleButton) templateGroup.getSelectedToggle()).getText()));
        User.getCurrentUser().setDefaultBallCount((int) ballSlider.getValue());
        User.getCurrentUser().setDefaultMusic(((ToggleButton) musicGroup.getSelectedToggle()).getText());
        User.getCurrentUser().setDefaultKeyCodes(controlKeysCode);
        User.getCurrentUser().setMusicMutedDefault(((ToggleButton) musicMuteGroup.getSelectedToggle()).getText().equals("off"));
        User.getCurrentUser().setSoundMutedDefault(((ToggleButton) soundMuteGroup.getSelectedToggle()).getText().equals("off"));
        User.getCurrentUser().setColorfulDefault(((ToggleButton) colorGroup.getSelectedToggle()).getText().equals("on"));
        DataHandler.getInstance().updateDatabase();
        new MainMenu().start(StartMenu.stage);
    }

    public void exitSettings() throws Exception {
        new MainMenu().start(StartMenu.stage);
    }
}