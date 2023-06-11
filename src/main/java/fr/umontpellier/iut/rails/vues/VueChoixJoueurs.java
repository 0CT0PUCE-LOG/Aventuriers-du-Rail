package fr.umontpellier.iut.rails.vues;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private ComboBox<Integer> comboBoxNombreJoueurs;
    private VBox vboxJoueurs;
    private AnimatedButton boutonDemarrer;

    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        createUI();
    }

    private void createUI() {
        Pane pane = new Pane();
        pane.setPrefWidth(800);
        pane.setPrefHeight(600);
        //set background image to homeScreen.jpg
        pane.setStyle("-fx-background-image: url('/images/homeScreen.jpg'); -fx-background-size: cover;");

        Pane menu = new Pane();
        menu.relocate(450, 100);
        menu.setPrefSize(300, 400);
        //set darkred border with 5px corner radius
        menu.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        menu.setStyle("-fx-stroke: darkred; -fx-stroke-width: 6; -fx-background-radius: 7; -fx-background-color: #F5F5DC;");
        menu.setStyle("-fx-background-color: #F5F5DC;");

        comboBoxNombreJoueurs = new ComboBox<>();
        comboBoxNombreJoueurs.getItems().addAll(2, 3, 4, 5);
        comboBoxNombreJoueurs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateNombreJoueurs(newValue);
        });
        comboBoxNombreJoueurs.setStyle("-fx-font-size: 14px; -fx-pref-width: 200px;");
        comboBoxNombreJoueurs.setPromptText("Nombre de joueurs");
        comboBoxNombreJoueurs.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        vboxJoueurs = new VBox();
        vboxJoueurs.setSpacing(10);

        boutonDemarrer = new AnimatedButton();
        boutonDemarrer.setText("JOUER");
        boutonDemarrer.setCursor(Cursor.HAND);
        boutonDemarrer.setOnAction(event -> {
            demarrerPartie();
        });
        boutonDemarrer.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px; -fx-background-color: #606060; -fx-text-fill: white;");

        Label indication = new Label("Veuillez indiquer le \nnombre de joueurs :");
        indication.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        VBox menuBox = new VBox(indication, comboBoxNombreJoueurs, vboxJoueurs, boutonDemarrer);
        menuBox.setSpacing(20);
        menuBox.setPadding(new Insets(20));
        menuBox.setPrefSize(300, 400);
        menuBox.setAlignment(Pos.CENTER);

        menu.getChildren().add(menuBox);
        pane.getChildren().add(menu);

        Scene scene = new Scene(pane);

        setScene(scene);
        setTitle("Aventurier du rails - Choix des joueurs");
        setResizable(false);
    }

    private void updateNombreJoueurs(int nombreJoueurs) {
        vboxJoueurs.getChildren().clear();
        for (int i = 1; i <= nombreJoueurs; i++) {
            TextField textFieldNom = new TextField();
            textFieldNom.setPromptText("Nom du joueur " + i);
            textFieldNom.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            vboxJoueurs.getChildren().add(textFieldNom);
        }
    }

    private void demarrerPartie() {
        List<String> nomsJoueurs = new ArrayList<>();
        Integer nombreJoueur = comboBoxNombreJoueurs.getValue();
        if (nombreJoueur == null) {
            showError("Veuillez choisir un nombre de joueur possible");
            return;
        }
        for (int i = 0; i < vboxJoueurs.getChildren().size(); i++) {
            TextField textFieldNom = (TextField) vboxJoueurs.getChildren().get(i);
            String nomJoueur = textFieldNom.getText();
            if (nomJoueur.isEmpty()) {
                showError("Veuillez renseigner tous les noms des joueurs.");
                return;
            }
            nomsJoueurs.add(nomJoueur);
        }

        this.nomsJoueurs.clear();
        this.nomsJoueurs.addAll(nomsJoueurs);
        hide();
    }

    public List<String> getNomsJoueurs() {
        return new ArrayList<>(nomsJoueurs);
    }

    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {
        comboBoxNombreJoueurs.getSelectionModel().selectedItemProperty().addListener(quandLeNombreDeJoueursChange);
    }

    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs(); i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            } else {
                tempNamesList.add(name);
            }
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    protected int getNombreDeJoueurs() {
        return getNomsJoueurs().size();
    }

    protected String getJoueurParNumero(int playerNumber) {
        return getNomsJoueurs().get(playerNumber - 1);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
