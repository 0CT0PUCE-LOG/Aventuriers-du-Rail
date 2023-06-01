package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {
    @FXML
    private Label nomJoueur;

    private IJoueur joueurCourant;

    @FXML
    private FlowPane carteTransportJoueurFlowPane;

    @FXML
    private FlowPane carteTransportPoseesJoueurFlowPane;

    @FXML
    private Label spriteJoueur;

    @FXML
    private Label scoreJoueur;

    @FXML
    private Label nbPionsBateauJoueur;

    @FXML
    private Label nbPionsWagonJoueur;

    @FXML
    private VBox destinationsJoueurCourant;

    public VueJoueurCourant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueJoueurCourant.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void creerBindings() {
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                joueurCourant = t1;
                nomJoueur.setText(joueurCourant.getNom());
                scoreJoueur.setText(String.valueOf(joueurCourant.getScore()));
                nbPionsWagonJoueur.setText(String.valueOf(joueurCourant.getNbPionsWagon()));
                nbPionsBateauJoueur.setText(String.valueOf(joueurCourant.getNbPionsBateau()));
                chargerCarteJoueurCourant();
                chargerSpriteJoueur();
                chargerDestinationJoueur();
                chargerCartePoseesJoueurCourant();
            }
        });
    }


    public void chargerCarteJoueurCourant(){
        carteTransportJoueurFlowPane.getChildren().clear();
        for(ICarteTransport carte : joueurCourant.getCartesTransport()){
            carteTransportJoueurFlowPane.getChildren().add(new VueCarteTransport(carte, 1));
        }
    }

    public void chargerCartePoseesJoueurCourant() {
        carteTransportPoseesJoueurFlowPane.getChildren().clear();
        for (ICarteTransport carte : joueurCourant.getCartesTransport()) {
            carteTransportPoseesJoueurFlowPane.getChildren().add(new VueCarteTransport(carte, 1));
        }
    }

    public void chargerSpriteJoueur(){
        ImageView image = new ImageView("images/cartesWagons/avatar-" + joueurCourant.getCouleur() + ".png");
        image.setPreserveRatio(true);
        image.setFitHeight(200);
        spriteJoueur.setGraphic(image);
    }

    public void chargerDestinationJoueur(){
        destinationsJoueurCourant.getChildren().clear();
        for(IDestination d : joueurCourant.getDestinations()){
            destinationsJoueurCourant.getChildren().add(new Label(d.getVilles().toString()));
        }
    }

    private VueCarteTransport trouveVueCarteTransport(ICarteTransport carteTransport){
        for(Node c : carteTransportJoueurFlowPane.getChildren()) {
            c = (VueCarteTransport) c;
            if (((VueCarteTransport) c).getCarteTransport().equals(carteTransport)) {
                return ((VueCarteTransport) c);
            }
        }
        return null;
    }
}
