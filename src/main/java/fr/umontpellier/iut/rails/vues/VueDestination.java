package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Cette classe représente la vue d'une carte Destination.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueDestination extends AnimatedButton {

    private final IDestination destination;

    public VueDestination(IDestination destination) {
        this.destination = destination;
        setText(destination.getVilles().toString());
        setOnAction(actionEvent -> {((VueDuJeu) getScene().getRoot()).getJeu().uneDestinationAEteChoisie(destination);});
        setOnMouseEntered(event -> {
            scaleUp();
            System.out.println("montrer les villes de la destination");
            ((VueDuJeu) getScene().getRoot()).getVuePlateau().setFlash(destination.getVilles(), true);
            ((VueDuJeu) getScene().getRoot()).getVuePlateau().setRouteSurbrillance(destination.getVilles(), true);
        });
        setOnMouseExited(event -> {
            scaleDown();
            System.out.println("cacher les villes de la destination");
            ((VueDuJeu) getScene().getRoot()).getVuePlateau().setFlash(destination.getVilles(), false);
            ((VueDuJeu) getScene().getRoot()).getVuePlateau().setRouteSurbrillance(destination.getVilles(), false);
        });
    }



    public IDestination getDestination() {
        return destination;
    }

}
