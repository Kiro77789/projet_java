package com.example.projet_java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TeamSelectionUI {

    private static VBox teamBox;  // Contiendra un HBox par Pokémon de l'équipe
    private static Label teamSelectionLabel;

    public static Scene getScene(PokemonBattleSimulator app) {
        BorderPane root = new BorderPane();

        teamSelectionLabel = new Label("Joueur " + app.currentPlayer + " - Sélectionnez vos Pokémon");
        teamSelectionLabel.setStyle("-fx-font-size: 24px;");
        root.setTop(teamSelectionLabel);
        BorderPane.setAlignment(teamSelectionLabel, Pos.CENTER);

        // Liste des Pokémon disponibles (affichée à gauche)
        VBox availableList = new VBox(10);
        availableList.setPadding(new Insets(10));
        for (Pokemon p : app.availablePokemons) {
            Button btn = new Button(p.getName() + " (HP: " + p.getCurrentHP() + ")");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> addPokemonToTeam(app, p));
            availableList.getChildren().add(btn);
        }
        ScrollPane availableScroll = new ScrollPane(availableList);
        availableScroll.setFitToWidth(true);
        root.setLeft(availableScroll);

        // Zone centrale : un VBox contenant un HBox pour chaque Pokémon sélectionné
        teamBox = new VBox(10);
        teamBox.setPadding(new Insets(10));
        // Initialiser avec 6 lignes vides
        for (int i = 0; i < 6; i++) {
            HBox row = createEmptyRow();
            teamBox.getChildren().add(row);
        }
        ScrollPane teamScroll = new ScrollPane(teamBox);
        teamScroll.setFitToWidth(true);
        root.setCenter(teamScroll);

        // Bas : bouton de validation de l'équipe
        Button validateButton = new Button("Valider l'équipe");
        validateButton.setOnAction(e -> {
            if ((app.currentPlayer == 1 && app.teamPlayer1.isEmpty()) ||
                    (app.currentPlayer == 2 && app.teamPlayer2.isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sélectionnez au moins un Pokémon.");
                alert.showAndWait();
            } else {
                if (app.currentPlayer == 1) {
                    app.currentPlayer = 2;
                    teamSelectionLabel.setText("Joueur " + app.currentPlayer + " - Sélectionnez vos Pokémon");
                    clearTeamBox();
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

    // Crée un HBox vide pour représenter une ligne de l'équipe
    private static HBox createEmptyRow() {
        HBox row = new HBox();
        row.setPadding(new Insets(5));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        Label placeholder = new Label("Vide");
        row.getChildren().add(placeholder);
        return row;
    }

    // Crée un HBox pour un Pokémon sélectionné, affichant nom, HP, stats, talent et objet, avec boutons espacés.
    private static HBox createPokemonRow(Pokemon p, PokemonBattleSimulator app) {
        HBox row = new HBox(20);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);

        // Label info avec un léger décalage (padding gauche)
        Label info = new Label(p.getName() + "\nHP: " + p.getCurrentHP());
        info.setPadding(new Insets(0, 0, 0, 10));

        // Récupération des informations sur le talent et l'objet
        String talentStr = "Aucun";
        if (p.getTalent() != null) {
            talentStr = p.getTalent().getClass().getSimpleName().replace("Talent", "");
        }

        Label stats = new Label("Att: " + p.getAttack() + " | Def: " + p.getDefense() +
                " | Spd: " + p.getSpeed() +
                " | Talent: " + talentStr + " ");

        // Boutons d'action regroupés dans un HBox pour espacer uniformément
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button modifyButton = new Button("Modifier Moves");
        modifyButton.setOnAction(e -> {
            MoveModificationUI.showMoveModificationDialog(p);
        });

        Button objetsButton = new Button("Objets");
        objetsButton.setOnAction(e -> {
            // Cette action peut par exemple afficher les informations sur l'objet équipé
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Objet équipé: " +
                    ((p.getHeldItem() != null) ? p.getHeldItem().toString() : "Aucun"));
            alert.showAndWait();
        });

        // Nouveau bouton "Équiper" pour choisir un objet
        Button equipButton = new Button("Équiper");
        equipButton.setOnAction(e -> {
            Item selectedItem = ItemSelectionUI.showItemSelectionDialog();
            if (selectedItem != null) {
                p.setHeldItem(selectedItem);
            }
        });

        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> {
            row.getChildren().clear();
            Label emptyLabel = new Label("Vide");
            row.getChildren().add(emptyLabel);
            row.setUserData(null);
            if (app.currentPlayer == 1) {
                app.teamPlayer1.remove(p);
            } else {
                app.teamPlayer2.remove(p);
            }
        });

        buttonBox.getChildren().addAll(modifyButton, objetsButton, equipButton, deleteButton);

        row.getChildren().addAll(info, stats, buttonBox);
        row.setUserData(p);
        return row;
    }

    public static void addPokemonToTeam(PokemonBattleSimulator app, Pokemon p) {
        // Limiter l'équipe à 6 Pokémon
        if ((app.currentPlayer == 1 && app.teamPlayer1.size() >= 6) ||
                (app.currentPlayer == 2 && app.teamPlayer2.size() >= 6)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "L'équipe est limitée à 6 Pokémon.");
            alert.showAndWait();
            return;
        }
        // Créer une copie pour éviter le partage d'instance
        Pokemon copy = Pokemon.copyOf(p);
        // Trouver la première ligne vide dans teamBox
        for (int i = 0; i < teamBox.getChildren().size(); i++) {
            HBox row = (HBox) teamBox.getChildren().get(i);
            if (row.getUserData() == null) {
                row.getChildren().clear();
                HBox newRow = createPokemonRow(copy, app);
                row.getChildren().addAll(newRow.getChildren());
                row.setUserData(copy);
                if (app.currentPlayer == 1) {
                    app.teamPlayer1.add(copy);
                } else {
                    app.teamPlayer2.add(copy);
                }
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "L'équipe est limitée à 6 Pokémon.");
        alert.showAndWait();
    }

    private static void clearTeamBox() {
        teamBox.getChildren().clear();
        for (int i = 0; i < 6; i++) {
            HBox row = createEmptyRow();
            teamBox.getChildren().add(row);
        }
    }
}
