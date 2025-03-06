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

    public static Scene getScene(HelloApplication app) {
        BorderPane root = new BorderPane();

        // Haut : Label indiquant le joueur courant
        teamSelectionLabel = new Label();
        teamSelectionLabel.setStyle("-fx-font-size: 24px;");
        updateTeamSelectionLabel(app);
        root.setTop(teamSelectionLabel);
        BorderPane.setAlignment(teamSelectionLabel, Pos.CENTER);

        // Gauche : Liste scrollable des Pokémon disponibles
        VBox availableList = new VBox(10);
        availableList.setPadding(new Insets(10));
        for (Pokemon p : app.availablePokemons) {
            Button btn = new Button(p.getName() + " | HP:" + p.getHP() + " Att:" + p.getAttack());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> addPokemonToTeam(app, p));
            availableList.getChildren().add(btn);
        }
        ScrollPane scrollPane = new ScrollPane(availableList);
        scrollPane.setFitToWidth(true);
        root.setLeft(scrollPane);

        // Centre : Grille de 6 cases pour afficher l’équipe sélectionnée
        teamGrid = new GridPane();
        teamGrid.setPadding(new Insets(10));
        teamGrid.setHgap(10);
        teamGrid.setVgap(10);
        for (int i = 0; i < 6; i++) {
            VBox slot = createEmptySlot();
            teamGrid.add(slot, i % 3, i / 3);
        }
        root.setCenter(teamGrid);

        // Bas : Bouton de validation de l’équipe
        Button validateButton = new Button("Valider l'équipe");
        validateButton.setOnAction(e -> {
            if ((app.currentPlayer == 1 && app.teamPlayer1.isEmpty()) ||
                    (app.currentPlayer == 2 && app.teamPlayer2.isEmpty())) {
                System.out.println("Sélectionnez au moins un Pokémon.");
            } else {
                if (app.currentPlayer == 1) {
                    app.currentPlayer = 2;
                    updateTeamSelectionLabel(app);
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

    public static void updateTeamSelectionLabel(HelloApplication app) {
        if (teamSelectionLabel != null) {
            teamSelectionLabel.setText("Joueur " + app.currentPlayer + " - Choisissez vos Pokémons:");
        }
    }

    public static void addPokemonToTeam(HelloApplication app, Pokemon p) {
        for (javafx.scene.Node node : teamGrid.getChildren()) {
            VBox slot = (VBox) node;
            if (slot.getUserData() == null) { // case vide
                slot.getChildren().clear();
                Label info = new Label(p.getName() + "\nHP:" + p.getHP() + "\nAtt:" + p.getAttack());
                Button deleteButton = new Button("Supprimer");
                deleteButton.setOnAction(e -> {
                    slot.getChildren().clear();
                    slot.getChildren().add(new Label(""));
                    slot.setUserData(null);
                    if (app.currentPlayer == 1) {
                        app.teamPlayer1.remove(p);
                    } else {
                        app.teamPlayer2.remove(p);
                    }
                });
                // Nouveau bouton Modifier pour modifier les capacités
                Button modifyButton = new Button("Modifier");
                modifyButton.setOnAction(e -> MovesModificationUI.show(app, p));
                slot.getChildren().addAll(info, deleteButton, modifyButton);
                slot.setUserData(p);
                if (app.currentPlayer == 1) {
                    app.teamPlayer1.add(p);
                } else {
                    app.teamPlayer2.add(p);
                }
                break;
            }
        }
    }

    private static VBox createEmptySlot() {
        VBox slot = new VBox(5);
        slot.setAlignment(Pos.CENTER);
        slot.setPrefSize(150, 100);
        slot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        slot.getChildren().add(new Label(""));
        return slot;
    }

    public static void clearTeamGrid() {
        for (javafx.scene.Node node : teamGrid.getChildren()) {
            VBox slot = (VBox) node;
            slot.getChildren().clear();
            slot.getChildren().add(new Label(""));
            slot.setUserData(null);
        }
    }
}
