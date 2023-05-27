package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.game.Game;
import view.GameMenuFXMLController;

public class GameController {
    private static final GameController instance = new GameController();
    private FXMLLoader loader;
    private Scene currentScene;
    private Game currentGame;
    private boolean isPaused;

    private GameController() {
    }

    public static GameController getInstance() {
        return instance;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void updatePhase(int queueSize, GameMenuFXMLController controller) {
        int shot = currentGame.getBallCount() - queueSize;
        if (shot * 4 < currentGame.getPhase() * currentGame.getBallCount()) return;
        currentGame.setPhase(currentGame.getPhase() + 1);
        currentGame.increaseScore(Game.getPhaseScore());
        if (currentGame.getPhase() == 2) {
            controller.changeRotationDirection();
            controller.getChangeDirectionTimeline().play();
            controller.startResizeTimeline();
        } else if (currentGame.getPhase() == 3) controller.startOpacityTimeline();
        else if (currentGame.getPhase() == 4) controller.startAngleChangeTimeLine();
    }

    public void applyPhaseActions(GameMenuFXMLController controller) {
        if (currentGame.getPhase() >= 2) {
            controller.changeRotationDirection();
            controller.getChangeDirectionTimeline().play();
            controller.startResizeTimeline();
        }
        if (currentGame.getPhase() >= 3) controller.startOpacityTimeline();
        if (currentGame.getPhase() == 4) controller.startAngleChangeTimeLine();
    }
}
