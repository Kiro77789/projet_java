package com.example.projet_java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonBattleSimulator extends Application {

    public List<Pokemon> availablePokemons = new ArrayList<>();
    public List<Pokemon> teamPlayer1 = new ArrayList<>();
    public List<Pokemon> teamPlayer2 = new ArrayList<>();
    public int currentPlayer = 1;
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        // Initialisation des Pokémons disponibles (au moins 10)
        availablePokemons.add(new Pokemon("Magicarpe", 250, 52, 20, 75, 66, 100, new String[]{"Eau"}, new BrasierTalent()));
        availablePokemons.add(new Pokemon("Salamèche", 200, 70, 40, 90, 50, 80, new String[]{"Feu"}, new BrasierTalent()));
        availablePokemons.add(new Pokemon("Bulbizarre", 220, 65, 45, 70, 60, 70, new String[]{"Plante"}, new VaccinTalent()));
        availablePokemons.add(new Pokemon("Carapuce", 230, 60, 50, 65, 55, 75, new String[]{"Eau"}, new VaccinTalent()));
        availablePokemons.add(new Pokemon("Pikachu", 180, 75, 35, 80, 40, 110, new String[]{"électrique"}, new BrasierTalent()));
        availablePokemons.add(new Pokemon("Roucool", 190, 65, 40, 75, 50, 90, new String[]{"Vol"}, new LevitationTalent()));
        availablePokemons.add(new Pokemon("Onix", 300, 80, 100, 40, 50, 30, new String[]{"Sol"}, new VaccinTalent()));
        availablePokemons.add(new Pokemon("Evoli", 200, 70, 50, 70, 50, 80, new String[]{"Normal"}, new LevitationTalent()));
        availablePokemons.add(new Pokemon("Dracaufeu", 240, 90, 60, 100, 70, 95, new String[]{"Feu"}, new BrasierTalent()));
        availablePokemons.add(new Pokemon("Mewtwo", 280, 110, 70, 140, 90, 130, new String[]{"Psy"}, new LevitationTalent()));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showMenuScene();
        primaryStage.setTitle("Simulateur de combat Pokémon");
        primaryStage.show();
    }

    public void showMenuScene() {
        Scene scene = MenuSceneUI.getScene(this);
        primaryStage.setScene(scene);
    }

    public void showTeamSelectionScene() {
        Scene scene = TeamSelectionUI.getScene(this);
        primaryStage.setScene(scene);
    }

    public void showCombatScene() {
        // Génération automatique de l'équipe adverse si vide
        if (teamPlayer2.isEmpty()) {
            Random random = new Random();
            int teamSize = 3; // par exemple, 3 Pokémon pour l'IA
            while (teamPlayer2.size() < teamSize) {
                Pokemon candidate = availablePokemons.get(random.nextInt(availablePokemons.size()));
                // Utiliser une copie pour éviter le partage d'instance
                if (!teamPlayer2.contains(candidate)) {
                    teamPlayer2.add(Pokemon.copyOf(candidate));
                }
            }
        }
        Scene scene = CombatUI.getScene(this);
        primaryStage.setScene(scene);
    }

    public void showCreditsScene() {
        Scene scene = CreditsSceneUI.getScene(this);
        primaryStage.setScene(scene);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
