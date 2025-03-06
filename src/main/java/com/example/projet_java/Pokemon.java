package com.example.projet_java;

public class Pokemon {

    private String name;
    private int HP;
    private int Attack;
    private int Defense;
    private int AttackSpe;
    private int DefenseSpe;
    private int vitesse;
    public Types type;
    public String object;
    // Tableau de capacités (4 coups)
    private String[] moves;

    public Pokemon(String name, int HP, int Attack, int Defense, int AttackSpe, int DefenseSpe, int vitesse, String type, String object) {
        this.name = name;
        this.HP = HP;
        this.Attack = Attack;
        this.Defense = Defense;
        this.AttackSpe = AttackSpe;
        this.DefenseSpe = DefenseSpe;
        this.vitesse = vitesse;
        this.type = TypeDatabase.getType(type);
        this.object = object;
        // Capacités par défaut
        this.moves = new String[]{"Move1", "Move2", "Move3", "Move4"};
    }

    // Getters pour les statistiques
    public String getName() {
        return name;
    }

    public int getHP() {
        return HP;
    }

    public int getAttack() {
        return Attack;
    }

    public int getDefense() {
        return Defense;
    }

    public int getAttackSpe() {
        return AttackSpe;
    }

    public int getDefenseSpe() {
        return DefenseSpe;
    }

    public int getVitesse() {
        return vitesse;
    }

    // Getters et setters pour les capacités
    public String[] getMoves() {
        return moves;
    }

    public void setMoves(String[] moves) {
        this.moves = moves;
    }

    // Méthodes de boost et autres
    public void boostDef(Object objec) {
        if (objec != null && object.equals(objec.Pierre)) {
            Defense += 50;
        }
    }

    public void boostVit(Object objec) {
        if (objec != null && object.equals(objec.Plume)) {
            vitesse += 50;
        }
    }

    public void boostAtt(Object objec) {
        if (objec != null && object.equals(objec.Poings)) {
            Attack += 50;
        }
    }

    public void Ceinture(Object objec) {
        if (HP == 0) {
            HP += 1;
        }
    }

    public int calculerDommages(Pokemon adversaire) {
        double multiplicateur = 1.0;
        if (this.type.isStrongAgainst(adversaire.type.getName())) {
            multiplicateur = 2.0; // Super efficace
        } else if (this.type.isWeakAgainst(adversaire.type.getName())) {
            multiplicateur = 0.5; // Pas très efficace
        } else if (adversaire.type.isResistantTo(this.type.getName())) {
            multiplicateur = 0; // Résistance
        }
        return (int) (((this.Attack * 2) / (double)adversaire.Defense) * multiplicateur);
    }
}
