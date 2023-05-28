package view;

import controller.DataHandler;
import controller.GameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.User;
import model.game.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GameMenuFXMLController {
    public HBox mainPane;
    public VBox leftPane;
    public VBox controlPane;
    public VBox infoPane;
    public Pane gamePane;
    public Button restartButton;
    public Button quitButton;
    public Label freezeLabel;
    private ProgressBar freezeBar;
    private Label angleLabel;
    private Label timerLabel;
    private Label scoreLabel;
    private Label ballCountLabel;
    private final DoubleProperty radiusProperty = new SimpleDoubleProperty();
    private final DoubleProperty opacityProperty = new SimpleDoubleProperty(1);
    private final ArrayList<BallObject> ballQueue = new ArrayList<>();
    private final ArrayList<BallObject> ballsInAir = new ArrayList<>();
    private final ArrayList<BallObject> attachedBalls = new ArrayList<>();
    private TargetObject targetObject;
    private Timeline freezeTimeline;
    private Timeline changeDirectionTimeline;
    private Timeline resizeTimeline;
    private Timeline checkAttachedBallsCollision;
    private Timeline opacityTimeline;
    private Timeline angleChangeTimeline;
    private Timeline updateTimerTimeline;
    private int timeLeft;
    private final Media shootMedia = new Media(new File("src/main/resources/soundEffects/shoot.mp3").toURI().toString());

    public Timeline getChangeDirectionTimeline() {
        return changeDirectionTimeline;
    }

    public void prepareGame(Game currentGame) {
        GameController.getInstance().setCurrentGame(currentGame);
        gamePane.setMinSize(StartMenu.stage.getScene().getWidth() - leftPane.getWidth(), StartMenu.stage.getScene().getHeight());
        gamePane.setPrefSize(StartMenu.stage.getScene().getWidth() - leftPane.getWidth(), StartMenu.stage.getScene().getHeight());
        gamePane.setMaxSize(StartMenu.stage.getScene().getWidth() - leftPane.getWidth(), StartMenu.stage.getScene().getHeight());
        initializeTarget();

        currentGame.setShootingSiteX(gamePane.getTranslateX() + gamePane.getMinWidth() / 2 - Ball.getRadius());
        currentGame.setShootingSiteY(gamePane.getTranslateY() + gamePane.getMinHeight() - Ball.getRadius() * 2 - 10);
        makeBalls();

        initializeFreezeBar();
        freezeTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> targetObject.getTargetRotation().setRate(0.2)),
                new KeyFrame(Duration.seconds(currentGame.getDifficulty().getFreezeTime()), event -> targetObject.getTargetRotation().setRate(1)));
        freezeTimeline.setOnFinished(event -> targetObject.getTargetRotation().setRate(1));
        freezeTimeline.setCycleCount(1);
        //TODO: add animation to freeze

        angleLabel = new Label();
        angleLabel.getStyleClass().add("gameLabel");
        updateAngleLabel();
        infoPane.getChildren().add(angleLabel);
        angleChangeTimeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            currentGame.changeShootAngle(currentGame.getDifficulty().getWindSpeed() / 5);
            updateAngleLabel();
        }));
        angleChangeTimeline.setCycleCount(Animation.INDEFINITE);

        timeLeft = Game.getInitialTimeInSecs() - currentGame.getTimeSpent();
        timerLabel = new Label();
        timerLabel.getStyleClass().add("gameLabel");
        updateTimeLabel();
        infoPane.getChildren().add(timerLabel);
        updateTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft -= updateTimerTimeline.getCycleDuration().toSeconds();
            if (timeLeft < 0) looseSequence();
            updateTimeLabel();
        }));
        updateTimerTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimerTimeline.play();

        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("gameLabel");
        updateScoreLabel();
        infoPane.getChildren().add(scoreLabel);

        ballCountLabel = new Label();
        ballCountLabel.getStyleClass().add("gameLabel");
        updateBallCountLabel();
        infoPane.getChildren().add(ballCountLabel);

        changeDirectionTimeline = new Timeline(new KeyFrame(Duration.seconds(new Random().nextInt(6) + 4), event -> changeRotationDirection()));
        changeDirectionTimeline.setOnFinished(event -> {
            changeDirectionTimeline.getKeyFrames().clear();
            changeDirectionTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(new Random().nextInt(6) + 4), e -> changeRotationDirection()));
            changeDirectionTimeline.play();
        });
        changeDirectionTimeline.setCycleCount(1);

        radiusProperty.set(Ball.getRadius());
        resizeTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(radiusProperty, Ball.getRadius())),
                new KeyFrame(Duration.millis(1000), new KeyValue(radiusProperty, Ball.getRadius() * 1.5)));
        resizeTimeline.setAutoReverse(true);
        resizeTimeline.setCycleCount(Animation.INDEFINITE);

        checkAttachedBallsCollision = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            detectOutOfWindow();
            for (int i = 0; i < attachedBalls.size(); i++)
                for (int j = i + 1; j < attachedBalls.size(); j++)
                    if (isGameOver(attachedBalls.get(i), attachedBalls.get(j))) {
                        looseSequence();
                        return;
                    }
        }));
        checkAttachedBallsCollision.setCycleCount(Animation.INDEFINITE);

        opacityTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty, 1)),
                new KeyFrame(Duration.millis(400), new KeyValue(opacityProperty, 1)),
                new KeyFrame(Duration.millis(600), new KeyValue(opacityProperty, 0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(opacityProperty, 0)));
        opacityTimeline.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (opacityTimeline.getStatus() == Animation.Status.STOPPED) opacityProperty.setValue(1);
        });
        opacityTimeline.setAutoReverse(true);
        opacityTimeline.setCycleCount(Animation.INDEFINITE);

        if (CommonActions.getSelectedTrack() != null && !User.getCurrentUser().isMusicMutedDefault())
            CommonActions.getSelectedTrack().play();
        GameController.getInstance().applyPhaseActions(this);
    }

    public void changeRotationDirection() {
        GameController.getInstance().getCurrentGame().setClockwise(!GameController.getInstance().getCurrentGame().isClockwise());
        targetObject.getTargetRotation().setRate(-targetObject.getTargetRotation().getRate());
    }

    public void startResizeTimeline() {
        resizeTimeline.play();
        checkAttachedBallsCollision.play();
    }

    public void startOpacityTimeline() {
        opacityTimeline.play();
    }

    public void startAngleChangeTimeLine() {
        angleChangeTimeline.play();
    }

    private void initializeTarget() {
        targetObject = new TargetObject(gamePane);
        gamePane.getChildren().add(targetObject.getTargetView());
        targetObject.getTargetRotation().play();
    }

    private void initializeFreezeBar() {
        freezeLabel = new Label("freeze");
        freezeLabel.getStyleClass().add("gameLabel");
        freezeBar = new ProgressBar((double) GameController.getInstance().getCurrentGame().getFreezeProgress() / Game.getFreezeCapacity());
        freezeBar.getStyleClass().add("gameProgressBar");
        freezeLabel.setLabelFor(freezeBar);
        infoPane.getChildren().addAll(freezeLabel, freezeBar);
    }

    private void updateAngleLabel() {
        angleLabel.setText("angle\n" + String.format("%.2f", GameController.getInstance().getCurrentGame().getShootAngle()) + "°");
    }

    private void updateTimeLabel() {
        timerLabel.setText("time\n" + String.format("%02d", timeLeft / 60) + ":" + String.format("%02d", timeLeft % 60));
    }


    private void updateScoreLabel() {
        scoreLabel.setText("score\n" + GameController.getInstance().getCurrentGame().getScore());
    }

    private void updateBallCountLabel() {
        ballCountLabel.setText("ball count\n" + GameController.getInstance().getCurrentGame().getRemainingBalls().size());
        switch (GameController.getInstance().getCurrentGame().getPhase()) {
            case 1:
                ballCountLabel.setStyle("-fx-text-fill: #ff4b4b");
                break;
            case 2:
                ballCountLabel.setStyle("-fx-text-fill: #f3a91b");
                break;
            case 3:
                ballCountLabel.setStyle("-fx-text-fill: #f1f163");
                break;
            case 4:
                ballCountLabel.setStyle("-fx-text-fill: #8cf18c");
                break;
        }
    }

    private void makeBalls() {
        ArrayList<Ball> attachedBalls = GameController.getInstance().getCurrentGame().getAttachedBalls();
        ArrayList<Ball> remainingBalls = GameController.getInstance().getCurrentGame().getRemainingBalls();
        BallObject ballObject;
        String text;
        int number = GameController.getInstance().getCurrentGame().getBallCount();
        for (Ball attachedBall : attachedBalls) {
            if (attachedBall.isInTemplate()) text = "";
            else {
                text = String.valueOf(number);
                number--;
            }
            ballObject = new BallObject(attachedBall, text, this);
            this.attachedBalls.add(ballObject);
            ballObject.getBallCircle().radiusProperty().bind(radiusProperty);
            ballObject.getBallView().opacityProperty().bind(opacityProperty);
            attachBallView(ballObject, Math.toRadians(attachedBall.getAttachedDegree()));
        }
        for (int i = 0; i < remainingBalls.size(); i++)
            ballQueue.add(new BallObject(remainingBalls.get(i), String.valueOf(remainingBalls.size() - i), this));
        showNewBall();
    }

    private void showNewBall() {
        if (ballQueue.size() == 0) return;
        ballQueue.get(0).getBallView().setTranslateX(GameController.getInstance().getCurrentGame().getShootingSiteX());
        ballQueue.get(0).getBallView().setTranslateY(GameController.getInstance().getCurrentGame().getShootingSiteY());
        gamePane.getChildren().add(ballQueue.get(0).getBallView());
    }

    public void shoot() {
        if (ballQueue.size() == 0) return;
        ballsInAir.add(ballQueue.get(0));
        ballQueue.get(0).updateTransition(this);
        ballQueue.get(0).getBallTransition().play();
        ballQueue.remove(0);
        showNewBall();
        MediaPlayer shootSoundEffect = new MediaPlayer(shootMedia);
        shootSoundEffect.setMute(User.getCurrentUser().isSoundMutedDefault());
        shootSoundEffect.setAutoPlay(true);
    }

    public void freeze() {
        Game currentGame = GameController.getInstance().getCurrentGame();
        if (currentGame.getFreezeProgress() != Game.getFreezeCapacity()) return;
        currentGame.resetFreezeProgress();
        freezeBar.setProgress(0);
        freezeTimeline.stop();
        freezeTimeline.play();
    }

    public void moveBallHorizontally(double change) {
        Game currentGame = GameController.getInstance().getCurrentGame();
        if (currentGame.getPhase() != 4) return;
        if (currentGame.getShootingSiteX() + change < 2 * Ball.getRadius() || currentGame.getShootingSiteX() + change > gamePane.getWidth() - 4 * Ball.getRadius())
            return;
        currentGame.setShootingSiteX(currentGame.getShootingSiteX() + change);
        ballQueue.get(0).getBallView().setTranslateX(currentGame.getShootingSiteX());
    }

    public void detectBallTargetCollision(BallObject ballObject) {
        double targetCenterX = targetObject.getCenterX();
        double targetCenterY = targetObject.getCenterY();
        double ballCenterX = ballObject.getBallView().getTranslateX() + Ball.getRadius();
        double ballCenterY = ballObject.getBallView().getTranslateY() + Ball.getRadius();
        double distance = Math.sqrt(Math.pow(targetCenterX - ballCenterX, 2) + Math.pow(targetCenterY - ballCenterY, 2));
        if (distance > Target.getOuterRadius()) return;

        Game currentGame = GameController.getInstance().getCurrentGame();
        currentGame.attachBall(ballObject.getBallData());
        attachBallView(ballObject);
        ballsInAir.remove(ballObject);
        attachedBalls.add(ballObject);
        ballObject.getBallCircle().radiusProperty().bind(radiusProperty);
        ballObject.getBallView().opacityProperty().bind(opacityProperty);
        currentGame.increaseFreezeProgress();
        freezeBar.setProgress((double) currentGame.getFreezeProgress() / Game.getFreezeCapacity());
        GameController.getInstance().updatePhase(ballQueue.size(), this);
        GameController.getInstance().getCurrentGame().increaseScore(Game.getBallScore());
        updateScoreLabel();
        updateBallCountLabel();
        if (!checkGameOver(ballObject) && ballQueue.isEmpty() && ballsInAir.isEmpty()) winSequence();
    }

    public void detectOutOfWindow() {
        for (BallObject ballObject : ballsInAir)
            if (isOutOfWindow(ballObject)) looseSequence();
    }

    private boolean isOutOfWindow(BallObject ballObject) {
        double ballX = ballObject.getBallView().getTranslateX();
        double ballY = ballObject.getBallView().getTranslateY();
        return ballX < 0 || ballY < 0 || ballX > gamePane.getWidth() - 2 * Ball.getRadius() || ballY > gamePane.getHeight() - 2 * Ball.getRadius();
    }


    private void attachBallView(BallObject ballObject) {
        ballObject.getBallTransition().stop();
        StackPane ballView = ballObject.getBallView();
        StackPane targetView = targetObject.getTargetView();
        double xDifference = ballView.getTranslateX() + Ball.getRadius() - (targetView.getTranslateX() + Target.getOuterRadius());
        double yDifference = ballView.getTranslateY() + Ball.getRadius() - (targetView.getTranslateY() + Target.getOuterRadius());
        double ballTargetAngle = Math.atan(xDifference / yDifference);
        double shootAngle = Math.toRadians(GameController.getInstance().getCurrentGame().getShootAngle());
        double temp = Math.asin(Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2)) * Math.sin(ballTargetAngle - shootAngle) / Target.getOuterRadius());
        if (Double.isNaN(temp)) return;
        double angle = temp + shootAngle + Math.toRadians(targetView.rotateProperty().getValue());
        attachBallView(ballObject, angle);
    }

    private void attachBallView(BallObject ballObject, double angle) {
        double radius = Target.getOuterRadius() - Ball.getRadius();
        ballObject.getBallView().setTranslateX(radius * Math.sin(angle));
        ballObject.getBallView().setTranslateY(radius * Math.cos(angle));
        gamePane.getChildren().remove(ballObject.getBallView());
        targetObject.getTargetView().getChildren().add(ballObject.getBallView());
        ballObject.getBallData().setAttachedDegree(Math.toDegrees(angle));
    }

    private boolean checkGameOver(BallObject ballObject) {
        for (BallObject attachedBall : attachedBalls)
            if (attachedBall != ballObject && isGameOver(ballObject, attachedBall)) {
                looseSequence();
                return true;
            }
        return false;
    }

    private boolean isGameOver(BallObject ball1, BallObject ball2) {
        StackPane ballView1 = ball1.getBallView();
        StackPane ballView2 = ball2.getBallView();
        double distance = Math.sqrt(Math.pow(ballView1.getTranslateX() - ballView2.getTranslateX(), 2) + Math.pow(ballView1.getTranslateY() - ballView2.getTranslateY(), 2));
        return distance <= ball1.getBallCircle().getRadius() * 2;
    }

    private void looseSequence() {
        stopAllActions();
        ignoreKeyInput();
        infoPane.getChildren().clear();
        controlPane.getChildren().clear();
        mainPane.setStyle("-fx-background-color: lose-color");
        restartButton.setText("try again");
        controlPane.getChildren().add(restartButton);
        quitButton.setText("back");
        controlPane.getChildren().add(quitButton);
    }

    private void winSequence() {
        stopAllActions();
        ignoreKeyInput();
        Game currentGame = GameController.getInstance().getCurrentGame();
        currentGame.increaseScore(timeLeft * Game.getTimeScoreCoefficient());
        int timeSpent = Game.getInitialTimeInSecs() - timeLeft;
        currentGame.setTimeSpent(timeSpent);
        User.getCurrentUser().updateHighScore(currentGame.getDifficulty(), currentGame.getScore(), timeSpent);
        DataHandler.getInstance().updateDatabase();

        updateScoreLabel();
        mainPane.setStyle("-fx-background-color: win-color");
        infoPane.getChildren().removeAll(angleLabel, freezeLabel, freezeBar);
        timerLabel.setText("time spent\n" + String.format("%02d", timeSpent / 60) + ":" + String.format("%02d", timeSpent % 60));
        controlPane.getChildren().clear();
        quitButton.setText("back");
        controlPane.getChildren().add(quitButton);
    }

    public void pauseGame() throws Exception {
        pauseAllActions();
        ignoreKeyInput();
        int timeSpent = Game.getInitialTimeInSecs() - timeLeft;
        GameController.getInstance().getCurrentGame().setTimeSpent(timeSpent);
        new PauseMenu().start(StartMenu.stage);
    }

    public void restartGame() throws Exception {
        stopAllActions();
        ignoreKeyInput();
        Game currentGame = GameController.getInstance().getCurrentGame();
        new GameMenu(new Game(currentGame.getDifficulty(), currentGame.getTemplate(), currentGame.getBallCount())).start(StartMenu.stage);
    }

    public void quitGame() throws Exception {
        stopAllActions();
        new MainMenu().start(StartMenu.stage);
    }

    private void stopAllActions() {
        freezeTimeline.stop();
        changeDirectionTimeline.stop();
        resizeTimeline.stop();
        opacityTimeline.stop();
        angleChangeTimeline.stop();
        updateTimerTimeline.stop();
        targetObject.getTargetRotation().stop();
        for (BallObject ballObject : ballsInAir)
            ballObject.getBallTransition().pause();
        checkAttachedBallsCollision.stop();
        CommonActions.stopAllTracks();
    }

    private void pauseAllActions() {
        freezeTimeline.pause();
        changeDirectionTimeline.pause();
        resizeTimeline.pause();
        opacityTimeline.pause();
        angleChangeTimeline.pause();
        updateTimerTimeline.pause();
        targetObject.getTargetRotation().pause();
        for (BallObject ballObject : ballsInAir)
            ballObject.getBallTransition().pause();
        checkAttachedBallsCollision.pause();
        CommonActions.pauseAllTracks();
    }

    private void ignoreKeyInput() {
        GameController.getInstance().setPaused(true);
    }

    public void considerKeyInput() {
        GameController.getInstance().setPaused(false);
    }

    public void playAllActions() {
        if (freezeTimeline.getStatus() == Animation.Status.PAUSED) freezeTimeline.play();
        if (changeDirectionTimeline.getStatus() == Animation.Status.PAUSED) changeDirectionTimeline.play();
        if (resizeTimeline.getStatus() == Animation.Status.PAUSED) resizeTimeline.play();
        if (checkAttachedBallsCollision.getStatus() == Animation.Status.PAUSED) checkAttachedBallsCollision.play();
        if (opacityTimeline.getStatus() == Animation.Status.PAUSED) opacityTimeline.play();
        if (angleChangeTimeline.getStatus() == Animation.Status.PAUSED) angleChangeTimeline.play();
        if (updateTimerTimeline.getStatus() == Animation.Status.PAUSED) updateTimerTimeline.play();
        if (targetObject.getTargetRotation().getStatus() == Animation.Status.PAUSED)
            targetObject.getTargetRotation().play();
        for (BallObject ballObject : ballsInAir)
            if (ballObject.getBallTransition().getStatus() == Animation.Status.PAUSED)
                ballObject.getBallTransition().play();
        if (CommonActions.getSelectedTrack() != null && !User.getCurrentUser().isMusicMutedDefault())
            CommonActions.getSelectedTrack().play();
    }
}
