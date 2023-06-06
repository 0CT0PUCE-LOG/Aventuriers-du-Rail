package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.security.KeyStore;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends MovingButton {

    private final ICarteTransport carteTransport;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes) {
        this.carteTransport = carteTransport;
        ImageView image = new ImageView(getFichierCarteTransport());
        image.setFitHeight(50);
        image.setFitWidth(75);
        setGraphic(image);
        setOnAction(actionEvent -> {
            ((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(this.carteTransport);
        });
    }

    private String getFichierCarteTransport(){
        String file = "images/cartesWagons/carte-";

        if(carteTransport.estBateau()){
            if(carteTransport.estDouble()){
                file+="DOUBLE-";
            }
            else{
                file+="BATEAU-";
            }
        }
        else if(carteTransport.estWagon()){
            file+="WAGON-";
        }
        else{
            file+="JOKER-";
        }

        file+= carteTransport.getStringCouleur();

        if(carteTransport.getAncre()){
            file+="-A";
        }

        file+=".png";

        return file;
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

}
