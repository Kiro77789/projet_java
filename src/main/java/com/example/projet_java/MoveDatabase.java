package com.example.projet_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MoveDatabase {
    public static List<Attack> getDefaultMoves(Pokemon p) {
        List<Attack> moves = new ArrayList<>();
        for (Types t : p.getTypes()) {
            if (t.getName().equalsIgnoreCase("Feu")) {
                moves.add(new Attack("Lance-flamme", 50, t, true, new BurnEffect(0.1)));
            } else if (t.getName().equalsIgnoreCase("Eau")) {
                moves.add(new Attack("Hydrocanon", 55, t, true, null));
            } else if (t.getName().equalsIgnoreCase("Plante")) {
                moves.add(new Attack("Fouet Lianes", 45, t, false, null));
            } else if (t.getName().equalsIgnoreCase("électrique")) {
                moves.add(new Attack("Éclair", 40, t, true, null));
            } else {
                // Par défaut, on met Charge
                Types normal = new Types("Normal", Arrays.asList(), Arrays.asList(), Arrays.asList());
                moves.add(new Attack("Charge", 35, normal, false, null));
            }
        }
        // On limite à 4 attaques
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
        Types normal = new Types("Normal", Arrays.asList(), Arrays.asList(), Arrays.asList());

        moves.add(new Attack("Lance-flamme", 50, feu, true, new BurnEffect(0.1)));
        moves.add(new Attack("Flamèche", 40, feu, true, new BurnEffect(0.1)));
        moves.add(new Attack("Hydrocanon", 55, eau, true, null));
        moves.add(new Attack("Pistolet à O", 45, eau, true, null));
        moves.add(new Attack("Fouet Lianes", 45, plante, false, null));
        moves.add(new Attack("Griffe", 35, normal, false, new RecoilEffect(0.33)));
        moves.add(new Attack("Éclair", 40, electrique, true, null));
        moves.add(new Attack("Charge", 35, normal, false, new StatChangeEffect(0.9)));
        moves.add(new Attack("Giga-Sangsue", 40, plante, true, new DrainEffect()));

        return moves;
    }
}
