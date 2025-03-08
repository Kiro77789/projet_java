package com.example.projet_java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class TeamSelectionUI {

    private static VBox teamBox;            // Au lieu d’une grille, on utilise un VBox
    private static Label teamSelectionLabel;

    public static Scene getScene(PokemonBattleSimulator app) {
        BorderPane root = new BorderPane();

        teamSelectionLabel = new Label("Joueur " + app.currentPlayer + " - Sélectionnez vos Pokémon");
        teamSelectionLabel.setStyle("-fx-font-size: 24px;");
        root.setTop(teamSelectionLabel);
        BorderPane.setAlignment(teamSelectionLabel, Pos.CENTER);

        // Liste des Pokémon disponibles (toujours à gauche, sous forme de boutons)
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

        // VBox pour afficher l’équipe sélectionnée (chaque Pokémon en HBox)
        teamBox = new VBox(10);
        teamBox.setPadding(new Insets(10));
        ScrollPane teamScroll = new ScrollPane(teamBox);
        teamScroll.setFitToWidth(true);
        root.setCenter(teamScroll);

        // Bouton de validation
        Button validateButton = new Button("Valider l'équipe");
        validateButton.setOnAction(e -> {
            // Vérifier qu’il y a au moins un Pokémon pour le joueur courant
            if ((app.currentPlayer == 1 && app.teamPlayer1.isEmpty()) ||
                    (app.currentPlayer == 2 && app.teamPlayer2.isEmpty())) {
                System.out.println("Sélectionnez au moins un Pokémon.");
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

        return new Scene(root, 900, 600);
    }

    // Crée un HBox représentant un Pokémon dans l’équipe
    private static HBox createPokemonRow(Pokemon p, PokemonBattleSimulator app) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10));
        row.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        row.setAlignment(Pos.CENTER_LEFT);

        // Label pour le nom et HP
        Label info = new Label(p.getName() + "\nHP: " + p.getCurrentHP());

        // Bouton pour supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> {
            row.getChildren().clear();
            row.setUserData(null);
            if (app.currentPlayer == 1) {
                app.teamPlayer1.remove(p);
            } else {
                app.teamPlayer2.remove(p);
            }
            // Retirer le HBox du parent
            VBox parent = (VBox) row.getParent();
            parent.getChildren().remove(row);
        });

        // Bouton pour modifier les moves
        Button modifyMovesButton = new Button("Modifier Moves");
        modifyMovesButton.setOnAction(e -> {
            // Ouvre la fenêtre de modification
            MoveModificationUI.showMoveModificationDialog(p);
            // On ne met plus à jour le label pour éviter d'afficher les moves
        });

        // On peut afficher des stats supplémentaires si on le souhaite
        Label statsLabel = new Label("Att: " + p.getAttack() +
                " | Def: " + p.getDefense() +
                " | Speed: " + p.getSpeed());

        row.getChildren().addAll(info, statsLabel, modifyMovesButton, deleteButton);
        row.setUserData(p);
        return row;
    }

    // Ajoute un Pokémon dans le VBox de l’équipe
    private static void addPokemonToTeam(PokemonBattleSimulator app, Pokemon basePokemon) {
        Pokemon copy = Pokemon.copyOf(basePokemon);  // Éviter de partager la même instance
        HBox row = createPokemonRow(copy, app);

        // Ajouter au VBox
        teamBox.getChildren().add(row);

        // Ajouter à l’équipe correspondante
        if (app.currentPlayer == 1) {
            app.teamPlayer1.add(copy);
        } else {
            app.teamPlayer2.add(copy);
        }
    }

    private static void clearTeamBox() {
        teamBox.getChildren().clear();
    }
}
