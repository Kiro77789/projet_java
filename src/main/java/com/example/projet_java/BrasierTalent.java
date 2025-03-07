package com.example.projet_java;

public class BrasierTalent implements Talent {
    @Override
    public void applyEffect(Pokemon pokemon) {
        if(pokemon.getCurrentHP() < pokemon.getMaxHP() / 3) {
            System.out.println(pokemon.getName() + " active Brasier, augmentant ses attaques de type Feu.");
        }
    }
}
