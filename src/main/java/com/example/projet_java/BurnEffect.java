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
            int burnDamage = defender.getMaxHP() / 8;
            defender.receiveDamage(burnDamage);
            if (battleLog != null) {
                battleLog.appendText(defender.getName() + " perd " + burnDamage + " HP à cause de la brûlure.\n");
                battleLog.appendText(defender.getName() + " est brûlé !\n");
            } else {
                System.out.println(defender.getName() + " perd " + burnDamage + " HP à cause de la brûlure.");
                System.out.println(defender.getName() + " est brûlé !");
            }
        }
    }
}
