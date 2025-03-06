package com.example.projet_java;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CreditsSceneUI {
    public static Scene getScene(HelloApplication app) {
        BorderPane root = new BorderPane();
        Label credits = new Label("Crédits :\nProjet réalisé par [Votre Nom]\nEtudiant en 1ère année");
        credits.setStyle("-fx-font-size: 20px;");
        root.setCenter(credits);
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> app.showMenuScene());
        root.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}
