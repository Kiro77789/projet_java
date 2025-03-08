package com.example.projet_java;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Pokemon {
    private String name;
    private int maxHP;
    private int currentHP;
    private int baseAttack;
    private int baseDefense;
    private int baseAttackSpe;
    private int baseDefenseSpe;
    private int baseSpeed;
    private List<Types> types;
    private List<Attack> moves;
    private Talent talent;
    private Item heldItem;
    private Status status;
    private boolean megaEvolved;

    // EV
    int evAttack;
    int evDefense;
    int evSpeed;

    // Stages
    private int attackStage = 0;
    private int defenseStage = 0;
    private int speedStage = 0;
    private int attackSpeStage = 0;
    private int defenseSpeStage = 0;

    public Pokemon(String name, int hp, int attack, int defense,
                   int attackSpe, int defenseSpe, int speed,
                   String[] typeNames, Talent talent) {
        this.name = name;
        this.maxHP = hp;
        this.currentHP = hp;
        this.baseAttack = attack;
        this.baseDefense = defense;
        this.baseAttackSpe = attackSpe;
        this.baseDefenseSpe = defenseSpe;
        this.baseSpeed = speed;

        this.types = new ArrayList<>();
        for (String t : typeNames) {
            Types type = TypeDatabase.getType(t);
            if (type != null) {
                this.types.add(type);
            }
        }
        this.talent = talent;
        this.heldItem = null;
        this.status = Status.NONE;
        this.megaEvolved = false;
        this.moves = MoveDatabase.getDefaultMoves(this);
    }

    public List<Types> getTypes() {
        return types;
    }

    // Copie pour éviter de partager la même instance
    public static Pokemon copyOf(Pokemon original) {
        List<String> typeNames = new ArrayList<>();
        for (Types t : original.getTypes()) {
            typeNames.add(t.getName());
        }
        Pokemon copy = new Pokemon(
                original.getName(),
                original.getMaxHP(),
                original.baseAttack,
                original.baseDefense,
                original.baseAttackSpe,
                original.baseDefenseSpe,
                original.baseSpeed,
                typeNames.toArray(new String[0]),
                original.getTalent()
        );
        copy.currentHP = original.currentHP;
        copy.evAttack = original.evAttack;
        copy.evDefense = original.evDefense;
        copy.evSpeed = original.evSpeed;
        copy.status = original.status;
        copy.megaEvolved = original.megaEvolved;

        copy.attackStage = original.attackStage;
        copy.defenseStage = original.defenseStage;
        copy.speedStage = original.speedStage;
        copy.attackSpeStage = original.attackSpeStage;
        copy.defenseSpeStage = original.defenseSpeStage;

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

    // Méthodes d'accès aux stats en tenant compte des EV et des stages
    public int getAttack() {
        double base = (baseAttack + evAttack);
        return (int)(base * stageMultiplier(attackStage));
    }

    public int getDefense() {
        double base = (baseDefense + evDefense);
        return (int)(base * stageMultiplier(defenseStage));
    }

    public int getSpeed() {
        double base = (baseSpeed + evSpeed);
        return (int)(base * stageMultiplier(speedStage));
    }

    public int getAttackSpe() {
        return (int)(baseAttackSpe * stageMultiplier(attackSpeStage));
    }

    public int getDefenseSpe() {
        return (int)(baseDefenseSpe * stageMultiplier(defenseSpeStage));
    }

    // Calcule le multiplicateur en fonction du stage (ex: +1 stage => x1.5)
    private double stageMultiplier(int stage) {
        // Système standard Pokémon
        // stage = -6..+6
        // -1 => x0.67, +1 => x1.5, +2 => x2.0, etc.
        // Formule : (2 + stage) / 2 si stage >= 0
        //           2 / (2 + |stage|) si stage < 0
        if (stage >= 0) {
            return (2.0 + stage) / 2.0;
        } else {
            return 2.0 / (2.0 + Math.abs(stage));
        }
    }

    // Méthodes pour modifier les stages
    public void changeStage(String stat, int amount) {
        switch (stat.toLowerCase()) {
            case "attack":
                attackStage = clampStage(attackStage + amount);
                break;
            case "defense":
                defenseStage = clampStage(defenseStage + amount);
                break;
            case "speed":
                speedStage = clampStage(speedStage + amount);
                break;
            case "spattack":
                attackSpeStage = clampStage(attackSpeStage + amount);
                break;
            case "spdefense":
                defenseSpeStage = clampStage(defenseSpeStage + amount);
                break;
        }
    }

    // Réinitialise les stages quand on switch
    public void resetStages() {
        attackStage = 0;
        defenseStage = 0;
        speedStage = 0;
        attackSpeStage = 0;
        defenseSpeStage = 0;
    }

    private int clampStage(int val) {
        if (val > 6) return 6;
        if (val < -6) return -6;
        return val;
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

    // Méga-Évolution : renomme + augmente 2 plus grosses stats de +30
    public void megaEvolve() {
        // Seuls les Pokémon pouvant mega évoluer sont Mewtwo, Onix et Dracaufeu.
        // On vérifie ici le nom de base. Si le Pokémon a déjà été mega évolué, son nom commence par "Mega-".
        if (this.name.startsWith("Mega-")) {
            return; // déjà mega évolué
        }
        if (!(this.name.equals("Mewtwo") || this.name.equals("Onix") || this.name.equals("Dracaufeu"))) {
            // Ce Pokémon ne peut pas mega évoluer
            return;
        }
        if (heldItem != null) {
            return; // Un Pokémon qui tient un objet ne peut pas mega évoluer.
        }

        // Effectue la méga évolution : renommer et augmenter de +30 les 2 statistiques les plus élevées.
        this.name = "Mega-" + this.name;
        int[][] stats = new int[][] {
                { baseAttack, 0 },
                { baseDefense, 1 },
                { baseAttackSpe, 2 },
                { baseDefenseSpe, 3 },
                { baseSpeed, 4 }
        };
        java.util.Arrays.sort(stats, (a, b) -> b[0] - a[0]);
        for (int i = 0; i < 2; i++) {
            int idx = stats[i][1];
            switch (idx) {
                case 0: baseAttack += 30; break;
                case 1: baseDefense += 30; break;
                case 2: baseAttackSpe += 30; break;
                case 3: baseDefenseSpe += 30; break;
                case 4: baseSpeed += 30; break;
            }
        }
        this.megaEvolved = true;
    }


    // Calcul des dégâts en tenant compte des talents, type chart, etc.
    public int calculateDamage(Pokemon opponent, Attack move) {
        // Vérifier statut Sleep ou Paralysis : le Pokémon peut ne pas attaquer
        if (this.status == Status.SLEEP) {
            // On pourrait gérer un compteur de tours de sommeil, simplifions : 50% de chance de ne pas attaquer
            if (Math.random() < 0.5) {
                return 0; // On renvoie 0 => pas d'attaque
            }
        } else if (this.status == Status.PARALYSIS) {
            // 25% de chance de ne pas attaquer
            if (Math.random() < 0.25) {
                return 0;
            }
        }

        // Talent Levitation, Talent Brasier, etc. (omise ici pour la brièveté)
        double multiplierTalent = 1.0;

        // Multiplicateur de type
        double multiplierType = TypeDatabase.getEffectiveness(move.getType(), opponent.getTypes());
        // On ne gère pas le talent Brasier, Levitation, etc. => Cf versions antérieures
        double randomFactor = 0.85 + Math.random() * 0.15;

        int usedAttack = move.isSpecial() ? getAttackSpe() : getAttack();
        int usedDefense = move.isSpecial() ? opponent.getDefenseSpe() : opponent.getDefense();

        int baseDamage = (int)(((usedAttack * move.getPower()) / (double)usedDefense)
                * multiplierType * multiplierTalent * randomFactor);
        return Math.max(baseDamage, 0);
    }

    public double getSpeedValueForPriorityCheck() {
        // La vitesse effective (ex: si le Pokémon est paralysé, on la divise par 4, etc.)
        double speed = getSpeed();
        if (this.status == Status.PARALYSIS) {
            // Dans Pokémon, PARALYSIS divise la vitesse par 2
            speed /= 2.0;
        }
        return speed;
    }

    public void receiveDamage(int damage) {
        currentHP = Math.max(currentHP - damage, 0);
    }

    public void heal(int amount) {
        currentHP = Math.min(currentHP + amount, maxHP);
    }

    public void setMoves(List<Attack> moves) {
        this.moves = moves;
    }

    public List<Attack> getMoves() {
        return moves;
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
        // Afficher un résumé y compris les stages
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (HP:").append(currentHP).append("/").append(maxHP).append(")");
        // Ex: +1 Att, +2 Spe, ...
        List<String> stageInfo = new ArrayList<>();
        if (attackStage != 0) stageInfo.add(attackStage > 0 ? "+"+attackStage+" Att" : attackStage+" Att");
        if (defenseStage != 0) stageInfo.add(defenseStage > 0 ? "+"+defenseStage+" Def" : defenseStage+" Def");
        if (speedStage != 0) stageInfo.add(speedStage > 0 ? "+"+speedStage+" Spe" : speedStage+" Spe");
        if (attackSpeStage != 0) stageInfo.add(attackSpeStage > 0 ? "+"+attackSpeStage+" SpA" : attackSpeStage+" SpA");
        if (defenseSpeStage != 0) stageInfo.add(defenseSpeStage > 0 ? "+"+defenseSpeStage+" SpD" : defenseSpeStage+" SpD");
        if (!stageInfo.isEmpty()) {
            sb.append(" [").append(String.join(", ", stageInfo)).append("]");
        }
        return sb.toString();
    }
}
