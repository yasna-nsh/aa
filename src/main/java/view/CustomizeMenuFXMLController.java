package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.User;
import model.game.Difficulty;
import model.game.Game;
import model.game.GameTemplate;

public class CustomizeMenuFXMLController {
    public VBox difficultyBox;
    public VBox templateBox;
    public VBox ballCountBox;
    public Label ballCountLabel;
    public ToggleGroup difficultyGroup;
    public ToggleGroup templateGroup;
    private Slider ballSlider;

    @FXML
    public void initialize() {
        difficultyGroup = new ToggleGroup();
        difficultyBox.getChildren().addAll(CommonActions.initializeOptions(difficultyGroup, User.getCurrentUser().getDefaultDifficulty().getName(), Difficulty.getStringValues()));
        templateGroup = new ToggleGroup();
        templateBox.getChildren().addAll(CommonActions.initializeOptions(templateGroup, User.getCurrentUser().getDefaultGameTemplate().getTemplateName(), GameTemplate.getStringValues()));
        ballSlider = CommonActions.showBallCount(ballCountLabel);
        ballCountBox.getChildren().add(ballSlider);
    }

    public void done() throws Exception {
        Difficulty difficulty = Difficulty.getDifficultyByName(((ToggleButton) difficultyGroup.getSelectedToggle()).getText());
        GameTemplate gameTemplate = GameTemplate.getTemplateByName(((ToggleButton) templateGroup.getSelectedToggle()).getText());
        assert gameTemplate != null;
        new GameMenu(new Game(difficulty, gameTemplate, (int) ballSlider.getValue())).start(StartMenu.stage);
    }

    public void cancel() throws Exception {
        new MainMenu().start(StartMenu.stage);
    }
}
