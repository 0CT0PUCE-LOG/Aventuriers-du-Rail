package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
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
    }

    public IDestination getDestination() {
        return destination;
    }

}
