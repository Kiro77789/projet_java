package com.example.projet_java;

import javafx.scene.control.TextArea;

class StatChangeEffect implements AttackEffect {
    private double factor;

    public StatChangeEffect(double factor) {
        this.factor = factor;
    }

    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        if (factor < 1) {
            if (battleLog != null) {
                battleLog.appendText(defender.getName() + " voit son attaque diminuer.\n");
            } else {
                System.out.println(defender.getName() + " voit son attaque diminuer.");
            }
        } else {
            if (battleLog != null) {
                battleLog.appendText(attacker.getName() + " voit son attaque augmenter.\n");
            } else {
                System.out.println(attacker.getName() + " voit son attaque augmenter.");
            }
        }
    }
}
