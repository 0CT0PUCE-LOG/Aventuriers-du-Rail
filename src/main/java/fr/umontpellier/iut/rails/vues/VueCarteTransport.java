package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.security.KeyStore;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Button {

    private final ICarteTransport carteTransport;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes) {
        this.carteTransport = carteTransport;
        ImageView image = new ImageView(getFichierCarteTransport());
        image.setFitHeight(100);
        image.setFitWidth(150);
        setGraphic(image);
        setOnAction(actionEvent -> {((VueDuJeu) getScene().getRoot()).getJeu().uneCarteTransportAEteChoisie(carteTransport);});
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
