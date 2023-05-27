package view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.User;
import model.game.Difficulty;
import model.game.GameScoringData;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardMenuFXMLController {
    public GridPane scoreTable;
    public VBox difficultyFilterBox;
    private ToggleGroup difficultyFilterGroup;

    public void prepareScoreboard() {
        difficultyFilterGroup = new ToggleGroup();
        ArrayList<String> options = new ArrayList<>();
        options.add("all");
        options.addAll(List.of(Difficulty.getStringValues()));
        difficultyFilterBox.getChildren().addAll(CommonActions.initializeOptions(difficultyFilterGroup, "all", options.toArray(new String[0])));
        updateRanking();
        difficultyFilterGroup.selectedToggleProperty().addListener(observable -> updateRanking());
    }

    private void updateRanking() {
        scoreTable.getChildren().clear();
        scoreTable.add(new Label("username"), 0, 0);
        scoreTable.add(new Label("score"), 1, 0);
        scoreTable.add(new Label("time"), 2, 0);
        scoreTable.add(new Label("difficulty"), 3, 0);
        Difficulty selectedDifficulty;
        if (difficultyFilterGroup.getSelectedToggle() == null) return;
        selectedDifficulty = Difficulty.getDifficultyByName(((ToggleButton) difficultyFilterGroup.getSelectedToggle()).getText());

        ArrayList<GameScoringData> scoreboardData = User.getScoreboardData(selectedDifficulty);
        for (int i = 0; i < 10 && i < scoreboardData.size(); i++) {
            scoreTable.add(new Label(scoreboardData.get(i).getUsername()), 0, i + 1);
            scoreTable.add(new Label(String.valueOf(scoreboardData.get(i).getScore())), 1, i + 1);
            scoreTable.add(new Label(String.valueOf(scoreboardData.get(i).getTimeSpent())), 2, i + 1);
            scoreTable.add(new Label(scoreboardData.get(i).getDifficulty().getName()), 3, i + 1);
        }
        for (Node child : scoreTable.getChildren()) {
            if (GridPane.getRowIndex(child) == 1) child.getStyleClass().add("first");
            else if (GridPane.getRowIndex(child) == 2) child.getStyleClass().add("second");
            else if (GridPane.getRowIndex(child) == 3) child.getStyleClass().add("third");
            else continue;
            child.getStyleClass().add("topThree");
        }
    }

    public void goBack() throws Exception {
        new MainMenu().start(StartMenu.stage);
    }
}
