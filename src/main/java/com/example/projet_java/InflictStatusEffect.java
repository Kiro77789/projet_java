package com.example.projet_java;

import javafx.scene.control.TextArea;

// Inflige un statut (PARALYSIS, SLEEP, etc.) sans faire de dégâts
public class InflictStatusEffect implements AttackEffect {
    private Status statusToInflict;

    public InflictStatusEffect(Status statusToInflict) {
        this.statusToInflict = statusToInflict;
    }

    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        // On n'inflige le statut que si le défenseur n'a pas déjà un statut
        if (defender.getStatus() == Status.NONE) {
            defender.setStatus(statusToInflict);
            if (battleLog != null) {
                battleLog.appendText(defender.getName() + " est maintenant " + statusToInflict + " !\n");
            } else {
                System.out.println(defender.getName() + " reçoit le statut " + statusToInflict);
            }
        } else {
            if (battleLog != null) {
                battleLog.appendText(defender.getName() + " a déjà un statut et n'est pas affecté.\n");
            }
        }
    }
}

// Augmente/diminue les stages (ex: +2 Attack)
class StageChangeEffect implements AttackEffect {
    private String stat;
    private int amount;

    public StageChangeEffect(String stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        // On applique le buff sur l'attaquant
        attacker.changeStage(stat, amount);
        if (battleLog != null) {
            battleLog.appendText(attacker.getName() + " augmente son " + stat + " de " + amount + " niveau(x)!\n");
        }
    }
}
