package fr.umontpellier.iut.rails.vues;

import fr.umontpellier.iut.rails.IRoute;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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
        System.out.println("On a cliqué sur une route");
    };

    EventHandler<MouseEvent> choixPort = event -> {
        System.out.println("On a cliqué sur un port");
    };

    public void creerBindings() {
        ajouterPorts();
        ajouterRoutes();
        bindRedimensionEtCentragePlateau();
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
                System.out.println("Route : " + nomRoute + " " + unSegment.getXHautGauche() + " " + unSegment.getYHautGauche());
            }
        }
        //glowRoutes();
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
            KeyFrame endFrame = new KeyFrame(Duration.seconds(0.5), new KeyValue(route.strokeProperty(), Color.GOLD));

            // Add the keyframes to the timeline
            timeline.getKeyFrames().addAll(startFrame, endFrame);

            // Add a small delay between each route
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5)));

            // Set up the final keyframe to reset the stroke color to transparent after the glow effect
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), new KeyValue(route.strokeProperty(), Color.TRANSPARENT)));
        }

        // Play the timeline to start the glow effect
        timeline.play();
    }


}