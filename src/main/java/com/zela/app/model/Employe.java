package com.zela.app.model;

public class Employe {

    private String nom;
    private String prenom;
    private int age;
    private int actif;

    public Employe(String nom, String prenom, int age, int actif) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.actif = actif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getActif() {
        return actif;
    }

    public void setActif(int actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Personnes{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                '}';
    }

}
