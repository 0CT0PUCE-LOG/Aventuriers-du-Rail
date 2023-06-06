package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class VueJoueur extends HBox {

    @FXML
    private HBox vueJoueur;
    @FXML
    private Label spriteJoueur;
    @FXML
    private Label scoreJoueur;
    @FXML
    private Label nbPionsWagon;
    @FXML
    private Label nbPionsBateau;

    private IJoueur joueur;

    public VueJoueur(IJoueur joueur){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueJoueur.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.joueur = joueur;

        chargerSpriteJoueur();
        chargerBgJoueur();
        scoreJoueur.setText(String.valueOf(joueur.getScore()));
        nbPionsBateau.setText(String.valueOf(joueur.getNbPionsBateau()));
        nbPionsWagon.setText(String.valueOf(joueur.getNbPionsWagon()));
    }

    public IJoueur getJoueur() {
        return joueur;
    }

    public void chargerSpriteJoueur(){
        ImageView image = new ImageView("images/cartesWagons/avatar-" + joueur.getCouleur() + ".png");
        image.setPreserveRatio(true);
        image.setFitHeight(70);
        spriteJoueur.setGraphic(image);
    }

    public void chargerBgJoueur(){
        vueJoueur.setStyle("-fx-border-color: " + VueDuJeu.getCouleurValue(joueur.getCouleur()) + ";");
    }

}
