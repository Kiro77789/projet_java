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

    // Rectangles pour indiquer le statut (ex. rouge pour Burn, violet pour Poison, jaune pour Paralysie)
    private static Rectangle p1StatusRect = new Rectangle(10, 10, Color.TRANSPARENT);
    private static Rectangle p2StatusRect = new Rectangle(10, 10, Color.TRANSPARENT);

    // FlowPane pour afficher la liste des Pokémon du joueur pour changer de Pokémon
    private static FlowPane playerTeamPane = new FlowPane(10, 10);

    public static Scene getScene(PokemonBattleSimulator app) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Conteneur pour les infos de combat (panneaux HP + VS)
        BorderPane battleInfoPane = new BorderPane();
        battleInfoPane.setPadding(new Insets(10));
        // Appliquer une bordure autour de l'ensemble
        battleInfoPane.setStyle("-fx-border-color: gray; -fx-border-width: 2;");

        // Panneau Joueur (gauche)
        VBox p1Info = new VBox(5);
        p1Info.setAlignment(Pos.CENTER);
        Label p1NameLabel = new Label();
        p1NameLabel.setFont(Font.font(14));
        HBox p1NameBox = new HBox(5, p1StatusRect, p1NameLabel);
        p1NameBox.setAlignment(Pos.CENTER);
        ProgressBar p1HPBar = new ProgressBar(1.0);
        p1HPBar.setPrefWidth(100);
        Label p1HPLabel = new Label();
        p1Info.getChildren().addAll(p1NameBox, p1HPBar, p1HPLabel);

        // Panneau IA (droite)
        VBox p2Info = new VBox(5);
        p2Info.setAlignment(Pos.CENTER);
        Label p2NameLabel = new Label();
        p2NameLabel.setFont(Font.font(14));
        HBox p2NameBox = new HBox(5, p2StatusRect, p2NameLabel);
        p2NameBox.setAlignment(Pos.CENTER);
        ProgressBar p2HPBar = new ProgressBar(1.0);
        p2HPBar.setPrefWidth(100);
        Label p2HPLabel = new Label();
        p2Info.getChildren().addAll(p2NameBox, p2HPBar, p2HPLabel);

        // Label "VS" au centre
        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font(20));
        StackPane vsPane = new StackPane(vsLabel);
        vsPane.setPrefSize(100, 100);
        // Le contour de vsPane n'est plus nécessaire car battleInfoPane a sa bordure

        battleInfoPane.setLeft(p1Info);
        battleInfoPane.setCenter(vsPane);
        battleInfoPane.setRight(p2Info);
        root.setTop(battleInfoPane);
        BorderPane.setAlignment(battleInfoPane, Pos.CENTER);

        // Zone centrale : 4 boutons d'attaque
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

        // FlowPane pour le menu de changement de Pokémon directement sous les attaques
        playerTeamPane.setAlignment(Pos.CENTER);
        playerTeamPane.setPadding(new Insets(10));
        updatePlayerTeamPane(app, null, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);

        // Bouton Mega Évolution
        Button megaButton = new Button("Mega Évolution");
        megaButton.setOnAction(e -> {
            Pokemon active = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
            if (active != null && !active.isMegaEvolved()) {
                active.megaEvolve();
            }
            updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);
        });

        centerPanel.getChildren().addAll(moveButtonsPane, megaButton, playerTeamPane);
        root.setCenter(centerPanel);

        // Zone inférieure : historique
        TextArea battleLog = new TextArea();
        battleLog.setEditable(false);
        battleLog.setWrapText(true);
        battleLog.setPrefHeight(150);
        battleLog.setStyle("-fx-control-inner-background: #333; -fx-text-fill: #fff; -fx-font-family: monospace;");
        root.setBottom(battleLog);

        updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel);
        updateMoveButtons(app, moveButton1, moveButton2, moveButton3, moveButton4);

        // Actions pour les boutons d'attaque
        moveButton1.setOnAction(e -> handleMoveButton(app, moveButton1, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton2.setOnAction(e -> handleMoveButton(app, moveButton2, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton3.setOnAction(e -> handleMoveButton(app, moveButton3, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));
        moveButton4.setOnAction(e -> handleMoveButton(app, moveButton4, battleLog,
                p1NameLabel, p1HPBar, p1HPLabel, p2NameLabel, p2HPBar, p2HPLabel));

        return new Scene(root, 1000, 600);
    }

    private static void updatePlayerTeamPane(PokemonBattleSimulator app, TextArea battleLog,
                                             Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                             Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        playerTeamPane.getChildren().clear();
        for (int i = 0; i < app.teamPlayer1.size(); i++) {
            final int index = i;
            final Pokemon p = app.teamPlayer1.get(i);
            Button pokeBtn = new Button(p.getName() + " (HP: " + p.getCurrentHP() + ")");
            pokeBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent e) {
                    if (index == 0) return;
                    Pokemon currentActive = app.teamPlayer1.get(0);
                    app.teamPlayer1.set(0, p);
                    app.teamPlayer1.set(index, currentActive);
                    if (battleLog != null) {
                        battleLog.appendText("Vous envoyez " + p.getName() + " !\n");
                    }
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    updateMoveButtons(app, null, null, null, null);
                    updatePlayerTeamPane(app, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                }
            });
            playerTeamPane.getChildren().add(pokeBtn);
        }
    }

    private static void handleMoveButton(PokemonBattleSimulator app, Button moveButton, TextArea battleLog,
                                         Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                         Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        Pokemon playerPokemon = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        Pokemon compPokemon = app.teamPlayer2.isEmpty() ? null : app.teamPlayer2.get(0);
        if (playerPokemon == null || compPokemon == null || moveButton == null) return;

        int moveIndex = -1;
        for (int i = 0; i < playerPokemon.getMoves().size(); i++) {
            if (playerPokemon.getMoves().get(i).toString().equals(moveButton.getText())) {
                moveIndex = i;
                break;
            }
        }
        if (moveIndex < 0) return;
        Attack playerMove = playerPokemon.getMoves().get(moveIndex);

        // L'IA choisit son move aléatoirement
        Attack compMove = null;
        if (!compPokemon.getMoves().isEmpty()) {
            Random r = new Random();
            compMove = compPokemon.getMoves().get(r.nextInt(compPokemon.getMoves().size()));
        }

        // Comparaison des vitesses pour déterminer l'ordre d'attaque
        double playerSpeed = playerPokemon.getSpeedValueForPriorityCheck();
        double compSpeed = CombatUI.getSpeedValueForPriorityCheck(compPokemon);

        if (playerSpeed > compSpeed) {
            if (doAttack(app, playerPokemon, compPokemon, playerMove, battleLog)) {
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);
                if (compPokemon.getCurrentHP() <= 0) {
                    battleLog.appendText(compPokemon.getName() + " est KO !\n");
                    forceSwitchOrEnd(app, false, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    return;
                }
            }
            if (compMove != null && compPokemon.getCurrentHP() > 0) {
                if (doAttack(app, compPokemon, playerPokemon, compMove, battleLog)) {
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    if (playerPokemon.getCurrentHP() <= 0) {
                        battleLog.appendText(playerPokemon.getName() + " est KO !\n");
                        forceSwitchOrEnd(app, true, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        return;
                    }
                }
            }
        } else if (playerSpeed < compSpeed) {
            if (compMove != null) {
                if (doAttack(app, compPokemon, playerPokemon, compMove, battleLog)) {
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    if (playerPokemon.getCurrentHP() <= 0) {
                        battleLog.appendText(playerPokemon.getName() + " est KO !\n");
                        forceSwitchOrEnd(app, true, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        return;
                    }
                }
            }
            if (doAttack(app, playerPokemon, compPokemon, playerMove, battleLog)) {
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);
                if (compPokemon.getCurrentHP() <= 0) {
                    battleLog.appendText(compPokemon.getName() + " est KO !\n");
                    forceSwitchOrEnd(app, false, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    return;
                }
            }
        } else {
            // Égalité de vitesse, départ aléatoire
            if (Math.random() < 0.5) {
                if (doAttack(app, playerPokemon, compPokemon, playerMove, battleLog)) {
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    if (compPokemon.getCurrentHP() <= 0) {
                        battleLog.appendText(compPokemon.getName() + " est KO !\n");
                        forceSwitchOrEnd(app, false, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        return;
                    }
                }
                if (compMove != null) {
                    if (doAttack(app, compPokemon, playerPokemon, compMove, battleLog)) {
                        updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        if (playerPokemon.getCurrentHP() <= 0) {
                            battleLog.appendText(playerPokemon.getName() + " est KO !\n");
                            forceSwitchOrEnd(app, true, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                    p2NameLabel, p2HPBar, p2HPLabel);
                            return;
                        }
                    }
                }
            } else {
                if (compMove != null) {
                    if (doAttack(app, compPokemon, playerPokemon, compMove, battleLog)) {
                        updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        if (playerPokemon.getCurrentHP() <= 0) {
                            battleLog.appendText(playerPokemon.getName() + " est KO !\n");
                            forceSwitchOrEnd(app, true, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                    p2NameLabel, p2HPBar, p2HPLabel);
                            return;
                        }
                    }
                }
                if (doAttack(app, playerPokemon, compPokemon, playerMove, battleLog)) {
                    updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                            p2NameLabel, p2HPBar, p2HPLabel);
                    if (compPokemon.getCurrentHP() <= 0) {
                        battleLog.appendText(compPokemon.getName() + " est KO !\n");
                        forceSwitchOrEnd(app, false, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                                p2NameLabel, p2HPBar, p2HPLabel);
                        return;
                    }
                }
            }
        }
    }

    private static boolean doAttack(PokemonBattleSimulator app, Pokemon attacker, Pokemon defender,
                                    Attack move, TextArea battleLog) {
        int damage = attacker.calculateDamage(defender, move);
        if (damage == 0 && attacker.getStatus() == Status.SLEEP) {
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " est endormi et n'attaque pas.\n");
            }
            return false;
        } else if (damage == 0 && attacker.getStatus() == Status.PARALYSIS) {
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " est paralysé et ne peut pas attaquer.\n");
            }
            return false;
        } else if (damage == 0 && move.getPower() > 0) {
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " a raté son attaque ou est bloqué.\n");
            }
        }
        if (move.getPower() > 0) {
            defender.receiveDamage(damage);
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " utilise " + move.getName()
                        + " et inflige " + damage + " dégâts.\n");
            }
        } else {
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " utilise " + move.getName() + " !\n");
            }
        }
        move.applyEffect(attacker, defender, damage, battleLog);
        return true;
    }

    private static void forceSwitchOrEnd(PokemonBattleSimulator app, boolean isPlayer,
                                         TextArea battleLog,
                                         Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                         Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        if (isPlayer) {
            if (app.teamPlayer1.size() <= 1) {
                battleLog.appendText("Vous n'avez plus de Pokémon disponibles. Défaite...\n");
                return;
            } else {
                java.util.List<Pokemon> alive = new java.util.ArrayList<>();
                for (Pokemon p : app.teamPlayer1) {
                    if (p.getCurrentHP() > 0) alive.add(p);
                }
                if (alive.isEmpty()) {
                    battleLog.appendText("Tous vos Pokémon sont KO. Défaite...\n");
                    return;
                }
                Pokemon newActive = alive.get(0);
                if (app.teamPlayer1.get(0) != newActive) {
                    app.teamPlayer1.remove(newActive);
                    app.teamPlayer1.add(0, newActive);
                    battleLog.appendText("Vous envoyez " + newActive.getName() + " !\n");
                }
                newActive.resetStages();
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);
                updatePlayerTeamPane(app, battleLog, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);
            }
        } else {
            if (app.teamPlayer2.size() <= 1) {
                battleLog.appendText("L'adversaire n'a plus de Pokémon. Victoire !\n");
                return;
            } else {
                java.util.List<Pokemon> alive = new java.util.ArrayList<>();
                for (Pokemon p : app.teamPlayer2) {
                    if (p.getCurrentHP() > 0) alive.add(p);
                }
                if (alive.isEmpty()) {
                    battleLog.appendText("L'adversaire n'a plus de Pokémon. Victoire !\n");
                    return;
                }
                Pokemon newActive = alive.get(0);
                if (app.teamPlayer2.get(0) != newActive) {
                    app.teamPlayer2.remove(newActive);
                    app.teamPlayer2.add(0, newActive);
                    battleLog.appendText("L'adversaire envoie " + newActive.getName() + " !\n");
                }
                updateActivePokemonInfo(app, p1NameLabel, p1HPBar, p1HPLabel,
                        p2NameLabel, p2HPBar, p2HPLabel);
            }
        }
    }

    public static void updateActivePokemonInfo(PokemonBattleSimulator app,
                                               Label p1NameLabel, ProgressBar p1HPBar, Label p1HPLabel,
                                               Label p2NameLabel, ProgressBar p2HPBar, Label p2HPLabel) {
        Pokemon active1 = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        Pokemon active2 = app.teamPlayer2.isEmpty() ? null : app.teamPlayer2.get(0);

        if (active1 != null) {
            p1NameLabel.setText(active1.getName() + (active1.isMegaEvolved() ? " (Mega)" : ""));
            if (active1.getCurrentHP() <= 0) {
                p1HPLabel.setText("KO !");
                p1HPBar.setProgress(0);
            } else {
                p1HPLabel.setText("HP: " + active1.getCurrentHP() + "/" + active1.getMaxHP());
                p1HPBar.setProgress(active1.getCurrentHP() / (double) active1.getMaxHP());
            }
            updateStatusColor(active1, p1StatusRect);
        } else {
            p1NameLabel.setText("Aucun");
            p1HPBar.setProgress(0);
            p1HPLabel.setText("HP: 0");
            p1StatusRect.setFill(Color.TRANSPARENT);
        }

        if (active2 != null) {
            p2NameLabel.setText(active2.getName() + (active2.isMegaEvolved() ? " (Mega)" : ""));
            if (active2.getCurrentHP() <= 0) {
                p2HPLabel.setText("KO !");
                p2HPBar.setProgress(0);
            } else {
                p2HPLabel.setText("HP: " + active2.getCurrentHP() + "/" + active2.getMaxHP());
                p2HPBar.setProgress(active2.getCurrentHP() / (double) active2.getMaxHP());
            }
            updateStatusColor(active2, p2StatusRect);
        } else {
            p2NameLabel.setText("Aucun");
            p2HPBar.setProgress(0);
            p2HPLabel.setText("HP: 0");
            p2StatusRect.setFill(Color.TRANSPARENT);
        }
    }

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

    public static void updateMoveButtons(PokemonBattleSimulator app,
                                         Button btn1, Button btn2, Button btn3, Button btn4) {
        if (btn1 == null || btn2 == null || btn3 == null || btn4 == null) return;
        Pokemon active1 = app.teamPlayer1.isEmpty() ? null : app.teamPlayer1.get(0);
        if (active1 != null) {
            btn1.setText("");
            btn2.setText("");
            btn3.setText("");
            btn4.setText("");
            for (int i = 0; i < active1.getMoves().size() && i < 4; i++) {
                Attack move = active1.getMoves().get(i);
                String buttonText = move.toString();
                if (i == 0) btn1.setText(buttonText);
                if (i == 1) btn2.setText(buttonText);
                if (i == 2) btn3.setText(buttonText);
                if (i == 3) btn4.setText(buttonText);
            }
        }
    }

    // Méthode utilitaire pour obtenir la vitesse effective d'un Pokémon pour la priorité
    public static double getSpeedValueForPriorityCheck(Pokemon p) {
        double speed = p.getSpeed();
        if (p.getStatus() == Status.PARALYSIS) {
            speed /= 4.0;
        }
        return speed;
    }
}
