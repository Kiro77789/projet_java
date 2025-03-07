package com.example.projet_java;

public class Item {
    private String name;
    private ItemEffect effect;

    public Item(String name, ItemEffect effect) {
        this.name = name;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public void applyEffect(Pokemon pokemon) {
        if(effect != null) {
            effect.apply(pokemon);
        }
    }
}
