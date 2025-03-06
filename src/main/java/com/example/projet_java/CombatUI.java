package com.example.projet_java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CombatUI {

    private static Label roundLabel;
    private static HBox activePokemonPane;
    private static HBox movesPanel;
    private static HBox switchPanel;
    private static int roundCount = 1;

    public static Scene getScene(HelloApplication app) {
        BorderPane root = new BorderPane();

        // Top : Zone avec le numéro du round et le cadre des Pokémon actifs
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(10));
        roundLabel = new Label("Round: " + roundCount);
        roundLabel.setStyle("-fx-font-size: 20px;");
        activePokemonPane = createActivePokemonPane(app);
        topBox.getChildren().addAll(roundLabel, activePokemonPane);
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        // Centre : Panneau d'actions (zone d'attaques et changement de Pokémon)
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);
        movesPanel = createMovesPanel(app);
        switchPanel = createSwitchPanel(app);
        centerBox.getChildren().addAll(movesPanel, switchPanel);
        root.setCenter(centerBox);

        // Right : Historique du combat
        VBox historyBox = new VBox(10);
        historyBox.setPadding(new Insets(10));
        Label historyTitle = new Label("Historique");
        TextArea battleLog = new TextArea();
        battleLog.setEditable(false);
        battleLog.setPrefWidth(250);
        battleLog.setPrefHeight(400);
        historyBox.getChildren().addAll(historyTitle, battleLog);
        root.setRight(historyBox);

        // Bottom : Bouton pour retourner au menu
        Button returnButton = new Button("Retour au menu");
        returnButton.setOnAction(e -> app.showMenuScene());
        HBox bottomBox = new HBox(returnButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        // Actions pour les capacités : chaque bouton ajoute son usage dans l'historique, même si c'est un move "placeholder"
        for (javafx.scene.Node node : movesPanel.getChildren()) {
            if (node instanceof Button) {
                Button moveBtn = (Button) node;
                moveBtn.setOnAction(e -> {
                    battleLog.appendText("Round " + roundCount + ": " + moveBtn.getText() + " utilisé.\n");
                    roundCount++;
                    roundLabel.setText("Round: " + roundCount);
                });
            }
        }

        return new Scene(root, 800, 600);
    }

    // Mise à jour de la scène de combat avant son affichage
    public static void setupBattleScene(HelloApplication app) {
        BorderPane root = (BorderPane) app.combatScene.getRoot();
        VBox topBox = (VBox) root.getTop();
        HBox updatedActivePane = createActivePokemonPane(app);
        if (topBox.getChildren().size() >= 2) {
            topBox.getChildren().set(1, updatedActivePane);
        } else {
            topBox.getChildren().add(updatedActivePane);
        }
        VBox centerBox = (VBox) root.getCenter();
        centerBox.getChildren().clear();
        centerBox.getChildren().addAll(createMovesPanel(app), createSwitchPanel(app));
    }

    // Crée le cadre affichant les deux Pokémon actifs avec des labels agrandis
    private static HBox createActivePokemonPane(HelloApplication app) {
        HBox pane = new HBox(40);
        pane.setPadding(new Insets(10));
        pane.setAlignment(Pos.CENTER);
        Pokemon activeP1 = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        Pokemon activeP2 = app.teamPlayer2.isEmpty() ? null : app.teamPlayer2.get(0);

        VBox p1Box = new VBox(5);
        p1Box.setAlignment(Pos.CENTER);
        p1Box.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        if (activeP1 != null) {
            Label name1 = new Label(activeP1.getName());
            name1.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            Label hp1 = new Label("HP: " + activeP1.getHP());
            hp1.setStyle("-fx-font-size: 20px;");
            p1Box.getChildren().addAll(name1, hp1);
        } else {
            p1Box.getChildren().add(new Label("Aucun"));
        }

        VBox p2Box = new VBox(5);
        p2Box.setAlignment(Pos.CENTER);
        p2Box.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        if (activeP2 != null) {
            Label name2 = new Label(activeP2.getName());
            name2.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            Label hp2 = new Label("HP: " + activeP2.getHP());
            hp2.setStyle("-fx-font-size: 20px;");
            p2Box.getChildren().addAll(name2, hp2);
        } else {
            p2Box.getChildren().add(new Label("Aucun"));
        }
        pane.getChildren().addAll(p1Box, p2Box);
        return pane;
    }

    // Crée le panneau des attaques avec 4 boutons (les capacités du Pokémon actif)
    private static HBox createMovesPanel(HelloApplication app) {
        HBox movesBox = new HBox(10);
        movesBox.setAlignment(Pos.CENTER);
        Pokemon active = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        String[] moves;
        if (active != null && active.getMoves() != null) {
            moves = active.getMoves();
        } else {
            moves = new String[]{"Move1", "Move2", "Move3", "Move4"};
        }
        for (String move : moves) {
            Button moveBtn = new Button(move);
            moveBtn.setPrefWidth(100);
            movesBox.getChildren().add(moveBtn);
        }
        return movesBox;
    }

    // Crée le panneau de changement de Pokémon avec 6 boutons
    private static HBox createSwitchPanel(HelloApplication app) {
        HBox switchBox = new HBox(10);
        switchBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 6; i++) {
            Button btn = new Button();
            btn.setPrefWidth(100);
            if (i < app.teamPlayer1.size()) {
                Pokemon p = app.teamPlayer1.get(i);
                btn.setText(p.getName() + "\nHP:" + p.getHP());
                if (i == 0) {
                    btn.setDisable(true);
                    btn.setStyle("-fx-background-color: lightgray;");
                }
                int index = i;
                btn.setOnAction(e -> {
                    if (index != 0) {
                        Pokemon temp = app.teamPlayer1.get(0);
                        app.teamPlayer1.set(0, app.teamPlayer1.get(index));
                        app.teamPlayer1.set(index, temp);
                        activePokemonPane.getChildren().clear();
                        activePokemonPane.getChildren().addAll(createActivePokemonPane(app).getChildren());
                        updateSwitchPanel(app, switchBox);
                    }
                });
            } else {
                btn.setText("Vide");
                btn.setDisable(true);
            }
            switchBox.getChildren().add(btn);
        }
        return switchBox;
    }

    private static void updateSwitchPanel(HelloApplication app, HBox switchBox) {
        switchBox.getChildren().clear();
        for (int i = 0; i < 6; i++) {
            Button btn = new Button();
            btn.setPrefWidth(100);
            if (i < app.teamPlayer1.size()) {
                Pokemon p = app.teamPlayer1.get(i);
                btn.setText(p.getName() + "\nHP:" + p.getHP());
                if (i == 0) {
                    btn.setDisable(true);
                    btn.setStyle("-fx-background-color: lightgray;");
                }
                int index = i;
                btn.setOnAction(e -> {
                    if (index != 0) {
                        Pokemon temp = app.teamPlayer1.get(0);
                        app.teamPlayer1.set(0, app.teamPlayer1.get(index));
                        app.teamPlayer1.set(index, temp);
                        activePokemonPane.getChildren().clear();
                        activePokemonPane.getChildren().addAll(createActivePokemonPane(app).getChildren());
                        updateSwitchPanel(app, switchBox);
                    }
                });
            } else {
                btn.setText("Vide");
                btn.setDisable(true);
            }
            switchBox.getChildren().add(btn);
        }
    }
}
