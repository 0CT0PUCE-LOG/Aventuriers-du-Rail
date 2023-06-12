package fr.umontpellier.iut.rails.vues;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;

public class AnimatedButton extends Button {

    private static final double SCALE_FACTOR = 1.1;
    private static final double SCALE_DOWN_FACTOR = 0.95;
    private static final Duration ANIMATION_DURATION = Duration.millis(200);

    public AnimatedButton() {
        setOnMouseEntered(event -> scaleUp());
        setOnMouseExited(event -> scaleDown());

        setOnMousePressed(event -> showPressed());
        setOnMouseReleased(event -> showRealeased());
    }

    protected void scaleUp() {
        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(SCALE_FACTOR);
        scaleTransition.setToY(SCALE_FACTOR);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    protected void scaleDown() {
        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, this);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    private void showPressed(){
        Media media = new Media(new File("src/main/resources/sound/click.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        //reactivate the music when it's finished
        mediaPlayer.setVolume(0.55);
        mediaPlayer.play();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.05), this);
        scaleTransition.setToX(SCALE_DOWN_FACTOR);
        scaleTransition.setToY(SCALE_DOWN_FACTOR);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.05), this);
        fadeTransition.setToValue(0.75);
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setAutoReverse(true);
        parallelTransition.play();
    }

    private void showRealeased(){

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.05), this);
        scaleTransition.setToX(SCALE_FACTOR);
        scaleTransition.setToY(SCALE_FACTOR);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.05), this);
        fadeTransition.setToValue(1.0);
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setAutoReverse(true);
        parallelTransition.play();
    }
}
