package com.example.projet_java;

import javafx.scene.control.TextArea;

public class DrainEffect implements AttackEffect {
    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        int healAmount = damage / 2;
        attacker.heal(healAmount);
        if (battleLog != null) {
            battleLog.appendText(attacker.getName() + " récupère " + healAmount + " HP grâce à Giga-Sangsue.\n");
        } else {
            System.out.println(attacker.getName() + " récupère " + healAmount + " HP.");
        }
    }
}
