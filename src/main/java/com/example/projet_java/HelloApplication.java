package com.example.projet_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Charger l'image de fond
        Image image = new Image("background.jpg");
        ImageView imageView = new ImageView(image);

        // Ajuster l'image pour qu'elle remplisse la fenêtre
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(false); // Déforme si nécessaire pour remplir la scène

        // Bouton devant l'image
        Button b = new Button("Jouer !");

        // Utiliser un StackPane pour empiler l'image et le bouton
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, b); // L'image est derrière, le bouton devant

        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Image en arrière-plan avec bouton");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
