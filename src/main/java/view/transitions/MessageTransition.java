package view.transitions;

import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MessageTransition extends Transition {
    private final int rate;
    private final Label messageLabel;
    private final String message;
    private int index;

    public MessageTransition(int rate, Label messageLabel, String message) {
        this.rate = rate;
        this.messageLabel = messageLabel;
        this.message = message;
        index = 1;
        setCycleDuration(Duration.INDEFINITE);
        setCycleCount(1);
    }

    @Override
    protected void interpolate(double frac) {
        if (index / rate > message.length()) stop();
        if (index % rate == 1) messageLabel.setText(message.substring(0, index / rate));
        index++;
    }
}
