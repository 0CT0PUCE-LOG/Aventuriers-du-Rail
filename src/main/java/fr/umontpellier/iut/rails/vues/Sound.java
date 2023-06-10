package fr.umontpellier.iut.rails.vues;

import javafx.scene.media.AudioClip;

import java.io.File;

public class Sound {

    private AudioClip soundtrack;

    public Sound() {
        this.soundtrack = new AudioClip(new File("src/main/resources/sound/soundtrack.mp3").toURI().toString());
    }

    public AudioClip getSoundtrack(){
        return soundtrack;
    }
}
