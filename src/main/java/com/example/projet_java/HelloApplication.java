package com.example.projet_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        // Création d'un objet contenant les items possibles
        Object myObject = new Object();

        // Création d'un Pokémon avec l'objet "Poings"
        Pokemon p = new Pokemon("Magicarpe", 250, 52, 20, 75, 66, 100, "Eau", myObject.Poings);
        Pokemon p2 = new Pokemon("Salamèche", 500, 70, 80, 175, 166, 150, "Feu", myObject.Poings);

        p.boostAtt(myObject);
        System.out.println(p.Attack);

        launch();
    }

    @Override
    public void start(Stage stage) {
        // Charger l'image de fond
        Image image = new Image("background.jpg");
        ImageView imageView = new ImageView(image);

        // Ajuster l'image pour qu'elle remplisse la fenêtre
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(false);

        // Bouton devant l'image
        Button b = new Button("Jouer !");

        // Utiliser un StackPane pour empiler l'image et le bouton
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, b);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Image en arrière-plan avec bouton");
        stage.setScene(scene);
        stage.show();
    }
}
