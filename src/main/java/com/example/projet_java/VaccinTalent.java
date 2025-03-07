package com.example.projet_java;

public class VaccinTalent implements Talent {
    @Override
    public void applyEffect(Pokemon pokemon) {
        if(pokemon.getStatus() == Status.POISON) {
            pokemon.setStatus(Status.NONE);
            System.out.println(pokemon.getName() + " est immunisé contre le poison grâce à Vaccin.");
        }
    }
}
