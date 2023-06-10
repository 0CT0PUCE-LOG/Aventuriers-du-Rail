package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.ICarteTransport;
import fr.umontpellier.iut.rails.IDestination;
import fr.umontpellier.iut.rails.IJeu;
import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.mecanique.data.Couleur;
import fr.umontpellier.iut.rails.mecanique.data.Destination;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;


/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les cartes Transport visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;

    @FXML
    private VuePlateau plateau;
    @FXML
    private Button passerBtn;
    @FXML
    private Label instructionLabel;

    @FXML
    private HBox choixDestinationHBox;

    @FXML
    private VueJoueurCourant vueJoueurCourant;

    @FXML
    private FlowPane cartesTransportVisible;

    @FXML
    private VueAutresJoueurs vueAutreJoueur;

    @FXML
    private AnimatedButton spritePiocheWagon;

    @FXML
    private AnimatedButton spritePiocheBateau;

    @FXML
    private Spinner<Integer> selecteurNombre;

    @FXML
    private Button pionsWagonBtn;

    @FXML
    private Button pionsBateauBtn;

    @FXML
    private Button confirmerNombre;

    @FXML
    private Button piocheDestinationBtn;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VueDuJeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            //mettre un imega customisée sur le curseur de la souris pour tout la vue du jeu
            Image image = new Image("file:src/main/resources/images/cursorbis.png");
            this.setCursor(new ImageCursor(image));
            //mettre l'image pointer.png lorque la souris survole n'importe quel élément clickable
            Image image2 = new Image("file:src/main/resources/images/cursorbis.png");
            this.setOnMouseEntered(mouseEvent -> {
                this.setCursor(new ImageCursor(image2));
            });
            this.setOnMouseExited(mouseEvent -> {
                this.setCursor(new ImageCursor(image));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        chargerTextures();

        Media media = new Media(new File("src/main/resources/sound/soundtrack.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();




        selecteurNombre.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 25, 0));
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();

        passerBtn.setOnAction(actionEvent -> {passerClicked();});
        spritePiocheWagon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                jeu.uneCarteWagonAEtePiochee();
            }
        });

        spritePiocheBateau.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                jeu.uneCarteBateauAEtePiochee();
            }
        });

        jeu.destinationsInitialesProperty().addListener(new ListChangeListener<IDestination>() {
            @Override
            public void onChanged(Change<? extends IDestination> change) {
                vueJoueurCourant.chargerDestinationJoueur();
                while(change.next()){
                    if(change.wasAdded()){
                        for(IDestination destination : change.getAddedSubList()){
                            choixDestinationHBox.getChildren().add(new VueDestination(destination));
                        }
                    }
                    else if(change.wasRemoved()){
                        for(IDestination destination : change.getRemoved()){
                            choixDestinationHBox.getChildren().remove(trouveVueDestination(destination));
                        }
                    }
                }
            }
        });
        jeu.cartesTransportVisiblesProperty().addListener(new ListChangeListener<ICarteTransport>() {
            @Override
            public void onChanged(Change<? extends ICarteTransport> change) {
                while(change.next()){
                    System.out.println("change");
                    if(change.wasRemoved()){
                        for(ICarteTransport carte : change.getRemoved()){
                            trouveVueCarteTransportVisible(carte).toFront();
                            TranslateTransition translation = new TranslateTransition(Duration.seconds(1), trouveVueCarteTransportVisible(carte));
                            double destinationX = trouverScrollpaneVuejoueurCourant().localToScene(0, 0).getX() - trouveVueCarteTransportVisible(carte).localToScene(0, 0).getX() + trouverScrollpaneVuejoueurCourant().prefWidth(-1)/3;
                            double destinationY = trouverScrollpaneVuejoueurCourant().localToScene(0, 0).getY() - trouveVueCarteTransportVisible(carte).localToScene(0, 0).getY() + trouverScrollpaneVuejoueurCourant().prefHeight(-1)/3;
                            translation.setToX(destinationX);
                            translation.setToY(destinationY);
                            FadeTransition fade = new FadeTransition(Duration.seconds(1), trouveVueCarteTransportVisible(carte));
                            fade.setFromValue(1.0);
                            fade.setToValue(0.2);
                            ParallelTransition parallelTransition = new ParallelTransition(trouveVueCarteTransportVisible(carte), translation, fade);
                            parallelTransition.setOnFinished(event -> {
                                chargerCartesTransportVisible();
                            });
                            parallelTransition.play();
                        }
                    }
                    else{
                        chargerCartesTransportVisible();
                    }
                }
            }
        });

        vueJoueurCourant.creerBindings();
        for(IJoueur j : jeu.getJoueurs()){
            j.cartesTransportProperty().addListener(new ListChangeListener<ICarteTransport>() {
                @Override
                public void onChanged(Change<? extends ICarteTransport> change) {
                    vueJoueurCourant.chargerCarteJoueurCourant();
                }
            });

            j.cartesTransportPoseesProperty().addListener(new ListChangeListener<ICarteTransport>() {
                @Override
                public void onChanged(Change<? extends ICarteTransport> change) {
                    vueJoueurCourant.chargerCartePoseesJoueurCourant();
                }
            });
        }
        instructionLabel.textProperty().bind(jeu.instructionProperty());

        selecteurNombre.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals(KeyCode.ENTER)){
                    nbPionsChoisi();
                }
            }
        });
        confirmerNombre.setOnAction(event -> {nbPionsChoisi();});

        pionsWagonBtn.setOnAction(event -> {jeu.nouveauxPionsWagonsDemandes();});
        pionsBateauBtn.setOnAction(event -> {jeu.nouveauxPionsBateauxDemandes();});
        piocheDestinationBtn.setOnAction(event -> {jeu.nouvelleDestinationDemandee();});

        vueJoueurCourant.prefHeightProperty().bind(plateau.prefHeightProperty());
        vueJoueurCourant.maxHeightProperty().bind(plateau.maxHeightProperty());

        vueAutreJoueur.creerBindings();
    }

    private VueDestination trouveVueDestination(IDestination destination){
        for(Node b : choixDestinationHBox.getChildren()){
            if(((VueDestination)b).getText().equals(destination.getVilles().toString())){
                return ((VueDestination)b);
            }
        }
        return null;
    }

    private VueCarteTransport trouveVueCarteTransportVisible(ICarteTransport carteTransport){
        for(Node d : cartesTransportVisible.getChildren()){
            if(((VueCarteTransport)d).getCarteTransport().equals(carteTransport)){
                return ((VueCarteTransport)d);
            }
        }
        return null;
    }

    public void passerClicked(){
        System.out.println("Vous passez votre tour");
        jeu.passerAEteChoisi();
    }

    public void nbPionsChoisi(){
        jeu.leNombreDePionsSouhaiteAEteRenseigne(String.valueOf(selecteurNombre.getValue()));
        selecteurNombre.getValueFactory().setValue(0);
    }

    public void chargerCartesTransportVisible(){
        cartesTransportVisible.getChildren().clear();
        for(ICarteTransport carte : jeu.cartesTransportVisiblesProperty()){
            cartesTransportVisible.getChildren().add(new VueCarteTransport(carte, 0));
        }
    }

    public Node trouverScrollpaneVuejoueurCourant(){
        for(Node n : vueJoueurCourant.getChildren()){
            if(n instanceof ScrollPane){
                return (ScrollPane)n;
            }
        }
        return null;
    }

    public void chargerTextures(){
        ImageView image = new ImageView("images/cartesWagons/dos-WAGON.png");
        image.setPreserveRatio(true);
        image.setFitHeight(100);
        spritePiocheWagon.setGraphic(image);

        image = new ImageView("images/cartesWagons/dos-BATEAU.png");
        image.setPreserveRatio(true);
        image.setFitHeight(100);
        spritePiocheBateau.setGraphic(image);

        image = new ImageView("images/bouton-pions-wagon.png");
        image.setPreserveRatio(true);
        image.setFitHeight(50);
        pionsWagonBtn.setGraphic(image);

        image = new ImageView("images/bouton-pions-bateau.png");
        image.setPreserveRatio(true);
        image.setFitHeight(50);
        pionsBateauBtn.setGraphic(image);

        image = new ImageView("images/cartesWagons/destinations.png");
        image.setPreserveRatio(true);
        image.setFitWidth(160);
        piocheDestinationBtn.setGraphic(image);
    }

    public static String getCouleurValue(IJoueur.CouleurJoueur couleur){
        switch(couleur){
            case JAUNE:
                return "#b38c00";
            case ROUGE:
                return "#750004";
            case VERT:
                return "#0a3600";
            case ROSE:
                return "#420075";
            case BLEU:
                return "#00128a";
            default:
                System.out.println("couleur"+couleur.toString()+" non reconnue");
                return "#000000";
        }
    }

    public IJeu getJeu() {
        return jeu;
    }

    public VueJoueurCourant getVueJoueurCourant(){return vueJoueurCourant;};

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

    public VuePlateau getVuePlateau() {
        return plateau;
    }
}
