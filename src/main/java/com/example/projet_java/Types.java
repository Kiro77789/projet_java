package com.example.projet_java;

import java.util.List;

public class Types {
    private String name;
    private List<String> strongAgainst;
    private List<String> weakAgainst;
    private List<String> resistantTo;
    private List<String> immuneTo; // nouveau

    public Types(String name, List<String> strongAgainst, List<String> weakAgainst, List<String> resistantTo) {
        this.name = name;
        this.strongAgainst = strongAgainst;
        this.weakAgainst = weakAgainst;
        this.resistantTo = resistantTo;
        this.immuneTo = null; // Par défaut
    }

    // On peut ajouter un constructeur plus complet si besoin
    public Types(String name, List<String> strongAgainst, List<String> weakAgainst,
                 List<String> resistantTo, List<String> immuneTo) {
        this.name = name;
        this.strongAgainst = strongAgainst;
        this.weakAgainst = weakAgainst;
        this.resistantTo = resistantTo;
        this.immuneTo = immuneTo;
    }

    public String getName() {
        return name;
    }

    public boolean isStrongAgainst(String opp) {
        return strongAgainst != null && strongAgainst.contains(opp);
    }

    public boolean isWeakAgainst(String opp) {
        return weakAgainst != null && weakAgainst.contains(opp);
    }

    public boolean isResistantTo(String opp) {
        return resistantTo != null && resistantTo.contains(opp);
    }

    public boolean isImmuneTo(String opp) {
        return immuneTo != null && immuneTo.contains(opp);
    }

    @Override
    public String toString() {
        return "Type: " + name;
    }
}
