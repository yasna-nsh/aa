package model.game;

import controller.GameController;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.GameMenuFXMLController;

public class BallObject {
    private final Ball ballData;
    private final StackPane ballView;
    private final TranslateTransition ballTransition;

    public BallObject(Ball ballData, String ballText, GameMenuFXMLController controller) {
        //TODO: add different function for making on target balls
        this.ballData = ballData;
        Circle circle = new Circle(Ball.getRadius());
        circle.getStyleClass().add("ball");
        Label number = new Label(ballText);
        number.getStyleClass().add("ballNumber");
        ballView = new StackPane(circle, number);

        // initialize shooting transition
        ballTransition = new TranslateTransition(Duration.millis(Game.getShootTimeInMillis()), ballView);

        // detect collision
        ballView.boundsInParentProperty().addListener((observable, oldValue, newValue) -> controller.detectBallTargetCollision(this));
    }

    public void updateTransition(GameMenuFXMLController controller) {
        double paneDiameter = Math.sqrt(Math.pow(controller.gamePane.getMinWidth(), 2) + Math.pow(controller.gamePane.getMinHeight(), 2));
        ballTransition.setToX(ballView.getTranslateX() + paneDiameter * Math.sin(Math.toRadians(GameController.getInstance().getCurrentGame().getShootAngle())));
        ballTransition.setToY(ballView.getTranslateY() - paneDiameter * Math.cos(Math.toRadians(GameController.getInstance().getCurrentGame().getShootAngle())));
        ballTransition.setCycleCount(1);
    }

    public Ball getBallData() {
        return ballData;
    }

    public StackPane getBallView() {
        return ballView;
    }

    public Circle getBallCircle() {
        for (Node child : ballView.getChildren())
            if (child instanceof Circle) return (Circle) child;
        return null;
    }

    public TranslateTransition getBallTransition() {
        return ballTransition;
    }
}
