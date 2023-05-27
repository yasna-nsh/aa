package model.game;

import controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.User;

public class TargetObject {
    private final StackPane targetView;
    private final Circle insideCircle;
    private final Timeline targetRotation;
    private final double centerX;
    private final double centerY;

    public TargetObject(Pane gamePane) {
        targetView = new StackPane();
        insideCircle = new Circle(Target.getInnerRadius());
        insideCircle.getStyleClass().add("target-inside");
        updateTargetBackground();
        Circle outsideCircle = new Circle(Target.getOuterRadius());
        outsideCircle.getStyleClass().add("target-outside");
        targetView.getChildren().addAll(outsideCircle, insideCircle);
        centerX = gamePane.getTranslateX() + gamePane.getMinWidth() / 2;
        targetView.setTranslateX(centerX - outsideCircle.getRadius());
        centerY = Target.getOuterRadius() + 30;
        targetView.setTranslateY(centerY - outsideCircle.getRadius());

        targetRotation = new Timeline();
        targetRotation.setCycleCount(Timeline.INDEFINITE);
        targetRotation.setRate(1);
        targetRotation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(targetView.rotateProperty(), 0)),
                new KeyFrame(Duration.seconds(2 * GameController.getInstance().getCurrentGame().getDifficulty().getSpinDuration()), new KeyValue(targetView.rotateProperty(), 360)));
    }

    public StackPane getTargetView() {
        return targetView;
    }

    public Timeline getTargetRotation() {
        return targetRotation;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void updateTargetBackground() {
        insideCircle.setFill(new ImagePattern(new Image(User.getCurrentUser().getAvatarFilePath())));
    }
}
