package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJoueur;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    private ImageView ornTop;

    @FXML
    private ImageView ornBot;

    @FXML
    private ScrollPane hoverScroll;

    @FXML
    private ScrollPane hoverScrollPose;

    @FXML
    private ScrollPane hoverScrollDestination;

    @FXML
    private HBox HBoxJoueurCourant;

    @FXML
    private Label scoreJoueur;

    @FXML
    private Label nbPionsBateauJoueur;

    @FXML
    private Label nbPionsWagonJoueur;

    @FXML
    private Label labelDestination;

    @FXML
    private Label labelPose;

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
                chargerBgJoueur();
                //chargerSpriteOrnament();
                chargerDestinationJoueur();
                chargerCartePoseesJoueurCourant();
            }
        });

        etendrePaneVerticallement(hoverScroll, carteTransportJoueurFlowPane);
        etendrePaneVerticallement(hoverScrollPose, 200);
        etendrePaneVerticallementDestination(hoverScrollDestination, destinationsJoueurCourant);
    }

    public void etendrePaneVerticallement(ScrollPane scrollPane, FlowPane child){
        double originalHeight = scrollPane.getPrefHeight();
        scrollPane.setOnMouseEntered(event -> {
            double expandedHeight = checkSize(child.getHeight(), 300);
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), expandedHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
        scrollPane.setOnMouseExited(event -> {
            double expandedHeight = checkSize(child.getHeight(), 300);
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), originalHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    }

    public void etendrePaneVerticallement(ScrollPane scrollPane, double expandedHeight){
        double originalHeight = scrollPane.getPrefHeight();
        scrollPane.setOnMouseEntered(event -> {
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), expandedHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
        scrollPane.setOnMouseExited(event -> {
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), originalHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    }

    public void etendrePaneVerticallementDestination(ScrollPane scrollPane, VBox child){
        double originalHeight = scrollPane.getPrefHeight();
        scrollPane.setOnMouseEntered(event -> {
            double expandedHeight = checkSize(child.getHeight(), 300);
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), expandedHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
        scrollPane.setOnMouseExited(event -> {
            double expandedHeight = checkSize(child.getHeight(), 300);
            Timeline timeline = new Timeline();
            KeyValue heightValue = new KeyValue(scrollPane.minViewportHeightProperty(), originalHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(expandedHeight + 100), heightValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
    }

    public double checkSize(double size, double limit){
        if(size>=limit){
            return limit;
        }else{
            return size;
        }
    }


    public void chargerCarteJoueurCourant(){
        carteTransportJoueurFlowPane.getChildren().clear();
        for(ICarteTransport carte : joueurCourant.getCartesTransport()){
            carteTransportJoueurFlowPane.getChildren().add(new VueCarteTransport(carte, 1));
        }
    }

    public void chargerCartePoseesJoueurCourant() {
        if(joueurCourant.cartesTransportPoseesProperty().size() == 0){
            hoverScrollPose.setManaged(false);
            hoverScrollPose.setVisible(false);
            labelPose.setManaged(false);
            labelPose.setVisible(false);
        }else{
            hoverScrollPose.setVisible(true);
            hoverScrollPose.setManaged(true);
            labelPose.setVisible(true);
            labelPose.setManaged(true);
            carteTransportPoseesJoueurFlowPane.getChildren().clear();
            for (ICarteTransport carte : joueurCourant.cartesTransportPoseesProperty()) {
                carteTransportPoseesJoueurFlowPane.getChildren().add(new VueCarteTransport(carte, 1));
            }
        }
    }

    public void chargerSpriteJoueur(){
        ImageView image = new ImageView("images/cartesWagons/avatar-" + joueurCourant.getCouleur() + ".png");
        image.setPreserveRatio(true);
        image.setFitHeight(150);
        spriteJoueur.setGraphic(image);
        ornTop.setPreserveRatio(true);
        ornTop.setFitHeight(90);
        ornBot.setPreserveRatio(true);
        ornBot.setFitHeight(90);


    }

    public void chargerBgJoueur(){
        HBoxJoueurCourant.setStyle("-fx-border-color: " + VueDuJeu.getCouleurValue(joueurCourant.getCouleur()) + ";");
    }

    public void chargerDestinationJoueur(){
        if(joueurCourant.getDestinations().isEmpty()){
            hoverScrollDestination.setManaged(false);
            hoverScrollDestination.setVisible(false);
            labelDestination.setManaged(false);
            labelDestination.setVisible(false);
        }
        else{
            hoverScrollDestination.setVisible(true);
            hoverScrollDestination.setManaged(true);
            labelDestination.setVisible(true);
            labelDestination.setManaged(true);
            destinationsJoueurCourant.getChildren().clear();
            for(IDestination d : joueurCourant.getDestinations()){
                destinationsJoueurCourant.getChildren().add(new Label(d.getVilles().toString()));
            }
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
