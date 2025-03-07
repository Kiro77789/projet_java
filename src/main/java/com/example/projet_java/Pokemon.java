package com.example.projet_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Pokemon {
    private String name;
    private int maxHP;
    private int currentHP;
    private int attack;
    private int defense;
    private int attackSpe;
    private int defenseSpe;
    private int speed;
    private List<Types> types;
    private List<Attack> moves;
    private Talent talent;
    private Item heldItem;
    private Status status;
    private boolean megaEvolved;
    // EV allocation (exprimés en points)
    private int evAttack;
    private int evDefense;
    private int evSpeed;

    public Pokemon(String name, int hp, int attack, int defense,
                   int attackSpe, int defenseSpe, int speed,
                   String[] typeNames, Talent talent) {
        this.name = name;
        this.maxHP = hp;
        this.currentHP = hp;
        this.attack = attack;
        this.defense = defense;
        this.attackSpe = attackSpe;
        this.defenseSpe = defenseSpe;
        this.speed = speed;
        this.types = new ArrayList<>();
        for (String t : typeNames) {
            Types type = TypeDatabase.getType(t);
            if (type != null) {
                this.types.add(type);
            }
        }
        // Assigne un talent éventuel
        this.talent = talent;
        // Par défaut, pas d'objet tenu
        this.heldItem = null;
        // Statut par défaut
        this.status = Status.NONE;
        this.megaEvolved = false;

        // Assigne automatiquement des attaques en fonction du type
        this.moves = MoveDatabase.getDefaultMoves(this);
    }

    public static Pokemon copyOf(Pokemon original) {
        // Nouveau tableau de types (String[]) pour le constructeur
        List<String> typeNames = new ArrayList<>();
        for (Types t : original.getTypes()) {
            typeNames.add(t.getName());
        }

        // Création du nouveau Pokémon
        Pokemon copy = new Pokemon(
                original.getName(),
                original.getMaxHP(),
                original.getAttack() - original.evAttack,  // on retire l'EV pour récupérer la stat de base
                original.getDefense() - original.evDefense,
                original.getAttackSpe(),
                original.getDefenseSpe(),
                original.getSpeed() - original.evSpeed,
                typeNames.toArray(new String[0]),
                original.getTalent()
        );
        // Copie du statut, EV, etc.
        copy.evAttack  = original.evAttack;
        copy.evDefense = original.evDefense;
        copy.evSpeed   = original.evSpeed;
        copy.setStatus(original.getStatus());
        copy.megaEvolved = original.megaEvolved;

        // Copie des attaques
        List<Attack> newMoves = new ArrayList<>(original.getMoves());
        copy.setMoves(newMoves);

        return copy;
    }


    public String getName() {
        return name;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getAttack() {
        return attack + evAttack;
    }

    public int getDefense() {
        return defense + evDefense;
    }

    public int getAttackSpe() {
        return attackSpe;
    }

    public int getDefenseSpe() {
        return defenseSpe;
    }

    public int getSpeed() {
        return speed + evSpeed;
    }

    public List<Types> getTypes() {
        return types;
    }

    public List<Attack> getMoves() {
        return moves;
    }

    public void setMoves(List<Attack> moves) {
        this.moves = moves;
    }

    public Talent getTalent() {
        return talent;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isMegaEvolved() {
        return megaEvolved;
    }

    // Méga‑Évolution : change le nom et augmente les 2 plus grosses stats de +30
    public void megaEvolve() {
        if (!megaEvolved && heldItem == null) {
            // Renommer le Pokémon
            this.name = "Mega-" + this.name;

            // On récupère les stats sous forme d'un tableau [valeur, index]
            int[][] statValues = new int[][] {
                    { this.attack, 0 },
                    { this.defense, 1 },
                    { this.attackSpe, 2 },
                    { this.defenseSpe, 3 },
                    { this.speed, 4 }
            };
            // On trie par ordre décroissant de la valeur
            java.util.Arrays.sort(statValues, (a, b) -> b[0] - a[0]);

            // On augmente de 30 les 2 premières
            for (int i = 0; i < 2; i++) {
                int index = statValues[i][1];
                switch (index) {
                    case 0: this.attack += 30; break;
                    case 1: this.defense += 30; break;
                    case 2: this.attackSpe += 30; break;
                    case 3: this.defenseSpe += 30; break;
                    case 4: this.speed += 30; break;
                }
            }
            this.megaEvolved = true;
        }
    }

    // Calcul des dégâts avec prise en compte du talent
    public int calculateDamage(Pokemon opponent, Attack move) {
        // 1) Vérifie le talent du défenseur (LevitationTalent)
        if (opponent.getTalent() instanceof LevitationTalent) {
            // Si l'attaque est de type Sol => 0 dégâts
            if (move.getType() != null && "Sol".equalsIgnoreCase(move.getType().getName())) {
                return 0;
            }
        }

        // 2) Vérifie le talent de l'attaquant (BrasierTalent)
        double multiplierTalent = 1.0;
        if (this.getTalent() instanceof BrasierTalent) {
            // Si PV < 1/3 et type Feu => x1.5
            if (this.getCurrentHP() < (this.getMaxHP() / 3)) {
                if (move.getType() != null && "Feu".equalsIgnoreCase(move.getType().getName())) {
                    multiplierTalent = 1.5;
                }
            }
        }

        // 3) Calcul normal du multiplicateur de type
        double multiplierType = calculateMultiplier(opponent);

        // 4) Facteur aléatoire
        double randomFactor = 0.85 + Math.random() * 0.15;

        int usedAttack = move.isSpecial() ? this.attackSpe : this.attack;
        int usedDefense = move.isSpecial() ? opponent.getDefenseSpe() : opponent.getDefense();

        int baseDamage = (int) (((usedAttack * move.getPower()) / (double) usedDefense)
                * multiplierType
                * multiplierTalent
                * randomFactor);

        return baseDamage;
    }

    // Calcule le multiplicateur d’efficacité (type) sans tenir compte du talent
    public double calculateMultiplier(Pokemon opponent) {
        double multiplier = 1.0;
        for (Types myType : this.types) {
            for (Types oppType : opponent.getTypes()) {
                if (myType.isStrongAgainst(oppType.getName())) {
                    multiplier *= 2.0;
                } else if (myType.isWeakAgainst(oppType.getName())) {
                    multiplier *= 0.5;
                }
            }
        }
        return multiplier;
    }

    public void receiveDamage(int damage) {
        currentHP = Math.max(currentHP - damage, 0);
    }

    public void heal(int amount) {
        currentHP = Math.min(currentHP + amount, maxHP);
    }

    public void allocateEVs(int attackEV, int defenseEV, int speedEV) {
        if (attackEV <= 20 && defenseEV <= 20 && speedEV <= 20 &&
                (attackEV + defenseEV + speedEV) <= 50) {
            this.evAttack = attackEV;
            this.evDefense = defenseEV;
            this.evSpeed = speedEV;
        }
    }

    @Override
    public String toString() {
        return name + " (HP:" + currentHP + "/" + maxHP + ")";
    }
}
