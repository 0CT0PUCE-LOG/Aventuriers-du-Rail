package fr.umontpellier.iut.rails.vues;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class AnimatedButton extends Button {

    private static final double SCALE_FACTOR = 1.1;
    private static final Duration ANIMATION_DURATION = Duration.millis(200);

    public AnimatedButton() {
        setOnMouseEntered(event -> scaleUp());
        setOnMouseExited(event -> scaleDown());
    }

    private void scaleUp() {
        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(SCALE_FACTOR);
        scaleTransition.setToY(SCALE_FACTOR);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    private void scaleDown() {
        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }
}
