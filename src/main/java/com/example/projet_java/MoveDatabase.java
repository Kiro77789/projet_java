package com.example.projet_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MoveDatabase {
    public static List<Attack> getDefaultMoves(Pokemon p) {
        List<Attack> moves = new ArrayList<>();
        // Pour chaque type du Pokémon, ajouter une attaque par défaut
        for (Types t : p.getTypes()) {
            if (t.getName().equalsIgnoreCase("Feu")) {
                moves.add(new Attack("Lance-flamme", 50, t, true, new BurnEffect(0.1)));
            } else if (t.getName().equalsIgnoreCase("Eau")) {
                moves.add(new Attack("Hydrocanon", 55, t, true, null));
            } else if (t.getName().equalsIgnoreCase("Plante")) {
                moves.add(new Attack("Fouet Lianes", 45, t, false, null));
            } else if (t.getName().equalsIgnoreCase("électrique")) {
                moves.add(new Attack("Éclair", 40, t, true, null));
            }
        }
        // Si aucune attaque n'a été ajoutée, ajouter une attaque par défaut "Charge"
        if (moves.isEmpty()) {
            Types normal = TypeDatabase.getType("Normal");
            if (normal == null) {
                normal = new Types("Normal", Arrays.asList(), Arrays.asList(), Arrays.asList());
            }
            moves.add(new Attack("Charge", 35, normal, false, null));
        }
        // Limiter à 4 attaques (si plus, on enlève l'excédent)
        while (moves.size() > 4) {
            moves.remove(moves.size() - 1);
        }
        return moves;
    }

    public static List<Attack> getAllMoves() {
        List<Attack> moves = new ArrayList<>();
        Types feu = TypeDatabase.getType("Feu");
        Types eau = TypeDatabase.getType("Eau");
        Types plante = TypeDatabase.getType("Plante");
        Types electrique = TypeDatabase.getType("électrique");
        Types normal = TypeDatabase.getType("Normal");
        if (normal == null) {
            normal = new Types("Normal", Arrays.asList(), Arrays.asList(), Arrays.asList());
        }
        moves.add(new Attack("Lance-flamme", 50, feu, true, new BurnEffect(0.1)));
        moves.add(new Attack("Hydrocanon", 55, eau, true, null));
        moves.add(new Attack("Fouet Lianes", 45, plante, false, null));
        moves.add(new Attack("Éclair", 40, electrique, true, null));
        moves.add(new Attack("Charge", 35, normal, false, null));
        // Attaques de statut (pas de dégâts)
        moves.add(new Attack("Para Spore", 0, plante, false, new InflictStatusEffect(Status.PARALYSIS)));
        moves.add(new Attack("Sleep Powder", 0, plante, false, new InflictStatusEffect(Status.SLEEP)));
        // Attaques de buff (ex. Danse-Lames qui augmente l'attaque)
        moves.add(new Attack("Danse-Lames", 0, normal, false, new StageChangeEffect("attack", 2)));
        return moves;
    }
}
