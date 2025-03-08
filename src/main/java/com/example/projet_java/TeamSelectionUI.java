package com.example.projet_java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TeamSelectionUI {

    private static GridPane teamGrid;
    private static Label teamSelectionLabel;

    public static Scene getScene(PokemonBattleSimulator app) {
        BorderPane root = new BorderPane();

        teamSelectionLabel = new Label("Joueur " + app.currentPlayer + " - Sélectionnez vos Pokémon");
        teamSelectionLabel.setStyle("-fx-font-size: 24px;");
        root.setTop(teamSelectionLabel);
        BorderPane.setAlignment(teamSelectionLabel, Pos.CENTER);

        // Liste des Pokémon disponibles
        VBox availableList = new VBox(10);
        availableList.setPadding(new Insets(10));
        for (Pokemon p : app.availablePokemons) {
            Button btn = new Button(p.getName() + " (HP: " + p.getCurrentHP() + ")");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> addPokemonToTeam(app, p));
            availableList.getChildren().add(btn);
        }
        ScrollPane scrollPane = new ScrollPane(availableList);
        scrollPane.setFitToWidth(true);
        root.setLeft(scrollPane);

        // Grille pour afficher l’équipe sélectionnée (6 cases)
        teamGrid = new GridPane();
        teamGrid.setPadding(new Insets(10));
        teamGrid.setHgap(10);
        teamGrid.setVgap(10);
        for (int i = 0; i < 6; i++) {
            VBox slot = createEmptySlot();
            teamGrid.add(slot, i % 3, i / 3);
        }
        root.setCenter(teamGrid);

        Button validateButton = new Button("Valider l'équipe");
        validateButton.setOnAction(e -> {
            if ((app.currentPlayer == 1 && app.teamPlayer1.isEmpty()) ||
                    (app.currentPlayer == 2 && app.teamPlayer2.isEmpty())) {
                System.out.println("Sélectionnez au moins un Pokémon.");
            } else {
                if (app.currentPlayer == 1) {
                    app.currentPlayer = 2;
                    teamSelectionLabel.setText("Joueur " + app.currentPlayer + " - Sélectionnez vos Pokémon");
                    clearTeamGrid();
                } else {
                    app.showCombatScene();
                }
            }
        });
        VBox bottomBox = new VBox(validateButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        return new Scene(root, 800, 600);
    }

    private static VBox createEmptySlot() {
        VBox slot = new VBox(5);
        slot.setAlignment(Pos.CENTER);
        slot.setPrefSize(150, 100);
        slot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        slot.getChildren().add(new Label("Vide"));
        return slot;
    }

    public static void addPokemonToTeam(PokemonBattleSimulator app, Pokemon p) {
        // Crée une copie pour éviter que 2 pokémons identiques partagent la même instance
        Pokemon copy = Pokemon.copyOf(p);

        for (javafx.scene.Node node : teamGrid.getChildren()) {
            VBox slot = (VBox) node;
            if (slot.getUserData() == null) {
                slot.getChildren().clear();
                Label info = new Label(copy.getName() + "\nHP: " + copy.getCurrentHP());

                // Bouton pour supprimer le Pokémon de l'équipe
                Button deleteButton = new Button("Supprimer");
                deleteButton.setOnAction(e -> {
                    slot.getChildren().clear();
                    slot.getChildren().add(new Label("Vide"));
                    slot.setUserData(null);
                    if (app.currentPlayer == 1) {
                        app.teamPlayer1.remove(copy);
                    } else {
                        app.teamPlayer2.remove(copy);
                    }
                });

                // Bouton pour modifier (attribuer) les attaques du Pokémon
                Button modifyMovesButton = new Button("Modifier Moves");
                modifyMovesButton.setOnAction(e -> {
                    MoveModificationUI.showMoveModificationDialog(copy);
                    info.setText(copy.getName() + "\nHP: " + copy.getCurrentHP() + "\nMoves: " + copy.getMoves());
                });

                slot.getChildren().addAll(info, modifyMovesButton, deleteButton);
                slot.setUserData(copy);

                // Ajout dans la bonne équipe
                if (app.currentPlayer == 1) {
                    app.teamPlayer1.add(copy);
                } else {
                    app.teamPlayer2.add(copy);
                }
                break;
            }
        }
    }


    private static void clearTeamGrid() {
        for (javafx.scene.Node node : teamGrid.getChildren()) {
            VBox slot = (VBox) node;
            slot.getChildren().clear();
            slot.getChildren().add(new Label("Vide"));
            slot.setUserData(null);
        }
    }


}
