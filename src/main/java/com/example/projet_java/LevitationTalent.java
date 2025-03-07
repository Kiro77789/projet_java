package com.example.projet_java;

public class LevitationTalent implements Talent {
    @Override
    public void applyEffect(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " bénéficie de Lévitation (immunité aux attaques de type Sol).");
    }
}
