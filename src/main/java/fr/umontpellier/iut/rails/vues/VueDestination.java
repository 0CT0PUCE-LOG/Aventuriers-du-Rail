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
            System.out.println("montrer les villes de la destination");
            //mettre en surbrillance les villes de la destination
            for (int i = 0; i < destination.getVilles().size(); i++) {
                //((VueDuJeu) getScene().getRoot()).getVuePlateau().getVueVille(destination.getVilles().get(i)).setStyle("-fx-background-color: #ff0000;");
            }
        });
    }
/*
    public String trouverVilleDestination(Destination destination){
        String ville = "";
        for (int i = 0; i < destination.getVilles().size(); i++) {
            ville += destination.getVilles().get(i).getNom();
            if (i != destination.getVilles().size() - 1) {
                ville += " - ";
            }
        }
        return ville;


    }
    */

    public IDestination getDestination() {
        return destination;
    }

}
