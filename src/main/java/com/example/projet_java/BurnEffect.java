package com.example.projet_java;

import javafx.scene.control.TextArea;

public class BurnEffect implements AttackEffect {
    private double chance;

    public BurnEffect(double chance) {
        this.chance = chance;
    }

    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        if (Math.random() < chance) {
            defender.setStatus(Status.BURN);
            if (battleLog != null) {
                battleLog.appendText(defender.getName() + " est brûlé !\n");
            } else {
                System.out.println(defender.getName() + " est brûlé !");
            }
        }
    }
}
