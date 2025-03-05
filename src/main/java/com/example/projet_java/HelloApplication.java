package com.example.projet_java;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    // Déclarer p1 comme variable d'instance accessible dans toute la classe
    private Pokemon p1;
    private Pokemon p2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // Initialiser p1 ici
        Object myObject = new Object();
        p1 = new Pokemon("Magicacarpe", 250, 52, 20, 75, 66, 100, "Vol", myObject.Poings);
        p2 = new Pokemon("Dracaufeu", 250, 52, 20, 75, 66, 100, "Vol", myObject.Poings);
    }

    @Override
    public void start(Stage stage) {
        // --- Scène 1 ---
        Image image = new Image("background.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(false);

        Button jouerButton = new Button("Jouer !");
        VBox vboxScene1 = new VBox(jouerButton);
        vboxScene1.setAlignment(Pos.CENTER);
        vboxScene1.setSpacing(20);

        StackPane rootScene1 = new StackPane(imageView, vboxScene1);
        Scene scene1 = new Scene(rootScene1, 800, 600);

        // --- Scène 2 avec BorderPane ---
        BorderPane root2 = new BorderPane();
        Scene scene2 = new Scene(root2, 800, 600);

        // Création des éléments de la scène 2 en utilisant les getters du Pokémon
        Label labelHP = new Label("HP : " + p1.getHP());
        Label labelAttack = new Label("Att : " + p1.getAttack());
        Label labelDefense = new Label("Def : " + p1.getDefense());
        Label labelAttackSpe = new Label("Att Spe : " + p1.getAttackSpe());
        Label labelDefenseSpe = new Label("Def Spe : " + p1.getDefenseSpe());
        Label labelVitesse = new Label("Vit : " + p1.getVitesse());
        Label labelType = new Label("Type : " + p1.type.getName());
        Button retourButton = new Button("Retour à la Scene 1");
        // Le bouton affiche automatiquement le nom du Pokémon via le getter
        Button pokemonButton = new Button(p1.getName());
        Button pokemonButton2 = new Button(p2.getName());


        //Section gauche selection pokemon

            // Hbox pour les labels
            HBox labelsHBox = new HBox(labelHP, labelAttack, labelDefense, labelAttackSpe, labelDefenseSpe, labelVitesse, labelType);
            labelsHBox.setSpacing(10);
            root2.setCenter(labelsHBox);

            // Bouton "pokemon"
            VBox leftBox = new VBox(pokemonButton, pokemonButton2);
            leftBox.setSpacing(10);

            // Élements de gestion des boutons d'affichage de stats
            HBox p1TotBox = new HBox(leftBox , labelsHBox);
            p1TotBox.setSpacing(20);
            p1TotBox.setPadding(new Insets(50,0,0,0));
            p1TotBox.setAlignment(Pos.CENTER);
            root2.setLeft(p1TotBox);

            // Bouton "Retour" en bas à gauche
            HBox bottomBox = new HBox(retourButton);
            bottomBox.setAlignment(Pos.CENTER_LEFT);
            root2.setBottom(bottomBox);

        //Affichage des éléments non interactif
        Label infoActions = new Label("Séléctionner des pokémons pour votre équipe :");
        infoActions.setFont(Font.font("Arial" , 28));

        //Placement des éléments non-interactifs
        root2.setTop(infoActions);
        infoActions.setAlignment(Pos.CENTER);

        // --- Actions des boutons ---
        jouerButton.setOnAction(e -> stage.setScene(scene2));
        retourButton.setOnAction(e -> stage.setScene(scene1));
        pokemonButton.setOnAction(e -> labelsHBox.setVisible(!labelsHBox.isVisible()));


        // Affichage initial de la scène 1
        stage.setTitle("Pokémon tah les fous");
        stage.setScene(scene1);
        stage.show();

        /*
            Nique agathe , je t'aime pas , t'es chiante , tu pètes les couilles
         */
    }
}
