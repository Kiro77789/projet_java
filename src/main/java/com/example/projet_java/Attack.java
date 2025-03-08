package com.example.projet_java;

import javafx.scene.control.TextArea;

public class Attack {
    private String name;
    private int power;
    private Types type;
    private boolean isSpecial;
    private AttackEffect effect; // Peut être null

    public Attack(String name, int power, Types type, boolean isSpecial, AttackEffect effect) {
        this.name = name;
        this.power = power;
        this.type = type;
        this.isSpecial = isSpecial;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public Types getType() {
        return type;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public AttackEffect getEffect() {
        return effect;
    }

    public void applyEffect(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog) {
        if (effect != null) {
            effect.apply(attacker, defender, damage, battleLog);
        }
    }

    @Override
    public String toString() {
        return name + (isSpecial ? " (Spe)" : " (Phy)");
    }
}
