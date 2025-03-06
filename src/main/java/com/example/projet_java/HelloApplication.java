package com.example.projet_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    public Stage primaryStage;
    public Scene menuScene;
    public Scene teamSelectionScene;
    public Scene combatScene;
    public Scene creditsScene;

    // Données de jeu
    public Pokemon[] availablePokemons;
    public List<Pokemon> teamPlayer1 = new ArrayList<>();
    public List<Pokemon> teamPlayer2 = new ArrayList<>();
    public int currentPlayer = 1; // 1 ou 2

    @Override
    public void init() {
        // Initialisation de quelques Pokémon disponibles
        availablePokemons = new Pokemon[] {
                new Pokemon("Pikachu", 200, 55, 40, 50, 50, 90, "électrique", "Poings"),
                new Pokemon("Carapuce", 250, 48, 65, 50, 64, 43, "Eau", "Plume"),
                new Pokemon("Bulbizarre", 300, 49, 49, 65, 65, 45, "Plante", "CeintureForce"),
                new Pokemon("Salamèche", 240, 52, 43, 60, 50, 65, "Feu", "Poings"),
                new Pokemon("Dracaufeu", 360, 84, 78, 109, 85, 100, "Feu", "Poings"),
                new Pokemon("Ronflex", 500, 110, 65, 65, 110, 30, "Normal", "Reste"),
                new Pokemon("Mewtwo", 400, 110, 90, 154, 90, 130, "Psy", "Plume")
                // Ajoutez d'autres Pokémon si besoin
        };
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        // Création des différentes scènes en passant une référence à this
        menuScene = MenuSceneUI.getScene(this);
        creditsScene = CreditsSceneUI.getScene(this);
        teamSelectionScene = TeamSelectionUI.getScene(this);
        combatScene = CombatUI.getScene(this);

        primaryStage.setTitle("Pokémon Showdown Simulator");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    // Méthodes de transition entre les scènes
    public void showMenuScene() {
        primaryStage.setScene(menuScene);
    }

    public void showCreditsScene() {
        primaryStage.setScene(creditsScene);
    }

    public void showTeamSelectionScene() {
        // Réinitialise les équipes et le joueur courant
        teamPlayer1.clear();
        teamPlayer2.clear();
        currentPlayer = 1;
        TeamSelectionUI.updateTeamSelectionLabel(this);
        TeamSelectionUI.clearTeamGrid();
        primaryStage.setScene(teamSelectionScene);
    }

    public void showCombatScene() {
        CombatUI.setupBattleScene(this);
        primaryStage.setScene(combatScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
