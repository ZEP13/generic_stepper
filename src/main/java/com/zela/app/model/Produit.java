package com.zela.app.model;

public class Produit {

	private String nom;
	private double prix;
	private int quantite;

	public Produit(String nom, double prix, int quantite) {
		this.nom = nom;
		this.prix = prix;
		this.quantite = quantite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	@Override
	public String toString() {
		return "Produit{" +
				"nom='" + nom + '\'' +
				", prix=" + prix +
				", quantite=" + quantite +
				'}';
	}
}
