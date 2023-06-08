package fr.umontpellier.iut.rails.vues;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class MovingButton extends Button {

    private static final double SCALE_FACTOR = 1.1;
    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private static final double MOVE_DISTANCE = 75.0;

    private TranslateTransition translateTransition;
    private ScaleTransition scaleTransition;

    public MovingButton() {
        setOnMouseEntered(event -> scaleUp());
        setOnMouseExited(event -> scaleDown());
    }

    private void scaleUp() {
        scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(SCALE_FACTOR);
        scaleTransition.setToY(SCALE_FACTOR);
        scaleTransition.play();
    }

    private void scaleDown() {
        scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    public void moveToLeftIfSpaceAvailable() {
            moveToLeft();
    }

    private boolean isSpaceAvailable() {
        Region parent = (Region) getParent();
        return getBoundsInParent().getMinX() > parent.getLayoutX() + MOVE_DISTANCE;
    }

    public void moveToLeft() {
        translateTransition = new TranslateTransition(ANIMATION_DURATION, this);
        translateTransition.setByX(getLayoutX()-getTranslateX()-(getHeight()-10));
        translateTransition.play();
    }
    public void moveToRight(){
        translateTransition = new TranslateTransition(ANIMATION_DURATION, this);
        translateTransition.setByX(getLayoutX()-getTranslateX());
        translateTransition.play();
    }

    public void checkForElementRemoval() {
        if (!isSpaceAvailable()) {
            moveToLeft();
        }
    }
}
