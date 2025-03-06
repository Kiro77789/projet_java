package com.example.projet_java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MovesModificationUI {

    // Liste prédéfinie de capacités
    private static final ObservableList<String> availableMoves = FXCollections.observableArrayList(
            "Thunderbolt", "Quick Attack", "Iron Tail", "Electro Ball", "Tackle", "Flamethrower", "Water Gun", "Vine Whip"
    );

    public static void show(HelloApplication app, Pokemon p) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modifier Capacités - " + p.getName());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Affichage des stats complets du Pokémon avec son type
        Label nameLabel = new Label("Nom: " + p.getName() + " (Type: " + p.type.getName() + ")");
        Label hpLabel = new Label("HP: " + p.getHP());
        Label attackLabel = new Label("Att: " + p.getAttack());
        Label defenseLabel = new Label("Def: " + p.getDefense());
        Label attackSpeLabel = new Label("Att Spe: " + p.getAttackSpe());
        Label defenseSpeLabel = new Label("Def Spe: " + p.getDefenseSpe());
        Label vitesseLabel = new Label("Vitesse: " + p.getVitesse());
        grid.add(nameLabel, 0, 0, 2, 1);
        grid.add(hpLabel, 0, 1);
        grid.add(attackLabel, 1, 1);
        grid.add(defenseLabel, 0, 2);
        grid.add(attackSpeLabel, 1, 2);
        grid.add(defenseSpeLabel, 0, 3);
        grid.add(vitesseLabel, 1, 3);

        // 4 ComboBox pour choisir les capacités
        ComboBox<String> moveBox1 = new ComboBox<>(availableMoves);
        ComboBox<String> moveBox2 = new ComboBox<>(availableMoves);
        ComboBox<String> moveBox3 = new ComboBox<>(availableMoves);
        ComboBox<String> moveBox4 = new ComboBox<>(availableMoves);

        // Afficher les capacités actuelles si elles existent
        String[] currentMoves = p.getMoves();
        if (currentMoves != null) {
            moveBox1.setValue(currentMoves[0]);
            moveBox2.setValue(currentMoves[1]);
            moveBox3.setValue(currentMoves[2]);
            moveBox4.setValue(currentMoves[3]);
        }

        grid.add(new Label("Capacité 1:"), 0, 4);
        grid.add(moveBox1, 1, 4);
        grid.add(new Label("Capacité 2:"), 0, 5);
        grid.add(moveBox2, 1, 5);
        grid.add(new Label("Capacité 3:"), 0, 6);
        grid.add(moveBox3, 1, 6);
        grid.add(new Label("Capacité 4:"), 0, 7);
        grid.add(moveBox4, 1, 7);

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(e -> {
            String[] newMoves = new String[]{
                    moveBox1.getValue(), moveBox2.getValue(), moveBox3.getValue(), moveBox4.getValue()
            };
            p.setMoves(newMoves);
            stage.close();
        });
        grid.add(saveButton, 1, 8);

        Scene scene = new Scene(grid, 500, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
