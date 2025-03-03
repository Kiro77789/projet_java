package com.example.projet_java;

public class Combat {

    static void checkCombat(Pokemon p1, Pokemon p2) {
        TypeDatabase typeDatabase = new TypeDatabase();

        // Récupérer les types des Pokémon
        Types p1Type = p1.type;
        Types p2Type = p2.type;

        // Vérifier si p1 est fort contre p2
        if (p1Type.isStrongAgainst(p2Type.getName())) {
            System.out.println(p1.getName() + " est fort contre " + p2.getName());
        } else if (p1Type.isWeakAgainst(p2Type.getName())) {
            System.out.println(p1.getName() + " est faible contre " + p2.getName());
        } else if (p1Type.isResistantTo(p2Type.getName())) {
            System.out.println(p2.getName() + " est inéficcace contre " + p1.getName());
        } else {
            System.out.println(p1.getName() + " est normale contre " + p2.getName());
        }

        // Vérifier si p2 est fort contre p1
        if (p2Type.isStrongAgainst(p1Type.getName())) {
            System.out.println(p2.getName() + " est fort contre " + p1.getName());
        } else if (p2Type.isWeakAgainst(p1Type.getName())) {
            System.out.println(p2.getName() + " est faible contre " + p1.getName());
        } else if (p2Type.isResistantTo(p1Type.getName())) {
            System.out.println(p2.getName() + " est inéficcace contre " + p1.getName());
        } else {
            System.out.println(p2.getName() + " est normale contre " + p1.getName());
        }
    }

}
