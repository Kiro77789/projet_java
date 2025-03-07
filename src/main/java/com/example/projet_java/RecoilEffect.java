package com.example.projet_java;

import javafx.scene.control.TextArea;

class RecoilEffect implements AttackEffect {
    private double fraction;

    public RecoilEffect(double fraction) {
        this.fraction = fraction;
    }

    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        int recoilDamage = (int)(damage * fraction);
        attacker.receiveDamage(recoilDamage);
        if (battleLog != null) {
            battleLog.appendText(attacker.getName() + " subit " + recoilDamage + " dégâts de recul.\n");
        } else {
            System.out.println(attacker.getName() + " subit " + recoilDamage + " dégâts de recul.");
        }
    }
}
