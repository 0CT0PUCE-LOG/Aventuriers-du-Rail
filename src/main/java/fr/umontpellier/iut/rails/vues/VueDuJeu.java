package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends VBox {

    private final IJeu jeu;
    private VuePlateau plateau;
    @FXML
    private Button passerBtn;
    @FXML
    private Label instructionLabel;

    @FXML
    private VBox choixDestinationVBox;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueDuJeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        plateau = new VuePlateau();
        getChildren().add(plateau);
         */

    }

    public void creerBindings() {
//        plateau.prefWidthProperty().bind(getScene().widthProperty());
//        plateau.prefHeightProperty().bind(getScene().heightProperty());
//        plateau.creerBindings();
        jeu.destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                while(change.next()){
                    if(change.wasAdded()){
                        for(IDestination destination : change.getAddedSubList()){
                            choixDestinationVBox.getChildren().add(new VueDestination(destination));
                        }
                    }
                    else if(change.wasRemoved()){
                        for(IDestination destination : change.getRemoved()){
                            choixDestinationVBox.getChildren().remove(trouveVueDestination(destination));
                        }
                    }
                }
            }
        });

        instructionLabel.textProperty().bind(jeu.instructionProperty());
    }

    private VueDestination trouveVueDestination(IDestination destination){
        for(Node b : choixDestinationVBox.getChildren()){
            if(((VueDestination)b).getText().equals(destination.getVilles().toString())){
                return ((VueDestination)b);
            }
        }
        return null;
    }

    public void passerClicked(){
        System.out.println("Vous passez votre tour");
        jeu.passerAEteChoisi();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
