package com.example.projet_java;

public class ResteEffect implements ItemEffect {
    @Override
    public void apply(Pokemon pokemon) {
        int healAmount = (int)(pokemon.getMaxHP() * 0.08);
        pokemon.heal(healAmount);
        System.out.println(pokemon.getName() + " récupère " + healAmount + " HP grâce aux Restes.");
    }
}
