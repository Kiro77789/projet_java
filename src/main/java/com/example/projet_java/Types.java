package com.example.projet_java;

import java.util.Arrays;
import java.util.List;

public class Types {
    private String name;
    private List<String> strongAgainst;  // Types contre lesquels ce type est fort
    private List<String> weakAgainst;    // Types contre lesquels ce type est faible
    private List<String> resistantTo;    // Types contre lesquels ce type résiste

    public Types(String name, List<String> strongAgainst, List<String> weakAgainst, List<String> resistantTo) {
        this.name = name;
        this.strongAgainst = strongAgainst;
        this.weakAgainst = weakAgainst;
        this.resistantTo = resistantTo;
    }

    public String getName() {
        return name;
    }

    public boolean isStrongAgainst(String opponentType) {
        return strongAgainst.contains(opponentType);
    }

    public boolean isWeakAgainst(String opponentType) {
        return weakAgainst.contains(opponentType);
    }

    public boolean isResistantTo(String opponentType) {
        return resistantTo.contains(opponentType);
    }

    @Override
    public String toString() {
        return "Type: " + name;
    }
}
