package com.example.projet_java;

import javafx.scene.control.TextArea;

public interface AttackEffect {
    void apply(Pokemon attacker, Pokemon defender, int damage, TextArea battleLog);
}
