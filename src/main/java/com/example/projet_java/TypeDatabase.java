package com.example.projet_java;

import java.util.HashMap;
import java.util.Arrays;

public class TypeDatabase {
    private static final HashMap<String, Types> types = new HashMap<>();

    static {
        types.put("Feu", new Types("Feu",
                Arrays.asList("Plante", "Glace", "Insecte", "Acier"), // Fort contre
                Arrays.asList("Eau", "Roche", "Sol"), // Faible contre
                Arrays.asList("Feu", "Plante", "Glace", "Insecte", "Acier", "Fée"))); // Résiste à

        types.put("Eau", new Types("Eau",
                Arrays.asList("Feu", "Roche", "Sol"),
                Arrays.asList("Électrik", "Plante"),
                Arrays.asList("Eau", "Glace", "Acier", "Feu")));

        types.put("Plante", new Types("Plante",
                Arrays.asList("Eau", "Roche", "Sol"),
                Arrays.asList("Feu", "Glace", "Poison", "Vol", "Insecte"),
                Arrays.asList("Eau", "Électrik", "Sol")));

        types.put("électrique", new Types("électrique",
                Arrays.asList("Eau", "Vol"),
                Arrays.asList("Sol"),
                Arrays.asList("électrique", "Acier", "Vol")));

        types.put("Sol", new Types("Sol",
                Arrays.asList("électrique", "Feu", "Roche", "Poison", "Acier"),
                Arrays.asList("Eau", "Glace", "Plante"),
                Arrays.asList("Poison", "Roche", "Vol")));

        types.put("Vol", new Types("Vol",
                Arrays.asList("Plante", "Combat", "Insecte"),
                Arrays.asList("électrique", "Glace", "Roche"),
                Arrays.asList("Combat", "Insecte")));
    }

    public static Types getType(String name) {
        return types.get(name);
    }
}
