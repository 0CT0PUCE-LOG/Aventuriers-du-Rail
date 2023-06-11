package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IJoueur;
import fr.umontpellier.iut.rails.IRoute;
import fr.umontpellier.iut.rails.IVille;
import javafx.animation.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Cette classe présente les routes et les villes sur le plateau.
 *
 * On y définit les handlers à exécuter lorsque qu'un élément du plateau a été choisi par l'utilisateur
 * ainsi que les bindings qui mettront à jour le plateau après la prise d'une route ou d'un port par un joueur
 */
public class VuePlateau extends Pane {

    @FXML
    private ImageView mapMonde;

    private static final Color GLOW_COLOR = Color.web("#ffaa00");
    private static final double GLOW_RADIUS = 40.0;
    private static final Duration GLOW_DURATION = Duration.seconds(1.5);
    private static final int GLOW_CYCLE_COUNT = Animation.INDEFINITE;

    public VuePlateau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/plateau.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinSize(Screen.getPrimary().getBounds().getWidth()/3, Screen.getPrimary().getBounds().getHeight()/3) ;
    }

    EventHandler<MouseEvent> choixRoute = event -> {
        System.out.println("On a cliqué sur " + event.getPickResult().getIntersectedNode().getId());
        ((VueDuJeu) getScene().getRoot()).getJeu().uneRouteAEteChoisie(event.getPickResult().getIntersectedNode().getId());
    };

    EventHandler<MouseEvent> choixPort = event -> {
        System.out.println("On a cliqué sur " + event.getPickResult().getIntersectedNode().getId());
        ((VueDuJeu) getScene().getRoot()).getJeu().unPortAEteChoisi(event.getPickResult().getIntersectedNode().getId());
    };

    public void creerBindings() {
        ajouterVilles();
        ajouterPorts();
        ajouterRoutes();
        //ajouterCercleScore();
        bindRedimensionEtCentragePlateau();
        bindCaptureRoute();
        bindConstruirePorts();
        toBack();
    }

    private void ajouterPorts() {
        for (String nomPort : DonneesGraphiques.ports.keySet()) {
            DonneesGraphiques.DonneesCerclesPorts positionPortSurPlateau = DonneesGraphiques.ports.get(nomPort);
            Circle cerclePort = new Circle(positionPortSurPlateau.centreX(), positionPortSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
            cerclePort.setId(nomPort);
            getChildren().add(cerclePort);
            bindCerclePortAuPlateau(positionPortSurPlateau, cerclePort);
            cerclePort.setOnMouseClicked(choixPort);
        }
    }

    private void bindConstruirePorts() {
        List<? extends IVille> listePorts = ((VueDuJeu) getScene().getRoot()).getJeu().getPorts();
        for(IVille ville : listePorts){
            List<Circle> ports = new ArrayList<>();
            for(Node n : getChildren()){
                if(n.getId().equals(ville.getNom())){
                    ports.add((Circle) n);
                }
            }
            ville.proprietaireProperty().addListener(new ChangeListener<IJoueur>() {
                @Override
                public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                    for(Circle c : ports){
                        c.setStyle("-fx-fill :" + VueDuJeu.getCouleurValue(t1.getCouleur()) + ";" +
                                "-fx-opacity: 1;" +
                                "-fx-stroke: black;" +
                                "-fx-stroke-width: 2px;");
                    }
                }
            });
        }
    }

    private void ajouterCercleScore() {
        for(int i=0;i<2;i++){
            /*
            DonneesGraphiques.DonneesScores positionScoreSurPlateau = DonneesGraphiques.scores.get(i);
            Circle cercleScore = new Circle(positionScoreSurPlateau.centreX(),positionScoreSurPlateau.centreY(),100);
            cercleScore.setFill(Color.RED);
            cercleScore.setOpacity(0.9);
            getChildren().add(cercleScore);
            cercleScore.setOnMouseClicked(choixRoute);
            bindCercleScore(cercleScore);

             */
        }
    }

    private void ajouterRoutes() {
        List<? extends IRoute> listeRoutes = ((VueDuJeu) getScene().getRoot()).getJeu().getRoutes();
        for (String nomRoute : DonneesGraphiques.routes.keySet()) {
            ArrayList<DonneesGraphiques.DonneesSegments> segmentsRoute = DonneesGraphiques.routes.get(nomRoute);
            IRoute route = listeRoutes.stream().filter(r -> r.getNom().equals(nomRoute)).findAny().orElse(null);
            for (DonneesGraphiques.DonneesSegments unSegment : segmentsRoute) {
                Rectangle rectangleSegment = new Rectangle(unSegment.getXHautGauche(), unSegment.getYHautGauche(), DonneesGraphiques.largeurRectangle, DonneesGraphiques.hauteurRectangle);
                rectangleSegment.setId(nomRoute);
                rectangleSegment.setRotate(unSegment.getAngle());
                getChildren().add(rectangleSegment);
                rectangleSegment.setOnMouseClicked(choixRoute);
                bindRectangle(rectangleSegment, unSegment.getXHautGauche(), unSegment.getYHautGauche());
            }
        }
        //glowRoutes();
    }

    private void bindCaptureRoute(){
        List<? extends IRoute> listeRoutes = ((VueDuJeu) getScene().getRoot()).getJeu().getRoutes();
        for(IRoute route : listeRoutes){
            List<Rectangle> portionsRoute = new ArrayList<>();
            for(Node n : getChildren()){
                if(n.getId().equals(route.getNom())){
                    portionsRoute.add((Rectangle) n);
                }
            }
            route.proprietaireProperty().addListener(new ChangeListener<IJoueur>() {
                @Override
                public void changed(ObservableValue<? extends IJoueur> observableValue, IJoueur iJoueur, IJoueur t1) {
                    for(Rectangle p : portionsRoute){
                        /*p.setStyle("-fx-fill :" + VueDuJeu.getCouleurValue(t1.getCouleur()) + ";" +
                                "-fx-opacity: 1;" +
                                "-fx-stroke: black;" +
                                "-fx-stroke-width: 2px;");*/
                        ImageView wagon = new ImageView("images/wagons/image-wagon-" + t1.getCouleur() + ".png");
                        wagon.layoutXProperty().bind(p.layoutXProperty().subtract(8));
                        wagon.layoutYProperty().bind(p.layoutYProperty().subtract(11));
                        wagon.rotateProperty().bind(p.rotateProperty());
                        wagon.fitWidthProperty().bind(p.widthProperty().add(11));
                        wagon.fitHeightProperty().bind(p.heightProperty().add(23));
                        getChildren().add(wagon);
                    }
                }
            });
        }
    }

    private void bindRedimensionEtCentragePlateau() {
        mapMonde.fitWidthProperty().bind(widthProperty());
        mapMonde.fitHeightProperty().bind(heightProperty());
        mapMonde.layoutXProperty().bind(new DoubleBinding() { // Pour maintenir le plateau au centre
            {
                super.bind(widthProperty(),heightProperty());
            }
            @Override
            protected double computeValue() {
                double imageViewWidth = mapMonde.getLayoutBounds().getWidth();
                return (getWidth() - imageViewWidth) / 2;
            }
        });
    }

    private void bindCerclePortAuPlateau(DonneesGraphiques.DonneesCerclesPorts port, Circle cerclePort) {
        cerclePort.centerXProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutX() + port.centreX() * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        cerclePort.centerYProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutY() + port.centreY() * mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
        cerclePort.radiusProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return DonneesGraphiques.rayonInitial * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
    }

    private void bindRectangle(Rectangle rect, double layoutX, double layoutY) {
        rect.widthProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return DonneesGraphiques.largeurRectangle * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.heightProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return DonneesGraphiques.hauteurRectangle * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.layoutXProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutX() + layoutX * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.xProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty(), mapMonde.xProperty());}
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
        rect.layoutYProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return layoutY * mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
        rect.yProperty().bind(new DoubleBinding() {
            { super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());}
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
    }

    private void bindCercleScore(Circle cercleScore) {
        cercleScore.centerXProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutX() + 100 * mapMonde.getLayoutBounds().getWidth()/ DonneesGraphiques.largeurInitialePlateau;
            }
        });
        cercleScore.centerYProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return mapMonde.getLayoutY() + 100 * mapMonde.getLayoutBounds().getHeight()/ DonneesGraphiques.hauteurInitialePlateau;
            }
        });
        cercleScore.radiusProperty().bind(new DoubleBinding() {
            {
                super.bind(mapMonde.fitWidthProperty(), mapMonde.fitHeightProperty());
            }
            @Override
            protected double computeValue() {
                return DonneesGraphiques.rayonInitial * mapMonde.getLayoutBounds().getWidth() / DonneesGraphiques.largeurInitialePlateau;
            }
        });
    }

    public void glowRoutes() {
        List<Rectangle> routeRectangles = new ArrayList<>();
        for (String nomRoute : DonneesGraphiques.routes.keySet()) {
            ArrayList<DonneesGraphiques.DonneesSegments> segmentsRoute = DonneesGraphiques.routes.get(nomRoute);
            for (DonneesGraphiques.DonneesSegments unSegment : segmentsRoute) {
                Rectangle rectangleSegment = new Rectangle(unSegment.getXHautGauche(), unSegment.getYHautGauche(), DonneesGraphiques.largeurRectangle, DonneesGraphiques.hauteurRectangle);
                rectangleSegment.setId(nomRoute);
                getChildren().add(rectangleSegment);
                rectangleSegment.setOnMouseClicked(choixRoute);
                bindRectangle(rectangleSegment, unSegment.getXHautGauche(), unSegment.getYHautGauche());
                routeRectangles.add(rectangleSegment);
                System.out.println("Route : " + nomRoute + " " + unSegment.getXHautGauche() + " " + unSegment.getYHautGauche());
            }
        }

        // Create a timeline to animate the glow effect for each route
        Timeline timeline = new Timeline();

        for (Rectangle route : routeRectangles) {
            // Set up the initial and final keyframes for the glow effect
            KeyFrame startFrame = new KeyFrame(Duration.ZERO, new KeyValue(route.strokeProperty(), Color.TRANSPARENT));
            KeyFrame endFrame = new KeyFrame(Duration.seconds(1), new KeyValue(route.strokeProperty(), Color.GOLD));

            // Add the keyframes to the timeline
            timeline.getKeyFrames().addAll(startFrame, endFrame);

            // Add a small delay between each route
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));

            // Set up the final keyframe to reset the stroke color to transparent after the glow effect
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(route.strokeProperty(), Color.TRANSPARENT)));
        }

        // Play the timeline to start the glow effect
        timeline.play();
    }

    public void setFlash(List<String> villes, boolean state){
        for(Node n : getChildren()){
            if(villes.contains(n.getId())){
                FadeTransition ft = new FadeTransition(Duration.millis(500), n);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.setCycleCount(Animation.INDEFINITE);
                ft.setAutoReverse(true);
                FadeTransition ft2 = new FadeTransition(Duration.millis(500), mapMonde);
                ft2.setFromValue(mapMonde.getOpacity());
                ft2.setToValue(0.6);
                ParallelTransition pt = new ParallelTransition(ft, ft2);
                if (state) {
                    n.setScaleX(1.5);
                    n.setScaleY(1.5);
                    n.setStyle("-fx-fill: white; -fx-effect: dropshadow(three-pass-box, white, 5, 0, 0, 0);");
                    pt.play();
                } else {
                    pt.stop();
                    n.setScaleX(1.0);
                    n.setScaleY(1.0);
                    n.setStyle("-fx-fill: transparent;");
                    n.setEffect(null);
                    FadeTransition ft3 = new FadeTransition(Duration.millis(500), mapMonde);
                    ft3.setToValue(1.0);
                    ft3.setAutoReverse(true);
                    ft3.play();
                }


            }
        }
    }

    public void setRouteSurbrillance(List<String> villes, boolean state){

    }

    private void playGlowAnimation(DropShadow glowEffect) {
        // Create a timeline for the glow animation
        Timeline timeline = new Timeline();

        // Define the keyframes for the glow animation
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(glowEffect.radiusProperty(), 0),
                new KeyValue(glowEffect.colorProperty(), GLOW_COLOR.deriveColor(1, 1, 1, 0))
        );
        KeyFrame endFrame = new KeyFrame(GLOW_DURATION,
                new KeyValue(glowEffect.radiusProperty(), GLOW_RADIUS, Interpolator.EASE_BOTH),
                new KeyValue(glowEffect.colorProperty(), GLOW_COLOR)
        );

        // Add the keyframes to the timeline
        timeline.getKeyFrames().addAll(startFrame, endFrame);

        // Set the animation to repeat indefinitely
        timeline.setCycleCount(GLOW_CYCLE_COUNT);

        // Play the timeline
        timeline.play();
    }

    public void toBack(){
        mapMonde.toBack();
    }








private void ajouterVilles() {
    for (String nomVille : DonneesGraphiques.villes.keySet()) {
        DonneesGraphiques.DonneesCerclesPorts positionVilleSurPlateau = DonneesGraphiques.villes.get(nomVille);
        Circle cercleVille = new Circle(positionVilleSurPlateau.centreX(), positionVilleSurPlateau.centreY(), DonneesGraphiques.rayonInitial);
        cercleVille.setId(nomVille);
        getChildren().add(cercleVille);
        bindCerclePortAuPlateau(positionVilleSurPlateau, cercleVille);
        cercleVille.setOnMouseClicked(choixPort);
    }
}
}
