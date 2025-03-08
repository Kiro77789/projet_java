package com.example.projet_java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TypeDatabase {
    private static final HashMap<String, Types> types = new HashMap<>();

    static {
        // On enrichit ou corrige les listes pour se rapprocher du tableau officiel
        types.put("Feu", new Types("Feu",
                Arrays.asList("Plante", "Glace", "Insecte", "Acier"),   // Fort contre
                Arrays.asList("Eau", "Roche", "Sol", "Dragon"),        // Faible contre
                Arrays.asList("Feu", "Plante", "Glace", "Insecte", "Acier", "Fée"))); // Résiste

        types.put("Eau", new Types("Eau",
                Arrays.asList("Feu", "Roche", "Sol"),
                Arrays.asList("Électrique", "Plante", "Dragon"),
                Arrays.asList("Eau", "Glace", "Acier", "Feu")));

        types.put("Plante", new Types("Plante",
                Arrays.asList("Eau", "Roche", "Sol"),
                Arrays.asList("Feu", "Glace", "Poison", "Vol", "Insecte", "Dragon"),
                Arrays.asList("Eau", "Électrique", "Sol")));

        types.put("électrique", new Types("électrique",
                Arrays.asList("Eau", "Vol"),
                Arrays.asList("Sol", "Plante", "Dragon"),
                Arrays.asList("électrique", "Acier", "Vol")));

        types.put("Sol", new Types("Sol",
                Arrays.asList("électrique", "Feu", "Roche", "Poison", "Acier"),
                Arrays.asList("Eau", "Glace", "Plante"),
                Arrays.asList("Poison", "Roche"))); // En vrai, Sol n'est pas immunisé par Vol

        types.put("Vol", new Types("Vol",
                Arrays.asList("Plante", "Combat", "Insecte"),
                Arrays.asList("électrique", "Glace", "Roche"),
                Arrays.asList("Combat", "Insecte", "Plante")));

        // On peut ajouter Normal, etc.
        types.put("Normal", new Types("Normal",
                Arrays.asList(), // Pas de super efficacité
                Arrays.asList("Roche", "Acier"),
                Arrays.asList())); // Pas de résistance
    }

    public static Types getType(String name) {
        return types.get(name);
    }

    // Calcule le multiplicateur global en tenant compte des immunités
    // Ex : Vol immunisé contre Sol => 0
    // On additionne (ou multiplie) les effets sur chaque type du défenseur
    public static double getEffectiveness(Types attackType, List<Types> defenderTypes) {
        if (attackType == null) return 1.0;
        double result = 1.0;
        for (Types def : defenderTypes) {
            // Check immunités
            // ex: "Sol" => immunisé par "Vol"? => On gère autrement si on a un champ d'immunités
            // Ici, on triche un peu : si def.isResistantTo(attackType.getName()) => x0.5, etc.
            if (def.isImmuneTo(attackType.getName())) {
                return 0.0;
            }
            if (attackType.isStrongAgainst(def.getName())) {
                result *= 2.0;
            }
            if (attackType.isWeakAgainst(def.getName())) {
                result *= 0.5;
            }
        }
        return result;
    }
}
