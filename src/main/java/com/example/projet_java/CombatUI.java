package com.example.projet_java;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Optional;
import java.util.Random;

public class CombatUI {

    // On stocke deux rectangles pour afficher le statut
    private static Rectangle p1StatusRect = new Rectangle(10, 10, Color.TRANSPARENT);
    private static Rectangle p2StatusRect = new Rectangle(10, 10, Color.TRANSPARENT);

    public static Scene getScene(PokemonBattleSimulator app) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Panneau de gauche : cadre composite avec infos joueur et ordinateur encadrant un "VS"
        HBox leftPanel = new HBox(10);
        leftPanel.setAlignment(Pos.CENTER);

        // Panneau Joueur
        VBox p1Info = new VBox(5);
        p1Info.setAlignment(Pos.CENTER);
        Label p1NameLabel = new Label();
        p1NameLabel.setFont(Font.font(14));

        // On place le petit carré + nom dans un HBox
        HBox p1NameBox = new HBox(5, p1StatusRect, p1NameLabel);

        ProgressBar p1HPBar = new ProgressBar(1.0);
        p1HPBar.setPrefWidth(100);
        Label p1HPLabel = new Label();
        p1Info.getChildren().addAll(p1NameBox, p1HPBar, p1HPLabel);

        // Cadre central "VS"
        StackPane lifePane = new StackPane();
        lifePane.setPrefSize(100, 100);
        lifePane.setStyle("-fx-border-color: gray; -fx-border-width: 2;");
        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font(20));
        lifePane.getChildren().add(vsLabel);

        // Panneau Ordinateur
        VBox p2Info = new VBox(5);
        p2Info.setAlignment(Pos.CENTER);
        Label p2NameLabel = new Label();
        p2NameLabel.setFont(Font.font(14));

        HBox p2NameBox = new HBox(5, p2StatusRect, p2NameLabel);

        ProgressBar p2HPBar = new ProgressBar(1.0);
        p2HPBar.setPrefWidth(100);
        Label p2HPLabel = new Label();
        p2Info.getChildren().addAll(p2NameBox, p2HPBar, p2HPLabel);

        leftPanel.getChildren().addAll(p1Info, lifePane, p2Info);
        root.setLeft(leftPanel);

        // Zone centrale : 4 boutons d'attaque et boutons supplémentaires
        VBox centerPanel = new VBox(10);
        centerPanel.setAlignment(Pos.CENTER);
        HBox moveButtonsPane = new HBox(10);
        moveButtonsPane.setAlignment(Pos.CENTER);

        Button moveButton1 = new Button();
        Button moveButton2 = new Button();
        Button moveButton3 = new Button();
        Button moveButton4 = new Button();

        moveButton1.setPrefWidth(150);
        moveButton2.setPrefWidth(150);
        moveButton3.setPrefWidth(150);
        moveButton4.setPrefWidth(150);

        moveButtonsPane.getChildren().addAll(moveButton1, moveButton2, moveButton3, moveButton4);

        HBox extraButtonsPane = new HBox(20);
        extraButtonsPane.setAlignment(Pos.CENTER);
        Button switchButton = new Button("Changer de Pokémon");
        Button megaButton = new Button("Mega Évolution");
        extraButtonsPane.getChildren().addAll(switchButton, megaButton);

        centerPanel.getChildren().addAll(moveButtonsPane, extraButtonsPane);
        root.setCenter(centerPanel);

        // Zone inférieure : historique de combat (style sombre et monospace)
        TextArea battleLog = new TextArea();
        battleLog.setEditable(false);
        battleLog.setWrapText(true);
        battleLog.setPrefHeight(150);
        battleLog.setStyle("-fx-control-inner-background: #333; -fx-text-fill: #fff; -fx-font-family: monospace;");
        root.setBottom(battleLog);

        // Mise à jour des panneaux d'infos et des boutons d'attaque
        updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);
        updateMoveButtons(app, moveButton1, moveButton2, moveButton3, moveButton4);

        // Actions pour les 4 boutons d'attaque
        moveButton1.setOnAction(e -> handleMoveButton(app, moveButton1, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton2.setOnAction(e -> handleMoveButton(app, moveButton2, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton3.setOnAction(e -> handleMoveButton(app, moveButton3, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton4.setOnAction(e -> handleMoveButton(app, moveButton4, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));

        // Changement de Pokémon : ce changement consomme le tour
        switchButton.setOnAction(e -> {
            java.util.List<Pokemon> team = app.teamPlayer1;
            if (team.size() <= 1) {
                battleLog.appendText("Aucun autre Pokémon disponible pour changer.\n");
                return;
            }
            Pokemon currentActive = team.get(0);
            java.util.List<Pokemon> choices = new java.util.ArrayList<>(team.subList(1, team.size()));
            ChoiceDialog<Pokemon> switchDialog = new ChoiceDialog<>(choices.get(0), choices);
            switchDialog.setTitle("Changer de Pokémon");
            switchDialog.setHeaderText("Sélectionnez le Pokémon à envoyer (le changement consomme le tour)");
            switchDialog.setContentText("Choix :");
            Optional<Pokemon> selected = switchDialog.showAndWait();
            selected.ifPresent(p -> {
                int index = team.indexOf(p);
                team.set(index, currentActive);
                team.set(0, p);
                battleLog.appendText("Changement de Pokémon: " + p.getName() + " est maintenant actif. Le tour est consommé.\n");
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);
                updateMoveButtons(app, moveButton1, moveButton2, moveButton3, moveButton4);
            });
        });

        // Mega Évolution
        megaButton.setOnAction(e -> {
            Pokemon active = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
            if (active != null && !active.isMegaEvolved()) {
                active.megaEvolve();
                battleLog.appendText(active.getName() + " a méga évolué!\n");
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);
            }
        });

        return new Scene(root, 1000, 600);
    }

    // Gère l'attaque du joueur suivie de la contre-attaque de l'ordinateur
    private static void handleMoveButton(PokemonBattleSimulator app, Button moveButton, TextArea battleLog,
                                         Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                         Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        Pokemon playerPokemon = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        Pokemon compPokemon = app.teamPlayer2.isEmpty() ? null : app.teamPlayer2.get(0);
        if (playerPokemon != null && compPokemon != null) {
            // Recherche de l'index de l'attaque
            int moveIndex = -1;
            for (int i = 0; i < playerPokemon.getMoves().size(); i++) {
                if (playerPokemon.getMoves().get(i).toString().equals(moveButton.getText())) {
                    moveIndex = i;
                    break;
                }
            }
            // Si l'attaque existe
            if (moveIndex >= 0 && moveIndex < playerPokemon.getMoves().size()) {
                Attack move = playerPokemon.getMoves().get(moveIndex);
                int damage = playerPokemon.calculateDamage(compPokemon, move);
                double multiplier = playerPokemon.calculateMultiplier(compPokemon);
                compPokemon.receiveDamage(damage);
                battleLog.appendText(playerPokemon.getName() + " utilise " + move.getName() +
                        " et inflige " + damage + " dégâts.\n");
                if (multiplier > 1.0) {
                    battleLog.appendText("C'est super efficace !\n");
                } else if (multiplier < 1.0) {
                    battleLog.appendText("Ce n'est pas très efficace...\n");
                } else {
                    battleLog.appendText("Attaque normale.\n");
                }
                // On applique l'effet secondaire
                move.applyEffect(playerPokemon, compPokemon, damage, battleLog);

                // On met à jour l'interface
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);

                // Vérifie si le Pokémon adverse est KO
                if (compPokemon.getCurrentHP() <= 0) {
                    battleLog.appendText(compPokemon.getName() + " est KO !\n");
                    return;
                }

                // Tour de l'ordinateur
                if (multiplier > 1.0 && app.teamPlayer2.size() > 1) {
                    // Si l'attaque du joueur est super efficace, l'ordinateur change de Pokémon
                    Random random = new Random();
                    int switchIndex = random.nextInt(app.teamPlayer2.size() - 1) + 1;
                    Pokemon newActive = app.teamPlayer2.get(switchIndex);
                    app.teamPlayer2.set(switchIndex, compPokemon);
                    app.teamPlayer2.set(0, newActive);
                    battleLog.appendText("L'ordinateur change de Pokémon en raison d'une attaque super efficace !\n");
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);

                } else {
                    // L'IA attaque si son Pokémon est toujours vivant
                    if (!compPokemon.getMoves().isEmpty() && compPokemon.getCurrentHP() > 0) {
                        Random random = new Random();
                        Attack compMove = compPokemon.getMoves().get(random.nextInt(compPokemon.getMoves().size()));
                        int compDamage = compPokemon.calculateDamage(playerPokemon, compMove);
                        double compMultiplier = compPokemon.calculateMultiplier(playerPokemon);
                        playerPokemon.receiveDamage(compDamage);
                        battleLog.appendText(compPokemon.getName() + " contre-attaque avec " + compMove.getName() +
                                " et inflige " + compDamage + " dégâts.\n");
                        if (compMultiplier > 1.0) {
                            battleLog.appendText("C'est super efficace !\n");
                        } else if (compMultiplier < 1.0) {
                            battleLog.appendText("Ce n'est pas très efficace...\n");
                        } else {
                            battleLog.appendText("Attaque normale.\n");
                        }
                        compMove.applyEffect(compPokemon, playerPokemon, compDamage, battleLog);
                        updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                    }
                }
            }
        }
    }

    // Met à jour l'affichage (nom, HP, statut)
    public static void updateActivePokemonInfo(PokemonBattleSimulator app,
                                               Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                               Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        Pokemon active1 = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        Pokemon active2 = app.teamPlayer2.isEmpty() ? null : app.teamPlayer2.get(0);

        // Joueur 1
        if (active1 != null) {
            p1NameLabel.setText(active1.getName() + (active1.isMegaEvolved() ? " (Mega)" : ""));
            p1HPBar.setProgress(active1.getCurrentHP() / (double) active1.getMaxHP());
            p1HPLabel.setText("HP: " + active1.getCurrentHP() + "/" + active1.getMaxHP());
            updateStatusColor(active1, p1StatusRect);
        } else {
            p1NameLabel.setText("Aucun");
            p1HPBar.setProgress(0);
            p1HPLabel.setText("HP: 0");
            p1StatusRect.setFill(Color.TRANSPARENT);
        }

        // Ordinateur
        if (active2 != null) {
            p2NameLabel.setText(active2.getName() + (active2.isMegaEvolved() ? " (Mega)" : ""));
            p2HPBar.setProgress(active2.getCurrentHP() / (double) active2.getMaxHP());
            p2HPLabel.setText("HP: " + active2.getCurrentHP() + "/" + active2.getMaxHP());
            updateStatusColor(active2, p2StatusRect);
        } else {
            p2NameLabel.setText("Aucun");
            p2HPBar.setProgress(0);
            p2HPLabel.setText("HP: 0");
            p2StatusRect.setFill(Color.TRANSPARENT);
        }
    }

    // Met à jour la couleur du petit carré en fonction du statut
    private static void updateStatusColor(Pokemon pokemon, Rectangle rect) {
        if (pokemon == null) {
            rect.setFill(Color.TRANSPARENT);
            return;
        }
        switch (pokemon.getStatus()) {
            case BURN:
                rect.setFill(Color.RED);
                break;
            case POISON:
                rect.setFill(Color.PURPLE);
                break;
            case PARALYSIS:
                rect.setFill(Color.YELLOW);
                break;
            default:
                rect.setFill(Color.TRANSPARENT);
        }
    }

    // Met à jour les boutons d'attaque (4 max)
    public static void updateMoveButtons(PokemonBattleSimulator app,
                                         Button btn1, Button btn2, Button btn3, Button btn4) {
        Pokemon active1 = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        if (active1 != null) {
            btn1.setText("");
            btn2.setText("");
            btn3.setText("");
            btn4.setText("");
            for (int i = 0; i < active1.getMoves().size() && i < 4; i++) {
                Attack move = active1.getMoves().get(i);
                btnSetText(i, move, btn1, btn2, btn3, btn4);
            }
        }
    }

    private static void btnSetText(int index, Attack move,
                                   Button btn1, Button btn2, Button btn3, Button btn4) {
        String txt = move.toString(); // ex : "Lance-flamme (Spe)"
        if (index == 0) btn1.setText(txt);
        if (index == 1) btn2.setText(txt);
        if (index == 2) btn3.setText(txt);
        if (index == 3) btn4.setText(txt);
    }
}
