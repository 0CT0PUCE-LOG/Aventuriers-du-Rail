package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import java.io.IOException;


/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {
    private IJeu jeu;

    public VueAutresJoueurs(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueAutresJoueurs.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creerBindings(){
        jeu = ((VueDuJeu) getScene().getRoot()).getJeu();
        jeu.joueurCourantProperty().addListener(new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur joueur, IJoueur t1) {
                if(joueur != null){
                    getChildren().add(new VueJoueur(joueur));
                }
                getChildren().removeIf(vueJoueur ->{
                        return ((VueJoueur)vueJoueur).getJoueur().equals(t1);
                    });
            }
        });

        chargerJoueurs();
    }

    public void chargerJoueurs(){
        for(int i=0; i<jeu.getJoueurs().size(); i++){
            getChildren().add(new VueJoueur(jeu.getJoueurs().get(i)));
        }
    }
}
