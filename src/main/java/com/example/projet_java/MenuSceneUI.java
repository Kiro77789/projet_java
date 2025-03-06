package com.example.projet_java;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MenuSceneUI {

    public static Scene getScene(HelloApplication app) {
        BorderPane root = new BorderPane();
        Label title = new Label("Pokémon Shutdown");
        title.setStyle("-fx-font-size: 36px;");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        Button playButton = new Button("Jouer");
        Button creditsButton = new Button("Crédits");
        VBox buttons = new VBox(20, playButton, creditsButton);
        buttons.setAlignment(Pos.CENTER);
        root.setCenter(buttons);

        playButton.setOnAction(e -> app.showTeamSelectionScene());
        creditsButton.setOnAction(e -> app.showCreditsScene());

        return new Scene(root, 800, 600);
    }
}
