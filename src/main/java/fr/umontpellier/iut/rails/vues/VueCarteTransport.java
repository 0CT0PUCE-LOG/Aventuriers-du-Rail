package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Transport.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteTransport extends Label {

    private final ICarteTransport carteTransport;

    public VueCarteTransport(ICarteTransport carteTransport, int nbCartes) {
        this.carteTransport = carteTransport;
        setGraphic(new ImageView(getFichierCarteTransport()));
    }

    private String getFichierCarteTransport(){
        String file = "cartesWagons/carte-";

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
            file+="A";
        }

        file+=".png";

        return file;
    }

    public ICarteTransport getCarteTransport() {
        return carteTransport;
    }

}
