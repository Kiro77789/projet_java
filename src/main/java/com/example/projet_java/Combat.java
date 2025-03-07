package com.example.projet_java;

public class Combat {
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private int turn;

    public Combat(Pokemon p1, Pokemon p2) {
        this.pokemon1 = p1;
        this.pokemon2 = p2;
        this.turn = 1;
    }

    public void start() {
        System.out.println("Début du combat entre " + pokemon1.getName() + " et " + pokemon2.getName());
        while(pokemon1.getCurrentHP() > 0 && pokemon2.getCurrentHP() > 0) {
            System.out.println("Tour " + turn);
            // Ordre d'attaque basé sur la vitesse (simplifié)
            if (pokemon1.getSpeed() >= pokemon2.getSpeed()) {
                executeTurn(pokemon1, pokemon2);
                if(pokemon2.getCurrentHP() > 0) {
                    executeTurn(pokemon2, pokemon1);
                }
            } else {
                executeTurn(pokemon2, pokemon1);
                if(pokemon1.getCurrentHP() > 0) {
                    executeTurn(pokemon1, pokemon2);
                }
            }
            turn++;
        }
        if(pokemon1.getCurrentHP() <= 0) {
            System.out.println(pokemon1.getName() + " est KO! " + pokemon2.getName() + " gagne!");
        } else {
            System.out.println(pokemon2.getName() + " est KO! " + pokemon1.getName() + " gagne!");
        }
    }

    private void executeTurn(Pokemon attacker, Pokemon defender) {
        if(attacker.getMoves().isEmpty()){
            System.out.println(attacker.getName() + " n'a aucune attaque!");
            return;
        }
        Attack move = attacker.getMoves().get(0); // utilisation simplifiée de la première attaque
        System.out.println(attacker.getName() + " utilise " + move.getName());
        int damage = attacker.calculateDamage(defender, move);
        defender.receiveDamage(damage);
        System.out.println(defender.getName() + " reçoit " + damage + " dégâts. (HP restant: " + defender.getCurrentHP() + ")");
        move.applyEffect(attacker, defender , damage , null);
    }
}
